"""The ingestion pipeline: a document id → chunk → embed → store its vectors.

Idempotent (Kafka is at-least-once): re-processing the same id upserts the same
chunks. The embedder runs in a worker thread because encoding (a local model) or
an HTTP call (Voyage) would otherwise block the asyncio event loop.
"""

import asyncio
import logging

from app.chunking.base import ChunkingStrategy
from app.db import Chunk, Database
from app.embeddings.base import EmbeddingStrategy

log = logging.getLogger("nexus-ingest")


class Ingestor:
    def __init__(
        self, db: Database, embedder: EmbeddingStrategy, chunker: ChunkingStrategy
    ) -> None:
        self._db = db
        self._embedder = embedder
        self._chunker = chunker

    async def process(self, document_id: str) -> int:
        content = await self._db.fetch_document_content(document_id)
        if not content:
            log.info("document %s has no content; skipping", document_id)
            return 0

        chunks = self._chunker.chunk(content)
        if not chunks:
            return 0

        # Offload the CPU/IO-bound embedding off the event loop.
        vectors = await asyncio.to_thread(self._embedder.embed_documents, chunks)
        rows: list[Chunk] = [
            (i, text, vec) for i, (text, vec) in enumerate(zip(chunks, vectors, strict=True))
        ]
        written = await self._db.upsert_chunks(document_id, rows)
        log.info("embedded document %s → %d chunk(s)", document_id, written)
        return written
