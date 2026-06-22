---
name: nosql
description: NoSQL database families and when to use them vs relational.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - choosing a non-relational store
    - deciding relational vs NoSQL
metadata:
  type: reference
---

# NoSQL Databases

"NoSQL" = non-relational stores that trade some of the relational model's
guarantees (joins, strict schema, ACID) for flexibility, scale, or speed.

## Families
| Family | Model | Examples | Good for |
|---|---|---|---|
| Key-value | key → opaque value | **Redis**, DynamoDB | caches, sessions, denylists |
| Document | key → JSON document | MongoDB, Couchbase | flexible, nested records |
| Wide-column | rows with dynamic columns | Cassandra, ScyllaDB | huge write throughput |
| Graph | nodes + edges | Neo4j | relationship-heavy queries |
| Vector | embeddings + similarity | pgvector, Pinecone | semantic search (Phase 9) |

## Relational vs NoSQL — how to choose
- **Relational** when data has clear structure + relationships and you want the
  DB to enforce integrity (ACID, FKs) — Nexus's `users`/`documents`.
- **NoSQL** when you need a specific super-power: sub-ms KV (Redis), schema
  flexibility (document), or write scale (wide-column).

Most systems are **polyglot**: relational for the system of record, KV for
cache/sessions. Nexus is exactly this — PostgreSQL + Redis.

## Redis as KV NoSQL
Redis is the key-value member used by Nexus for caching and the JWT denylist.
Details: [redis_orchestrator.md](../../infra/redis/redis_orchestrator.md) · `[[redis_orchestrator]]`.

## References
- [databases_orchestrator.md](databases_orchestrator.md) · `[[databases_orchestrator]]`
- [relational_model.md](relational_model.md) · `[[relational_model]]`
- [caching_orchestrator.md](../caching/caching_orchestrator.md) · `[[caching_orchestrator]]`
