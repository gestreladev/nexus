package dev.nexus.api.domain.auth

import dev.nexus.api.domain.user.User
import dev.nexus.api.domain.user.UserRepository

/** Thrown when an email is already registered → 409. */
class ConflictException(message: String) : RuntimeException(message)

/** Thrown on failed authentication → 401. Message is intentionally generic. */
class UnauthorizedException(message: String = "Invalid credentials") : RuntimeException(message)

class AuthService(
    private val users: UserRepository,
    private val jwt: JwtService,
) {
    fun register(req: RegisterRequest): User {
        if (users.existsByEmail(req.email)) throw ConflictException("Email already registered")
        val hash = PasswordHasher.hash(req.password)
        return users.create(req.email, req.displayName, hash)
    }

    /**
     * Returns a signed JWT on success. Uses the SAME generic failure for both
     * "no such user" and "wrong password" to avoid user enumeration.
     */
    fun login(req: LoginRequest): String {
        val cred = users.findCredentialsByEmail(req.email) ?: throw UnauthorizedException()
        if (!PasswordHasher.verify(req.password, cred.passwordHash)) throw UnauthorizedException()
        return jwt.sign(cred.id, cred.email)
    }
}
