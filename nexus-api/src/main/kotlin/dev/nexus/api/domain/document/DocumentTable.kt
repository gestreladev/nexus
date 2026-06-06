package dev.nexus.api.domain.document

import dev.nexus.api.domain.user.UserTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestampWithTimeZone

object DocumentTable : Table("documents") {
    val id        = uuid("id").autoGenerate()
    val userId    = uuid("user_id").references(UserTable.id)
    val title     = varchar("title", 500)
    val status    = varchar("status", 50).default("pending")
    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")

    override val primaryKey = PrimaryKey(id)
}
