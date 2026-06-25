"""Postgres access for ingestion — read document text, write/search chunk vectors.

`register_vector` teaches asyncpg the pgvector codecs so `vector(1024)` columns
round-trip as `pgvector.Vector`. The upsert is keyed on (document_id, chunk_index)
so a redelivered Kafka event overwrites rather than duplicates (idempotency).
"""

import logging
import uuid
from typing import Any, cast

import asyncpg
from pgvector import Vector
from pgvector.asyncpg import register_vector

log = logging.getLogger("nexus-ingest")

# (chunk_index, content, embedding)
Chunk = tuple[int, str, list[float]]


class Database:
    def __init__(self, dsn: str) -> None:
        self._dsn = dsn
        self._pool: asyncpg.Pool | None = None

    async def connect(self) -> None:
        async def init(conn: asyncpg.Connection) -> None:
            await register_vector(conn)

        self._pool = await asyncpg.create_pool(self._dsn, init=init, min_size=1, max_size=5)
        log.info("Database pool connected")

    async def close(self) -> None:
        if self._pool is not None:
            await self._pool.close()

    @property
    def pool(self) -> asyncpg.Pool:
        if self._pool is None:
            raise RuntimeError("Database.connect() was not awaited")
        return self._pool

    async def fetch_document_content(self, document_id: str) -> str | None:
        """The claim-check read: the event carries only the id; the text lives here."""
        value = await self.pool.fetchval(
            "SELECT content FROM documents WHERE id = $1", uuid.UUID(document_id)
        )
        return cast("str | None", value)

    async def upsert_chunks(self, document_id: str, chunks: list[Chunk]) -> int:
        if not chunks:
            return 0
        doc = uuid.UUID(document_id)
        records = [(doc, idx, content, Vector(emb)) for idx, content, emb in chunks]
        async with self.pool.acquire() as conn, conn.transaction():
            await conn.executemany(
                """
                INSERT INTO document_chunks (document_id, chunk_index, content, embedding)
                VALUES ($1, $2, $3, $4)
                ON CONFLICT (document_id, chunk_index)
                DO UPDATE SET content = EXCLUDED.content, embedding = EXCLUDED.embedding
                """,
                records,
            )
        return len(records)

    async def search(self, query_embedding: list[float], k: int) -> list[Any]:
        # `<=>` is cosine distance; 1 - distance = cosine similarity (higher = closer).
        rows = await self.pool.fetch(
            """
            SELECT document_id, chunk_index, content, 1 - (embedding <=> $1) AS score
            FROM document_chunks
            ORDER BY embedding <=> $1
            LIMIT $2
            """,
            Vector(query_embedding),
            k,
        )
        return cast("list[Any]", rows)
