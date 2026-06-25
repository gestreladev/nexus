"""Integration test: the full chunk → embed → upsert → search path.

Uses the deterministic FakeEmbedder (no model/key) against the live Postgres, so
it proves the pipeline, idempotency, and search ranking without external deps.
Skips cleanly if Postgres isn't reachable (e.g. plain unit-test runs).
"""

import uuid

import pytest

from app.chunking.character import CharacterChunker
from app.config import settings
from app.db import Database
from app.embeddings.fake import FakeEmbedder
from app.ingest import Ingestor

CONTENT = (
    "The Kafka broker advertises dual listeners so host and container clients connect. " * 3
    + "Redis provides cache-aside reads and a JWT denylist for token revocation. " * 3
    + "Postgres with pgvector stores embeddings and runs HNSW nearest-neighbor search. " * 3
)


async def _seed(db: Database, content: str) -> tuple[str, uuid.UUID]:
    uid, did = uuid.uuid4(), uuid.uuid4()
    async with db.pool.acquire() as conn:
        await conn.execute(
            "INSERT INTO users (id, email, display_name, password_hash, created_at, updated_at)"
            " VALUES ($1, $2, $3, 'x', now(), now())",
            uid, f"itest-{uid}@nexus.dev", "itest",
        )
        await conn.execute(
            "INSERT INTO documents (id, user_id, title, content, status, created_at, updated_at)"
            " VALUES ($1, $2, 'itest', $3, 'pending', now(), now())",
            did, uid, content,
        )
    return str(did), uid


async def _cleanup(db: Database, did: str, uid: uuid.UUID) -> None:
    async with db.pool.acquire() as conn:
        await conn.execute("DELETE FROM documents WHERE id = $1", uuid.UUID(did))
        await conn.execute("DELETE FROM users WHERE id = $1", uid)


@pytest.mark.asyncio
async def test_pipeline_idempotency_and_search() -> None:
    db = Database(settings.db_dsn)
    try:
        await db.connect()
    except Exception as exc:  # noqa: BLE001
        pytest.skip(f"Postgres not reachable at {settings.db_dsn}: {exc}")

    did, uid = await _seed(db, CONTENT)
    chunker = CharacterChunker(80, 20)
    embedder = FakeEmbedder(dim=settings.embedding_dim)
    try:
        n = await Ingestor(db, embedder, chunker).process(did)
        assert n > 1, "expected multiple chunks"

        # Idempotency: re-processing the same id upserts, never duplicates.
        again = await Ingestor(db, embedder, chunker).process(did)
        assert again == n
        async with db.pool.acquire() as conn:
            stored = await conn.fetchval(
                "SELECT count(*) FROM document_chunks WHERE document_id = $1", uuid.UUID(did)
            )
        assert stored == n

        # A query equal to a stored chunk's text returns that chunk first
        # (Fake → identical vector → cosine 1).
        target = chunker.chunk(CONTENT)[1]
        hits = await db.search(embedder.embed_query(target), k=3)
        assert hits, "search returned nothing"
        assert hits[0]["content"] == target
        assert float(hits[0]["score"]) > 0.99
    finally:
        await _cleanup(db, did, uid)
        await db.close()
