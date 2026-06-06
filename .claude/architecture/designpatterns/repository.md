# Repository Pattern

## Intent

Isolate the domain layer from data access details. The domain works with
plain data classes; the repository is the only place that knows SQL exists.

## Structure in Nexus

```
Route
  │  calls
  ▼
Repository          ← the contract (could be an interface; here a class)
  │  uses
  ▼
Exposed DSL + Table ← SQL details, invisible to callers
  │  runs on
  ▼
HikariCP / PostgreSQL
```

## Rules

1. **Repositories own transactions.** Every public method that writes must
   wrap its operations in `transaction { }`. Callers never open transactions.

2. **Domain models cross the boundary, not ResultRows.** The private
   `ResultRow.toX()` extension converts DB rows to domain types inside
   the repository. Nothing outside sees `ResultRow`.

3. **No business logic in repositories.** A repository answers "give me
   user by email" — it never decides "should this user be allowed to do X".

4. **One repository per aggregate root.** `UserRepository` owns `users`.
   `DocumentRepository` owns `documents`. They do not call each other.

## Nexus implementation pattern

```kotlin
class UserRepository {

    // Write: wraps in transaction, returns domain type
    fun create(request: CreateUserRequest): User = transaction {
        val id = UserTable.insert { ... } get UserTable.id
        findById(id) ?: error("Insert succeeded but row not found: $id")
    }

    // Read: transaction for consistency, returns nullable domain type
    fun findByEmail(email: String): User? = transaction {
        UserTable.selectAll()
            .where { UserTable.email eq email }
            .singleOrNull()
            ?.toUser()           // private extension — ResultRow stays here
    }

    // Private: only the repository knows the column mapping
    private fun ResultRow.toUser() = User(
        id    = this[UserTable.id],
        email = this[UserTable.email],
        // …
    )
}
```

## When a route calls a repository

```kotlin
// routes/v1/UserRoute.kt
post("/users") {
    val body = call.receive<CreateUserRequest>()
    val user = userRepository.create(body)     // repository handles transaction
    call.respond(HttpStatusCode.Created, user)
}
```

The route knows nothing about SQL, transactions, or Exposed. If the
database changes, only the repository changes.

## Adding a new repository

1. Create `domain/<resource>/<Resource>Table.kt` — Exposed table object
2. Create `domain/<resource>/<Resource>Repository.kt` — queries + mapper
3. Instantiate and pass via constructor/DI in `plugins/Routing.kt`
4. Do not add the repository as a singleton unless you have a specific reason
