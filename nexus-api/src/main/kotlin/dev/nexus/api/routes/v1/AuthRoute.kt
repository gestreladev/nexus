package dev.nexus.api.routes.v1

import dev.nexus.api.cache.Cache
import dev.nexus.api.cache.TokenDenylist
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

private val json = Json
private fun User.toMeResponse() = MeResponse(id.toString(), email, displayName)

fun Route.authRoutes(
    auth: AuthService,
    users: UserRepository,
    denylist: TokenDenylist,
    cache: Cache,
) {
    route("/auth") {

        post("/register") {
            val req = call.receive<RegisterRequest>()
            require(req.email.isNotBlank()) { "Email is required" }
            require(req.displayName.isNotBlank()) { "Display name is required" }
            require(req.password.length >= 8) { "Password must be at least 8 characters" }
            call.respond(HttpStatusCode.Created, auth.register(req).toMeResponse())
        }

        post("/login") {
            val token = auth.login(call.receive<LoginRequest>())
            call.respond(HttpStatusCode.OK, AuthResponse(token))
        }

        authenticate(AUTH_JWT) {

            // Cache-aside: serve the user's identity from Redis, fall back to DB.
            get("/me") {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = principal.subject!!
                val cacheKey = "cache:user:$userId"

                cache.get(cacheKey)?.let { hit ->
                    call.respondText(hit, ContentType.Application.Json)   // cache hit — no DB
                    return@get
                }
                val user = users.findById(UUID.fromString(userId))
                    ?: throw UnauthorizedException("User no longer exists")
                val body = json.encodeToString(user.toMeResponse())
                cache.set(cacheKey, body, ttlSeconds = 300)               // populate
                call.respondText(body, ContentType.Application.Json, HttpStatusCode.OK)
            }

            // Revoke the current token (logout) — denylisted until it would expire.
            post("/logout") {
                val principal = call.principal<JWTPrincipal>()!!
                val jti = principal.payload.id
                val expiresAt = principal.expiresAt?.toInstant()
                if (jti != null && expiresAt != null) denylist.revoke(jti, expiresAt)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
