-- Phase 9: documents need their text to be embeddable. `content` was part of the
-- API shape conceptually but never persisted (the table only stored `title`).
-- Store it now — `documents` is the system of record; nexus-ingest fetches the
-- text by id (claim-check) to chunk + embed it.
ALTER TABLE documents ADD COLUMN content TEXT NOT NULL DEFAULT '';

COMMENT ON COLUMN documents.content IS 'Raw document text; chunked + embedded by nexus-ingest (Phase 9)';
