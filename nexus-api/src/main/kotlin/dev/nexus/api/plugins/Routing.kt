package dev.nexus.api.plugins

import dev.nexus.api.routes.v1.healthRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/v1") {
            healthRoutes()
        }
    }
}
