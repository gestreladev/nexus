package dev.nexus.api.routes.v1

import dev.nexus.api.domain.document.CreateDocumentRequest
import dev.nexus.api.domain.document.Document
import dev.nexus.api.domain.document.DocumentRepository
import dev.nexus.api.messaging.DocumentEventPublisher
import dev.nexus.api.plugins.AUTH_JWT
import dev.nexus.api.plugins.ErrorResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CreateDocumentBody(val title: String)

@Serializable
data class DocumentResponse(
    val id: String,
    val title: String,
    val status: String,
    val createdAt: String,
)

private fun Document.toResponse() =
    DocumentResponse(id.toString(), title, status.name, createdAt.toString())

fun Route.documentRoutes(
    documents: DocumentRepository,
    publisher: DocumentEventPublisher,
) {
    authenticate(AUTH_JWT) {
        route("/documents") {

            // Create a document (status=pending), publish the event, return 201 fast.
            post {
                val userId = UUID.fromString(call.principal<JWTPrincipal>()!!.subject)
                val body = call.receive<CreateDocumentBody>()
                require(body.title.isNotBlank()) { "Title is required" }

                val doc = documents.create(CreateDocumentRequest(userId, body.title))
                publisher.publishUploaded(doc.id, doc.userId, doc.title)   // async ingest trigger
                call.respond(HttpStatusCode.Created, doc.toResponse())
            }

            // Read a document — owner-only (403 otherwise). The client polls this for status.
            get("/{id}") {
                val userId = UUID.fromString(call.principal<JWTPrincipal>()!!.subject)
                val id = UUID.fromString(call.parameters["id"])
                val doc = documents.findById(id)
                when {
                    doc == null -> call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse("DOCUMENT_NOT_FOUND", "Document '$id' does not exist"),
                    )
                    doc.userId != userId -> call.respond(
                        HttpStatusCode.Forbidden,
                        ErrorResponse("FORBIDDEN", "Not your document"),
                    )
                    else -> call.respond(HttpStatusCode.OK, doc.toResponse())
                }
            }
        }
    }
}
