---
name: pgvector
description: Postgres vector search with pgvector — vector(N) columns, distance operators, HNSW indexing, and asyncpg round-tripping.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - storing or querying embeddings in Postgres
    - choosing a distance operator or building an HNSW/IVFFlat index
    - wiring asyncpg so vector columns round-trip
metadata:
  type: reference
---

# pgvector

`pgvector` is the Postgres extension that adds a native `vector` type plus
distance operators and ANN indexes — it lets Postgres be the vector store, so
embeddings live next to relational data and join cleanly.

## Setup and column type

```sql
CREATE EXTENSION IF NOT EXISTS vector;
-- a 1024-dimensional embedding column
ALTER TABLE document_chunks ADD COLUMN embedding vector(1024);
```

`vector(N)` fixes the dimensionality at `N`; every row must match. For large
corpora, `halfvec(N)` stores each component as a 16-bit float — half the disk
and index size, negligible recall loss — and is the storage type of choice when
RAM for the index is the constraint.

## Distance operators

The operator you query with **must match the opclass** of the index, or the
planner skips the index and does a full scan.

| Operator | Distance | Index opclass | Notes |
|----------|----------|---------------|-------|
| `<->` | L2 (Euclidean) | `vector_l2_ops` | raw magnitude matters |
| `<#>` | negative inner product | `vector_ip_ops` | returns `-(a·b)` |
| `<=>` | cosine | `vector_cosine_ops` | **use this** — magnitude-invariant |

Use `<=>` with `vector_cosine_ops`. Cosine ignores magnitude, which is what you
want for normalized embeddings (see `[[embeddings]]`).

## Index: HNSW (the 2026 default)

```sql
CREATE INDEX ON document_chunks
  USING hnsw (embedding vector_cosine_ops);
```

HNSW (Hierarchical Navigable Small World) is the default in 2026: strong
recall/latency, **no training step**, and it can be built on an empty table.
IVFFlat is the clustering alternative — needs a `lists` parameter and a training
step over existing rows, so it must be (re)built after data lands. Both are
**ANN** (approximate nearest neighbor): they trade a little recall for
sub-linear search. Tuning lives in `[[vector_indexes_ann]]`.

## Query

```sql
SELECT id, content
FROM document_chunks
ORDER BY embedding <=> $1     -- $1 is the query vector
LIMIT $2;                     -- k results
```

`ORDER BY <op> LIMIT k` is the shape the index accelerates — it walks the HNSW
graph instead of scanning. Anything that breaks the `ORDER BY ... LIMIT` form
(e.g. a function around `embedding`) silently falls back to a seq scan.

## asyncpg: register_vector

asyncpg sends `vector` as text unless you register the codec. Do it on pool
init so columns round-trip as `pgvector.Vector`:

```python
from pgvector.asyncpg import register_vector

pool = await asyncpg.create_pool(dsn, init=register_vector)
# now: await conn.fetch(sql, query_vec)  # query_vec is a list/np.ndarray
```

Registering on `init` applies the codec to every connection the pool hands out;
without it you must serialize vectors to strings by hand.

## In Nexus

Phase 9 stores chunk embeddings in `document_chunks`:

```sql
CREATE TABLE document_chunks (
    id            BIGSERIAL PRIMARY KEY,
    document_id   BIGINT NOT NULL REFERENCES documents(id),
    chunk_index   INT    NOT NULL,
    content       TEXT   NOT NULL,
    embedding     vector(1024) NOT NULL,
    UNIQUE (document_id, chunk_index)
);

CREATE INDEX ON document_chunks USING hnsw (embedding vector_cosine_ops);
```

- **1024 dims** is fixed so the local model (mxbai-embed-large-v1) and Voyage
  (`output_dimension=1024`) are swappable without a re-embed migration.
- **Idempotent upsert** keyed on `(document_id, chunk_index)` — re-running
  ingestion overwrites rather than duplicating:

```sql
INSERT INTO document_chunks (document_id, chunk_index, content, embedding)
VALUES ($1, $2, $3, $4)
ON CONFLICT (document_id, chunk_index)
DO UPDATE SET content = EXCLUDED.content,
              embedding = EXCLUDED.embedding;
```

This idempotency lets the ingest worker retry safely after a crash without
re-embedding the whole document (queue mechanics in
[messaging_orchestrator.md](../messaging/messaging_orchestrator.md) ·
`[[messaging_orchestrator]]`). Search is the `<=>` / `LIMIT k` query above,
fed into hybrid scoring in `[[hybrid_search]]`.

## References

- Orchestrator: [search_orchestrator.md](search_orchestrator.md) · `[[search_orchestrator]]`
- ANN index tuning: `[[vector_indexes_ann]]`
- What embeddings are: `[[embeddings]]`
- Lexical contrast: `[[lexical_vs_semantic]]` · combining signals: `[[hybrid_search]]`
- Postgres indexing fundamentals: [indexing.md](../databases/indexing.md) · `[[indexing]]`
- Database design: [databases_orchestrator.md](../databases/databases_orchestrator.md) · `[[databases_orchestrator]]`
- Lesson log: [lesson_10_search.md](../../lessons/log/lesson_10_search.md) · `[[lesson_10_search]]`
