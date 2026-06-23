package dev.nexus.api.messaging

import io.ktor.server.config.*
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.util.Properties

data class KafkaConfig(val bootstrapServers: String, val documentTopic: String) {
    companion object {
        fun from(config: ApplicationConfig) = KafkaConfig(
            bootstrapServers = System.getenv("KAFKA_BOOTSTRAP_SERVERS")
                ?: config.property("nexus.kafka.bootstrapServers").getString(),
            documentTopic = config.property("nexus.kafka.documentTopic").getString(),
        )
    }
}

/** One KafkaProducer per process. Creation is non-blocking; it connects lazily. */
object KafkaFactory {
    private val log = LoggerFactory.getLogger(KafkaFactory::class.java)

    lateinit var producer: KafkaProducer<String, String>
        private set

    fun init(config: KafkaConfig) {
        val props = Properties().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers)
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            put(ProducerConfig.ACKS_CONFIG, "all")              // durability — wait for all replicas
            put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true) // no duplicate appends on retry
        }
        producer = KafkaProducer(props)
        log.info("Kafka producer ready — ${config.bootstrapServers}")
    }
}
