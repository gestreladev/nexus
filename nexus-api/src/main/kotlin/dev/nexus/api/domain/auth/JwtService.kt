package dev.nexus.api.domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import java.util.Date
import java.util.UUID

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val expiresInMinutes: Long,
) {
    companion object {
        /** Secret from env (prod) with a yaml dev default — never hardcoded in code. */
        fun from(config: ApplicationConfig) = JwtConfig(
            secret = System.getenv("JWT_SECRET")
                ?: config.property("nexus.jwt.secret").getString(),
            issuer = config.property("nexus.jwt.issuer").getString(),
            audience = config.property("nexus.jwt.audience").getString(),
            realm = config.property("nexus.jwt.realm").getString(),
            expiresInMinutes = config.property("nexus.jwt.expiresInMinutes").getString().toLong(),
        )
    }
}

/** Signs and verifies HS256 JWTs. `sub` = user id; `email` is a convenience claim. */
class JwtService(private val cfg: JwtConfig) {

    val realm: String get() = cfg.realm

    fun sign(userId: UUID, email: String): String =
        JWT.create()
            .withIssuer(cfg.issuer)
            .withAudience(cfg.audience)
            .withSubject(userId.toString())
            .withClaim("email", email)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + cfg.expiresInMinutes * 60_000))
            .sign(Algorithm.HMAC256(cfg.secret))

    val verifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(cfg.secret))
        .withIssuer(cfg.issuer)
        .withAudience(cfg.audience)
        .build()
}
