---
name: lesson_03_database
description: Lesson 3 log — PostgreSQL, ACID, indexes, migrations, ORM + data layer.
agent:
  role: tutor
  tier: nano
  weight: soft
  triggers:
    - reviewing what Lesson 3 covered
    - recapping the data layer before a session
metadata:
  type: reference
---

# Lesson 3 — Data Layer (Phase 2)

| Field | Value |
|---|---|
| Phase | 2 — Databases |
| Roadmap topics | relational-databases, postgresql, acid, transactions, normalization, database-indexes, migrations, orms |
| Deliverable | PostgreSQL data layer — Flyway, Exposed, HikariCP, users/documents |
| Milestone | `v0.2.0` ✅ |
| Status | ✅ mastery pass |

## Concepts taught
ACID, transactions as unit of work, normalization (1NF–3NF), B-tree vs **partial
index**, migrations (Flyway), ORM via Exposed DSL, connection pooling (HikariCP),
Repository pattern.

## Exercises
| Q | Topic | Verdict |
|---|---|---|
| 1 | Atomicity violated; fix = wrap both ops in one transaction | 🟡 → refined (said "redo"; corrected to transaction) |
| 2 | Slow `status='processing'` query → **partial index** | 🟡 → learned partial index |
| 3 | `user_email` in documents breaks 3NF; FK to users instead | ❌ → explained |

## Gaps to revisit
- **3NF normalization** was new — re-surface in Recap at Lesson 4.
- Partial indexes — applied in `V2__create_documents.sql`; confirm retention.

## Notes
Exercises mapped directly to shipped code: transaction-wrapped repos, partial
index `idx_documents_processing`, no denormalized `user_email`.

## References
- [lesson_02_first_service.md](lesson_02_first_service.md) · `[[lesson_02_first_service]]`
- [lesson_orchestrator.md](../lesson_orchestrator.md) · `[[lesson_orchestrator]]`
