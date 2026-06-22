package dev.nexus.api.domain.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val displayName: String,
    val password: String,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

/** Returned on successful login — the signed JWT (never a secret/hash). */
@Serializable
data class AuthResponse(
    val token: String,
)

/** Public identity view returned by register and /me — never the password hash. */
@Serializable
data class MeResponse(
    val id: String,
    val email: String,
    val displayName: String,
)
