---
name: normalization
description: Database normalization 1NF → 3NF and how to spot a violation.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - designing or reviewing a schema for redundancy
    - spotting a normal-form violation
metadata:
  type: reference
---

# Normalization (1NF → 3NF)

Normalization removes redundancy so every fact lives in exactly one place.
The mnemonic encodes all three forms:

> Every non-key column depends on **the key, the whole key, and nothing but the
> key.**

| Form | Rule | Breaks when |
|---|---|---|
| **1NF** | atomic values, no repeating groups | an array/CSV crammed in a column |
| **2NF** | no dependency on *part* of a composite key | non-key depends on half a composite PK |
| **3NF** | no dependency on *another non-key* column | a non-key column describes another non-key column |

## The systems analogy
Normalization = **single source of truth**. A fact lives in one row; everything
else holds a foreign key (a pointer), never a copy. A denormalized column is a
cached copy — and you know the tax on caches: invalidation.

## The durable shortcut
**2NF can only break with a composite primary key.** With a single-column PK
(every Nexus table uses a UUID `id`), 2NF is free, so the only question that ever
bites is **3NF**: does each column describe the row's `id` directly, or does it
secretly describe *another* column?

## Spot-the-violation
```
sessions(id PK, user_id FK, user_email, token, expires_at)
                              ^^^^^^^^^^
id → user_id → user_email   (transitive: user_email depends on user_id, a non-key)
```
Fix: drop `user_email`; it lives in `users`, reachable via `user_id` (JOIN).

## When to denormalize
Deliberately, for read performance, *with* an invalidation plan — never by
accident. Default to 3NF.

## Why it matters in Nexus
`documents` holds only `user_id` (no `user_email`) — 3NF by design (Lesson 3).

## References
- [databases_orchestrator.md](databases_orchestrator.md) · `[[databases_orchestrator]]`
- [relational_model.md](relational_model.md) · `[[relational_model]]`
- [lesson_03_database.md](../../lessons/log/lesson_03_database.md) · `[[lesson_03_database]]`
