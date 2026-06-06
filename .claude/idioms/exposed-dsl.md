# Exposed DSL — Nexus Patterns

## Table definition

```kotlin
object UserTable : Table("users") {        // object = singleton, table name explicit
    val id          = uuid("id").autoGenerate()
    val email       = varchar("email", 255)
    val displayName = varchar("display_name", 255)
    val createdAt   = timestampWithTimeZone("created_at")
    val updatedAt   = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(id)
}
```

Column names match SQL (`display_name`). Kotlin property names are `camelCase`.
`autoGenerate()` on UUID means Exposed generates the value if not provided.

## Foreign key

```kotlin
val userId = uuid("user_id").references(UserTable.id)
```

## Insert — get generated key back

```kotlin
val id = UserTable.insert {
    it[email]       = request.email
    it[displayName] = request.displayName
    it[createdAt]   = OffsetDateTime.now()
    it[updatedAt]   = OffsetDateTime.now()
} get UserTable.id    // ← retrieves the generated UUID
```

## Select

```kotlin
// Single row — nullable
UserTable.selectAll()
    .where { UserTable.email eq email }
    .singleOrNull()
    ?.toUser()

// Multiple rows
DocumentTable.selectAll()
    .where { DocumentTable.userId eq userId }
    .orderBy(DocumentTable.createdAt, SortOrder.DESC)
    .map { it.toDocument() }

// Existence check — cheaper than fetching the row
UserTable.selectAll()
    .where { UserTable.email eq email }
    .count() > 0
```

## ResultRow mapper — always private

```kotlin
private fun ResultRow.toUser() = User(
    id          = this[UserTable.id],
    email       = this[UserTable.email],
    displayName = this[UserTable.displayName],
    createdAt   = this[UserTable.createdAt],
    updatedAt   = this[UserTable.updatedAt],
)
```

Extension on `ResultRow`, private to the repository. Domain types never
leak Exposed internals. If a column is renamed, only this function changes.

## Transactions

```kotlin
// Every public repository method wraps in transaction {}
fun create(request: CreateUserRequest): User = transaction {
    // multiple DB operations here are atomic
    val id = UserTable.insert { … } get UserTable.id
    DocumentTable.insert { … }   // both commit or both roll back
    findById(id) ?: error("not found after insert")
}
```

`transaction { }` is Exposed's unit of work. Never let a route or service
open a transaction — repositories own that boundary.

## Update

```kotlin
UserTable.update({ UserTable.id eq id }) {
    it[displayName] = newName
    it[updatedAt]   = OffsetDateTime.now()
}
```

## Delete

```kotlin
UserTable.deleteWhere { UserTable.id eq id }
```

## What NOT to do

```kotlin
// ❌ Returning ResultRow from a repository
fun findUser(id: UUID): ResultRow? { … }

// ❌ Opening transaction in a route
post("/users") {
    transaction { userRepository.create(…) }   // transaction belongs in repo
}

// ❌ Putting query logic in a route
post("/users") {
    UserTable.selectAll().where { … }           // SQL belongs in repo
}
```
