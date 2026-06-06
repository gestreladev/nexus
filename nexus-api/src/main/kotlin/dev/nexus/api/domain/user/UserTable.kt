package dev.nexus.api.domain.user

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object UserTable : Table("users") {
    val id          = uuid("id").autoGenerate()
    val email       = varchar("email", 255)
    val displayName = varchar("display_name", 255)
    val createdAt   = timestampWithTimeZone("created_at")
    val updatedAt   = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(id)
}
