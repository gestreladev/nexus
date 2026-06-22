---
name: orm_and_connection_pooling
description: ORMs (Exposed) and connection pooling (HikariCP) — what and why.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - querying via an ORM
    - configuring or reasoning about a connection pool
metadata:
  type: reference
---

# ORM & Connection Pooling

## ORM
An ORM maps between database rows and language objects. **Exposed** (Kotlin,
JetBrains) has two modes:
- **DSL** — a type-safe query builder mapping closely to SQL (Nexus uses this).
- **DAO** — active-record; leaks persistence into domain objects (avoided).

```kotlin
UserTable.selectAll().where { UserTable.email eq email }.singleOrNull()
```
You keep control of the SQL shape; Kotlin gives type safety. Implementation
patterns: [exposed_dsl.md](../../services/nexus-api/idioms/exposed_dsl.md) · `[[exposed_dsl]]`.

## Connection pooling
Opening a DB connection is expensive (TCP + auth + session setup, ~20–100ms). A
**pool** opens N connections once and lends them out, so each request skips that
cost — the same idea as HTTP keep-alive
([tcp_and_connections.md](../networking/tcp_and_connections.md) · `[[tcp_and_connections]]`).

**HikariCP** (Nexus's pool) — the fastest JVM JDBC pool. Key settings:
| Setting | Meaning |
|---|---|
| `maximumPoolSize` | max simultaneous connections |
| `minimumIdle` | warm connections kept even when idle |
| `connectionTimeout` | fail fast if none free (backpressure) |

The pool is also a backpressure mechanism: if all connections are busy, new work
waits then fails loudly rather than overwhelming the database.

## Why it matters in Nexus
`DatabaseFactory` (an `object` singleton) builds one HikariCP pool, runs Flyway,
then connects Exposed — wired as the `database()` pipeline stage.

## References
- [databases_orchestrator.md](databases_orchestrator.md) · `[[databases_orchestrator]]`
- [acid_and_transactions.md](acid_and_transactions.md) · `[[acid_and_transactions]]`
