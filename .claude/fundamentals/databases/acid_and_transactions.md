---
name: acid_and_transactions
description: ACID guarantees and the transaction as a unit of work.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - reasoning about transaction boundaries
    - understanding atomicity/consistency/isolation/durability
metadata:
  type: reference
---

# ACID & Transactions

A **transaction** is a unit of work: a group of operations that succeed or fail
together. PostgreSQL guarantees ACID:

| Letter | Property | Meaning |
|---|---|---|
| **A** | Atomicity | all operations commit, or none do — no partial writes |
| **C** | Consistency | only valid states; constraints never violated |
| **I** | Isolation | concurrent transactions don't see each other's uncommitted changes |
| **D** | Durability | once committed, it survives a crash (on disk) |

## The pattern
```sql
BEGIN;
  INSERT INTO documents (...) VALUES (...);
  UPDATE users SET document_count = document_count + 1 WHERE id = $1;
COMMIT;   -- both apply, or ROLLBACK undoes both
```

If anything fails between `BEGIN` and `COMMIT`, the engine rolls back — the app
never sees a half-done state. You **prevent** the inconsistency by design rather
than handle it after the fact (the Lesson 3 R1 insight).

## Isolation levels
Trade consistency vs concurrency: `READ COMMITTED` (PostgreSQL default) →
`REPEATABLE READ` → `SERIALIZABLE` (strictest, may abort on conflict). Default is
fine until you hit specific anomalies.

## Why it matters in Nexus
Repositories own the transaction boundary: every multi-step write runs inside
`transaction { }`. See
[repository.md](../../services/nexus-api/architecture/repository.md) · `[[repository]]`.

## References
- [databases_orchestrator.md](databases_orchestrator.md) · `[[databases_orchestrator]]`
- [orm_and_connection_pooling.md](orm_and_connection_pooling.md) · `[[orm_and_connection_pooling]]`
