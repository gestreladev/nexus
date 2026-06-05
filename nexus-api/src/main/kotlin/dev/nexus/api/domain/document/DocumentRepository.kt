package dev.nexus.api.domain.document

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.OffsetDateTime
import java.util.UUID

enum class DocumentStatus { pending, processing, ready, failed }

data class Document(
    val id: UUID,
    val userId: UUID,
    val title: String,
    val status: DocumentStatus,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)

data class CreateDocumentRequest(
    val userId: UUID,
    val title: String,
)

class DocumentRepository {

    fun create(request: CreateDocumentRequest): Document = transaction {
        val now = OffsetDateTime.now()
        val id = DocumentTable.insert {
            it[userId]    = request.userId
            it[title]     = request.title
            it[status]    = DocumentStatus.pending.name
            it[createdAt] = now
            it[updatedAt] = now
        } get DocumentTable.id

        findById(id) ?: error("Insert succeeded but document not found: $id")
    }

    fun findById(id: UUID): Document? = transaction {
        DocumentTable
            .selectAll()
            .where { DocumentTable.id eq id }
            .singleOrNull()
            ?.toDocument()
    }

    fun findByUserId(userId: UUID): List<Document> = transaction {
        DocumentTable
            .selectAll()
            .where { DocumentTable.userId eq userId }
            .orderBy(DocumentTable.createdAt, SortOrder.DESC)
            .map { it.toDocument() }
    }

    private fun ResultRow.toDocument() = Document(
        id        = this[DocumentTable.id],
        userId    = this[DocumentTable.userId],
        title     = this[DocumentTable.title],
        status    = DocumentStatus.valueOf(this[DocumentTable.status]),
        createdAt = this[DocumentTable.createdAt],
        updatedAt = this[DocumentTable.updatedAt],
    )
}
