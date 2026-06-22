---
name: indexing
description: Database indexes — B-tree, partial, and selectivity.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - deciding whether/how to index a column
    - diagnosing a slow query
metadata:
  type: reference
---

# Indexing

A table without indexes is a **full scan** per query. An index is a separate
data structure (B-tree by default in PostgreSQL) that lets the engine jump to
matching rows.

```sql
-- full scan
SELECT * FROM documents WHERE user_id = $1;
-- with index → jump directly
CREATE INDEX idx_documents_user_id ON documents(user_id);
```

## Rules of thumb
- Primary keys are indexed automatically.
- **Foreign keys** should almost always be indexed — you filter/join on them.
- Index columns used in `WHERE`, `ORDER BY`, `JOIN ON`.
- Indexes cost on **write** (maintained on every insert/update/delete) — don't
  index everything.

## Selectivity
An index helps most when a query matches *few* rows. A low-cardinality column
(few distinct values, e.g. `status`) is a poor plain index — the planner may
ignore it because too many rows match.

## Partial index
When one specific value is rare/hot, index only those rows:
```sql
CREATE INDEX idx_documents_processing
  ON documents(id) WHERE status = 'processing';
```
Tiny index, stays in memory, used only for that predicate (the Lesson 3 Q2
insight — 10M rows, ~0.1% processing → index ~10K rows).

## Why it matters in Nexus
`documents` has a standard FK index on `user_id` and a **partial** index on
`status = 'processing'`. Visible in `\d documents`.

## References
- [databases_orchestrator.md](databases_orchestrator.md) · `[[databases_orchestrator]]`
- [relational_model.md](relational_model.md) · `[[relational_model]]`
