package dev.nexus.api.domain.user

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.OffsetDateTime
import java.util.UUID

data class User(
    val id: UUID,
    val email: String,
    val displayName: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

data class CreateUserRequest(
    val email: String,
    val displayName: String,
)

class UserRepository {

    fun create(request: CreateUserRequest): User = transaction {
        val now = OffsetDateTime.now()
        val id = UserTable.insert {
            it[email]       = request.email
            it[displayName] = request.displayName
            it[createdAt]   = now
            it[updatedAt]   = now
        } get UserTable.id

        findById(id) ?: error("Insert succeeded but user not found: $id")
    }

    fun findById(id: UUID): User? = transaction {
        UserTable
            .selectAll()
            .where { UserTable.id eq id }
            .singleOrNull()
            ?.toUser()
    }

    fun findByEmail(email: String): User? = transaction {
        UserTable
            .selectAll()
            .where { UserTable.email eq email }
            .singleOrNull()
            ?.toUser()
    }

    fun existsByEmail(email: String): Boolean = transaction {
        UserTable
            .selectAll()
            .where { UserTable.email eq email }
            .count() > 0
    }

    private fun ResultRow.toUser() = User(
        id          = this[UserTable.id],
        email       = this[UserTable.email],
        displayName = this[UserTable.displayName],
        createdAt   = this[UserTable.createdAt],
        updatedAt   = this[UserTable.updatedAt],
    )
}
