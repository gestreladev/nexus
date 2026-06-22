package dev.nexus.api.routes.v1

import dev.nexus.api.domain.auth.AuthResponse
import dev.nexus.api.domain.auth.AuthService
import dev.nexus.api.domain.auth.LoginRequest
import dev.nexus.api.domain.auth.MeResponse
import dev.nexus.api.domain.auth.RegisterRequest
import dev.nexus.api.domain.auth.UnauthorizedException
import dev.nexus.api.domain.user.User
import dev.nexus.api.domain.user.UserRepository
import dev.nexus.api.plugins.AUTH_JWT
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

private fun User.toMeResponse() = MeResponse(id.toString(), email, displayName)

fun Route.authRoutes(auth: AuthService, users: UserRepository) {
    route("/auth") {

        // Public: create an account. Returns identity — never the password hash.
        post("/register") {
            val req = call.receive<RegisterRequest>()
            require(req.email.isNotBlank()) { "Email is required" }
            require(req.displayName.isNotBlank()) { "Display name is required" }
            require(req.password.length >= 8) { "Password must be at least 8 characters" }

            val user = auth.register(req)
            call.respond(HttpStatusCode.Created, user.toMeResponse())
        }

        // Public: verify password (bcrypt) and issue a signed JWT.
        post("/login") {
            val req = call.receive<LoginRequest>()
            val token = auth.login(req)
            call.respond(HttpStatusCode.OK, AuthResponse(token))
        }

        // Protected: validates the JWT only — no DB hit for auth itself.
        authenticate(AUTH_JWT) {
            get("/me") {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = UUID.fromString(principal.subject)
                val user = users.findById(userId) ?: throw UnauthorizedException("User no longer exists")
                call.respond(HttpStatusCode.OK, user.toMeResponse())
            }
        }
    }
}
