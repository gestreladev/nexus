---
name: exposed_dsl
description: Exposed DSL idioms in nexus-api — tables, queries, mappers, transactions.
agent:
  role: database-specialist
  tier: standard
  weight: medium
  triggers:
    - writing Exposed tables or queries
    - mapping ResultRow to a domain type
    - deciding transaction boundaries
metadata:
  type: reference
---

# Exposed DSL — Nexus Patterns

## Table definition

```kotlin
object UserTable : Table("users") {
    val id          = uuid("id").autoGenerate()
    val email       = varchar("email", 255)
    val displayName = varchar("display_name", 255)
    val createdAt   = timestampWithTimeZone("created_at")
    val updatedAt   = timestampWithTimeZone("updated_at")
    override val primaryKey = PrimaryKey(id)
}
```

Column names match SQL (`display_name`); Kotlin properties are `camelCase`.

## Foreign key
```kotlin
val userId = uuid("user_id").references(UserTable.id)
```

## Insert — get generated key back
```kotlin
val id = UserTable.insert {
    it[email]     = request.email
    it[createdAt] = OffsetDateTime.now()
} get UserTable.id
```

## Select
```kotlin
// Single row — nullable
UserTable.selectAll().where { UserTable.email eq email }.singleOrNull()?.toUser()

// Multiple rows
DocumentTable.selectAll()
    .where { DocumentTable.userId eq userId }
    .orderBy(DocumentTable.createdAt, SortOrder.DESC)
    .map { it.toDocument() }

// Existence — cheaper than fetching the row
UserTable.selectAll().where { UserTable.email eq email }.count() > 0
```

## ResultRow mapper — always private
```kotlin
private fun ResultRow.toUser() = User(
    id          = this[UserTable.id],
    email       = this[UserTable.email],
    displayName = this[UserTable.displayName],
)
```

Private to the repository; domain types never leak Exposed internals.

## Transactions
```kotlin
fun create(request: CreateUserRequest): User = transaction {
    val id = UserTable.insert { … } get UserTable.id
    DocumentTable.insert { … }   // both commit or both roll back
    findById(id) ?: error("not found after insert")
}
```

`transaction { }` is Exposed's unit of work. Repositories own that boundary.

## Update / Delete
```kotlin
UserTable.update({ UserTable.id eq id }) { it[displayName] = newName }
UserTable.deleteWhere { UserTable.id eq id }
```

## RETURNING (Postgres) — fewer round trips
On PostgreSQL, `insertReturning` / `updateReturning` / `deleteReturning` get
modified columns back in one statement — no follow-up `findById`:
```kotlin
val created = UserTable.insertReturning {
    it[email] = request.email
} .single()                       // a ResultRow with the new row's columns
```
For `IntIdTable`, `insertAndGetId { }` returns the generated id directly.

> **Version note (Context7, verified):** the `selectAll().where { }` form here is
> current (the 0.46.0 DSL refactor). Nexus pins **Exposed 0.61.0**; the library
> has since reached **1.x** — a future `chore(deps)` upgrade, validated before
> bumping.

## What NOT to do
```kotlin
fun findUser(id: UUID): ResultRow?            // ❌ returning ResultRow
post("/users") { transaction { repo.create() } }  // ❌ transaction in route
post("/users") { UserTable.selectAll()... }   // ❌ SQL in route
```

## References
- [idioms_orchestrator.md](idioms_orchestrator.md) · `[[idioms_orchestrator]]`
- [repository.md](../architecture/repository.md) · `[[repository]]`
