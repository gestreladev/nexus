package dev.nexus.api.plugins

import dev.nexus.api.cache.RedisFactory
import dev.nexus.api.cache.TokenDenylist
import dev.nexus.api.domain.auth.JwtConfig
import dev.nexus.api.domain.auth.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

const val AUTH_JWT = "auth-jwt"

fun Application.configureSecurity() {
    val jwt = JwtService(JwtConfig.from(environment.config))
    val denylist = TokenDenylist(RedisFactory.cache)   // redis() stage ran first

    install(Authentication) {
        jwt(AUTH_JWT) {
            realm = jwt.realm
            verifier(jwt.verifier)
            validate { credential ->
                val jti = credential.payload.id
                // Valid signature AND a subject AND not revoked.
                if (credential.payload.subject != null && jti != null && !denylist.isRevoked(jti)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse("UNAUTHORIZED", "Missing, invalid, or revoked authentication token"),
                )
            }
        }
    }
}
