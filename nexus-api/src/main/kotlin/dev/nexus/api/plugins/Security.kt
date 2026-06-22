package dev.nexus.api.plugins

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

    install(Authentication) {
        jwt(AUTH_JWT) {
            realm = jwt.realm
            verifier(jwt.verifier)
            // Token is valid only if it carries a subject (the user id).
            validate { credential ->
                if (credential.payload.subject != null) JWTPrincipal(credential.payload) else null
            }
            // Missing/invalid token → our standard error shape, not Ktor's default.
            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ErrorResponse("UNAUTHORIZED", "Missing or invalid authentication token"),
                )
            }
        }
    }
}
