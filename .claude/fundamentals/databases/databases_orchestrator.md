---
name: databases_orchestrator
description: Routing for database fundamentals (Lesson 3).
agent:
  role: fundamentals-router
  tier: nano
  weight: soft
  triggers:
    - looking up a relational-database concept
metadata:
  type: orchestrator
---

# Databases — Fundamentals

Relational storage behind `nexus-api`. Taught in Lesson 3.

---

## Routing Table

| Topic | Scope | Document |
|---|---|---|
| Relational model | Tables, keys, relationships, FK | [relational_model.md](relational_model.md) · `[[relational_model]]` |
| ACID & transactions | Guarantees, unit of work | [acid_and_transactions.md](acid_and_transactions.md) · `[[acid_and_transactions]]` |
| Normalization | 1NF → 3NF, spotting violations | [normalization.md](normalization.md) · `[[normalization]]` |
| Indexing | B-tree, partial, selectivity | [indexing.md](indexing.md) · `[[indexing]]` |
| Migrations | Versioned schema change | [migrations.md](migrations.md) · `[[migrations]]` |
| ORM & pooling | Exposed, HikariCP, transactions | [orm_and_connection_pooling.md](orm_and_connection_pooling.md) · `[[orm_and_connection_pooling]]` |

---

## References
- [fundamentals_orchestrator.md](../fundamentals_orchestrator.md) · `[[fundamentals_orchestrator]]`
- [repository.md](../../services/nexus-api/architecture/repository.md) · `[[repository]]`
- [exposed_dsl.md](../../services/nexus-api/idioms/exposed_dsl.md) · `[[exposed_dsl]]`
- [lesson_03_database.md](../../lessons/log/lesson_03_database.md) · `[[lesson_03_database]]`
