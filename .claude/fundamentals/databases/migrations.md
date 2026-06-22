---
name: migrations
description: Versioned, ordered schema change as code.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - changing the database schema
    - reasoning about schema versioning across environments
metadata:
  type: reference
---

# Migrations

Never change a schema by hand. **Migrations** are versioned SQL files applied in
order, once, and tracked in a history table. Benefits:
- schema lives in version control
- any environment (dev/CI/prod) reaches the same state by running migrations
- full, auditable history of how the schema evolved

## Flyway (Nexus's tool)
Files named `V{n}__{description}.sql`, run lowest-first:
```
V1__create_users.sql
V2__create_documents.sql
V3__add_auth_tokens.sql   ← Phase 3
```
Flyway records applied versions in `flyway_schema_history` and refuses to start
if a migration failed — it never silently skips.

## Run order matters
```
fun init(config) {
    buildDataSource()
    runMigrations()      // schema exists after this
    Database.connect()   // ORM safe to query now
}
```
Migrate **before** connecting the ORM, or the first request hits missing tables.

## Forward-only discipline
Prefer additive, forward migrations. To undo, write a new migration rather than
editing a past one (which would break environments that already applied it).

## Why it matters in Nexus
Flyway applies `V1`/`V2` on startup; new schema (auth tokens, etc.) arrives as
new `V{n}` files — reproducible everywhere.

## References
- [databases_orchestrator.md](databases_orchestrator.md) · `[[databases_orchestrator]]`
- [orm_and_connection_pooling.md](orm_and_connection_pooling.md) · `[[orm_and_connection_pooling]]`
