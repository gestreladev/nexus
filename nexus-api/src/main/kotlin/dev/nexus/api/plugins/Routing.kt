package dev.nexus.api.plugins

import dev.nexus.api.cache.RedisFactory
import dev.nexus.api.cache.TokenDenylist
import dev.nexus.api.domain.auth.AuthService
import dev.nexus.api.domain.auth.JwtConfig
import dev.nexus.api.domain.auth.JwtService
import dev.nexus.api.domain.user.UserRepository
import dev.nexus.api.routes.v1.authRoutes
import dev.nexus.api.routes.v1.healthRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val users = UserRepository()
    val jwt = JwtService(JwtConfig.from(environment.config))
    val auth = AuthService(users, jwt)
    val cache = RedisFactory.cache
    val denylist = TokenDenylist(cache)

    routing {
        route("/v1") {
            healthRoutes()
            authRoutes(auth, users, denylist, cache)
        }
    }
}
