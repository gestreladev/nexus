CREATE TYPE document_status AS ENUM ('pending', 'processing', 'ready', 'failed');

CREATE TABLE documents (
    id          UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID            NOT NULL,
    title       VARCHAR(500)    NOT NULL,
    status      document_status NOT NULL DEFAULT 'pending',
    created_at  TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ     NOT NULL DEFAULT now(),

    CONSTRAINT documents_user_fk FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

-- Standard index: all queries filtering by user
CREATE INDEX idx_documents_user_id ON documents(user_id);

-- Partial index: only indexes the small subset actively being processed
-- (see: Q2 from lesson — low-cardinality column, high selectivity for one value)
CREATE INDEX idx_documents_processing
    ON documents(id)
    WHERE status = 'processing';

COMMENT ON TABLE  documents        IS 'User-uploaded documents awaiting ingestion';
COMMENT ON COLUMN documents.status IS 'pending→processing→ready|failed lifecycle';
