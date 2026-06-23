package dev.nexus.api.messaging

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.util.UUID

/** The contract: the JSON event shape both nexus-api and nexus-ingest agree on. */
@Serializable
data class DocumentUploadedEvent(
    val documentId: String,
    val userId: String,
    val title: String,
    val status: String = "pending",
)

class DocumentEventPublisher(
    private val producer: Producer<String, String>,
    private val topic: String,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    /** Keyed by documentId so all events for a document keep partition order. */
    fun publishUploaded(documentId: UUID, userId: UUID, title: String) {
        val event = DocumentUploadedEvent(documentId.toString(), userId.toString(), title)
        val record = ProducerRecord(topic, documentId.toString(), Json.encodeToString(event))
        producer.send(record) { meta, ex ->   // async; ack confirmed in the callback
            if (ex != null) {
                log.error("Failed to publish document.uploaded for $documentId", ex)
            } else {
                log.info("Published document.uploaded $documentId → ${meta.topic()}-${meta.partition()}@${meta.offset()}")
            }
        }
    }
}
