---
name: repository
description: Repository pattern (PoEAA) in nexus-api — transaction ownership and mapper rules.
agent:
  role: database-specialist
  tier: standard
  weight: medium
  triggers:
    - writing or reviewing a repository
    - deciding transaction boundaries
    - isolating the domain from data access
metadata:
  type: pattern
---

# Repository Pattern

> Not a GoF pattern — this is a PoEAA / DDD architectural pattern, so it lives
> with nexus-api architecture rather than in the GoF tree.

## Intent

Isolate the domain layer from data access details. The domain works with plain
data classes; the repository is the only place that knows SQL exists.

## Structure in Nexus

```
Route
  │  calls
  ▼
Repository          ← the contract (a class here)
  │  uses
  ▼
Exposed DSL + Table ← SQL details, invisible to callers
  │  runs on
  ▼
HikariCP / PostgreSQL
```

## Rules

1. **Repositories own transactions.** Every public method wraps its operations
   in `transaction { }`. Callers never open transactions.
2. **Domain models cross the boundary, not ResultRows.** A private
   `ResultRow.toX()` extension converts rows inside the repository.
3. **No business logic in repositories.** "Give me user by email" — not
   "should this user be allowed to do X".
4. **One repository per aggregate root.** `UserRepository` owns `users`; they
   do not call each other.

## Nexus implementation pattern

```kotlin
class UserRepository {
    fun create(request: CreateUserRequest): User = transaction {
        val id = UserTable.insert { ... } get UserTable.id
        findById(id) ?: error("Insert succeeded but row not found: $id")
    }

    fun findByEmail(email: String): User? = transaction {
        UserTable.selectAll()
            .where { UserTable.email eq email }
            .singleOrNull()
            ?.toUser()           // private extension — ResultRow stays here
    }

    private fun ResultRow.toUser() = User(
        id    = this[UserTable.id],
        email = this[UserTable.email],
    )
}
```

## When a route calls a repository

```kotlin
post("/users") {
    val body = call.receive<CreateUserRequest>()
    val user = userRepository.create(body)     // repository handles transaction
    call.respond(HttpStatusCode.Created, user)
}
```

The route knows nothing about SQL, transactions, or Exposed.

## Adding a new repository
1. Create `domain/<resource>/<Resource>Table.kt` — Exposed table object.
2. Create `domain/<resource>/<Resource>Repository.kt` — queries + mapper.
3. Wire it via constructor/DI in `plugins/Routing.kt`.

## References
- [architecture_orchestrator.md](architecture_orchestrator.md) · `[[architecture_orchestrator]]`
- [layers.md](layers.md) · `[[layers]]`
- [exposed_dsl.md](../idioms/exposed_dsl.md) · `[[exposed_dsl]]`
