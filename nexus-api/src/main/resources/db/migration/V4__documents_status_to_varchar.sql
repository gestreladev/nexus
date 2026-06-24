-- documents.status was a native PG enum (document_status), but the app maps it
-- as a string (the Kotlin DocumentStatus enum). Native enums need an explicit
-- cast on insert, which the ORM doesn't emit. Align the column to VARCHAR so
-- inserts work directly — app-level enum + VARCHAR is the simpler, common choice.

DROP INDEX IF EXISTS idx_documents_processing;
ALTER TABLE documents ALTER COLUMN status DROP DEFAULT;
ALTER TABLE documents ALTER COLUMN status TYPE VARCHAR(50) USING status::text;
ALTER TABLE documents ALTER COLUMN status SET DEFAULT 'pending';

-- Recreate the partial index (predicate now compares varchar).
CREATE INDEX idx_documents_processing ON documents(id) WHERE status = 'processing';

DROP TYPE IF EXISTS document_status;
