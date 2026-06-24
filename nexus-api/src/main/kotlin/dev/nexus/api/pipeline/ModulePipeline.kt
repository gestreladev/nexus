package dev.nexus.api.pipeline

import dev.nexus.api.cache.CacheConfig
import dev.nexus.api.cache.RedisFactory
import dev.nexus.api.database.DatabaseConfig
import dev.nexus.api.database.DatabaseFactory
import dev.nexus.api.messaging.KafkaConfig
import dev.nexus.api.messaging.KafkaFactory
import dev.nexus.api.plugins.configureRouting
import dev.nexus.api.plugins.configureSecurity
import dev.nexus.api.plugins.configureSerialization
import dev.nexus.api.plugins.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

@DslMarker
annotation class NexusDsl

@NexusDsl
class ModulePipeline internal constructor(private val app: Application) {

    private val configured = mutableSetOf<Stage>()
    private val chain = ArrayDeque<Application.() -> Unit>()

    enum class Stage { LOGGING, SERIALIZATION, STATUS_PAGES, DATABASE, REDIS, KAFKA, AUTHENTICATION, ROUTING }

    fun logging(block: CallLoggingConfig.() -> Unit = {}) =
        register(Stage.LOGGING) {
            install(CallLogging) {
                level = Level.INFO
                filter { call -> call.request.path().startsWith("/v1") }
                block()
            }
        }

    fun serialization() =
        register(Stage.SERIALIZATION) {
            configureSerialization()
        }

    fun statusPages() {
        requireStage(Stage.SERIALIZATION, current = Stage.STATUS_PAGES)
        register(Stage.STATUS_PAGES) {
            configureStatusPages()
        }
    }

    fun database() {
        register(Stage.DATABASE) {
            val config = environment.config
            DatabaseFactory.init(
                DatabaseConfig(
                    url = config.property("nexus.db.url").getString(),
                    user = config.property("nexus.db.user").getString(),
                    password = config.property("nexus.db.password").getString(),
                    maxPoolSize = config.propertyOrNull("nexus.db.maxPoolSize")
                        ?.getString()?.toInt() ?: 10,
                )
            )
        }
    }

    fun redis() {
        register(Stage.REDIS) {
            RedisFactory.init(CacheConfig.from(environment.config))
        }
    }

    fun kafka() {
        register(Stage.KAFKA) {
            KafkaFactory.init(KafkaConfig.from(environment.config))
        }
    }

    fun authentication() {
        // Auth's denylist check reads the shared cache, so Redis must init first.
        requireStage(Stage.REDIS, current = Stage.AUTHENTICATION)
        register(Stage.AUTHENTICATION) {
            configureSecurity()
        }
    }

    fun routing() {
        requireStage(Stage.SERIALIZATION, current = Stage.ROUTING)
        requireStage(Stage.STATUS_PAGES, current = Stage.ROUTING)
        requireStage(Stage.KAFKA, current = Stage.ROUTING)            // document routes publish events
        requireStage(Stage.AUTHENTICATION, current = Stage.ROUTING)
        register(Stage.ROUTING) {
            configureRouting()
        }
    }

    private fun register(stage: Stage, handler: Application.() -> Unit) {
        check(stage !in configured) { "Stage $stage is already registered" }
        configured += stage
        chain.addLast(handler)
    }

    private fun requireStage(required: Stage, current: Stage) =
        check(required in configured) {
            "${current.name} requires ${required.name} to be registered first"
        }

    internal fun build() = chain.forEach { app.apply(it) }
}

fun Application.nexusModule(block: ModulePipeline.() -> Unit) {
    ModulePipeline(this).also(block).build()
}
