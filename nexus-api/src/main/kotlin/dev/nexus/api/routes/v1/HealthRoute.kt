package dev.nexus.api.routes.v1

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String,
    val version: String,
    val environment: String,
)

fun Route.healthRoutes() {
    get("/health") {
        val config = call.application.environment.config
        call.respond(
            HttpStatusCode.OK,
            HealthResponse(
                status = "ok",
                version = config.property("nexus.version").getString(),
                environment = config.property("nexus.environment").getString(),
            ),
        )
    }
}
