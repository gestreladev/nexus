-- Phase 9 — vector search. Each document is split into chunks; each chunk is
-- embedded into a 1024-dim vector. We standardize on 1024 so the embedding
-- provider (local model vs Voyage API) is swappable without a re-embed migration.
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE document_chunks (
    id           UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id  UUID         NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    chunk_index  INT          NOT NULL,
    content      TEXT         NOT NULL,
    embedding    vector(1024) NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now(),

    -- (document_id, chunk_index) is the idempotency key: re-handling a Kafka
    -- event (at-least-once) upserts the same chunk instead of duplicating it.
    CONSTRAINT document_chunks_doc_chunk_uq UNIQUE (document_id, chunk_index)
);

-- ANN index: HNSW with cosine ops, matching the `<=>` operator used at query time.
-- HNSW is the 2026 default — strong recall/latency, no training step.
CREATE INDEX idx_document_chunks_embedding_hnsw
    ON document_chunks USING hnsw (embedding vector_cosine_ops);

-- Plain B-tree for "all chunks of a document" reads/deletes.
CREATE INDEX idx_document_chunks_document_id ON document_chunks(document_id);

COMMENT ON TABLE document_chunks IS 'Chunked + embedded slices of a document for vector search (Phase 9)';
