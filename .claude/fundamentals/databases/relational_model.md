---
name: relational_model
description: Tables, keys, and relationships in the relational model.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - modeling data as tables and relationships
    - choosing keys
metadata:
  type: reference
---

# Relational Model

Data lives in **tables** (relations): rows (tuples) with typed columns. The
engine — not your app — enforces structure and relationships.

## Keys
| Key | Role |
|---|---|
| **Primary key (PK)** | uniquely identifies a row; Nexus uses UUID PKs |
| **Foreign key (FK)** | a column referencing another table's PK |
| **Unique** | enforces no duplicates on a column (e.g. `users.email`) |

UUID PKs (vs sequential ints): globally unique and don't leak row counts /
insertion order to the outside.

## Relationships
- **one-to-many**: a `user` has many `documents` → FK `documents.user_id`
- **many-to-many**: a join table holds two FKs
- **one-to-one**: FK + unique constraint

## Referential integrity
A FK guarantees the referenced row exists. `ON DELETE CASCADE` removes children
when the parent is deleted (Nexus: deleting a user removes their documents).

## Relational vs NoSQL
Relational shines when data has clear structure and relationships and you want
the DB to enforce them (ACID, constraints). NoSQL trades those for flexibility
or scale — covered in Phase 4.

## Why it matters in Nexus
`users` and `documents` are relations with a FK and cascade; the schema is the
backbone the API and repositories build on.

## References
- [databases_orchestrator.md](databases_orchestrator.md) · `[[databases_orchestrator]]`
- [normalization.md](normalization.md) · `[[normalization]]`
