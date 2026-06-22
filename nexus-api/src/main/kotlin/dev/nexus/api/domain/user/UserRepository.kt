package dev.nexus.api.domain.user

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.OffsetDateTime
import java.util.UUID

/** Public domain model — NEVER carries the password hash. */
data class User(
    val id: UUID,
    val email: String,
    val displayName: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

/** Internal credentials view — only for login verification; never leaves the domain. */
data class UserCredentials(
    val id: UUID,
    val email: String,
    val passwordHash: String,
)

class UserRepository {

    fun create(email: String, displayName: String, passwordHash: String): User = transaction {
        val now = OffsetDateTime.now()
        val id = UserTable.insert {
            it[UserTable.email]        = email
            it[UserTable.displayName]  = displayName
            it[UserTable.passwordHash] = passwordHash
            it[createdAt]              = now
            it[updatedAt]              = now
        } get UserTable.id

        findById(id) ?: error("Insert succeeded but user not found: $id")
    }

    fun findById(id: UUID): User? = transaction {
        UserTable.selectAll().where { UserTable.id eq id }.singleOrNull()?.toUser()
    }

    fun findByEmail(email: String): User? = transaction {
        UserTable.selectAll().where { UserTable.email eq email }.singleOrNull()?.toUser()
    }

    fun existsByEmail(email: String): Boolean = transaction {
        UserTable.selectAll().where { UserTable.email eq email }.count() > 0
    }

    /** For login only — reads the hash. Returns null if no such user. */
    fun findCredentialsByEmail(email: String): UserCredentials? = transaction {
        UserTable.selectAll().where { UserTable.email eq email }.singleOrNull()?.let {
            UserCredentials(it[UserTable.id], it[UserTable.email], it[UserTable.passwordHash])
        }
    }

    private fun ResultRow.toUser() = User(
        id          = this[UserTable.id],
        email       = this[UserTable.email],
        displayName = this[UserTable.displayName],
        createdAt   = this[UserTable.createdAt],
        updatedAt   = this[UserTable.updatedAt],
    )
}
