package dev.nexus.api.domain.auth

import at.favre.lib.crypto.bcrypt.BCrypt

/**
 * bcrypt password hashing. The cost (work factor) is tunable — raise it as
 * hardware improves. Cost 12 ≈ ~250ms per hash: imperceptible at login, brutal
 * for an attacker. Salt + cost are embedded in the 60-char output.
 */
object PasswordHasher {
    private const val COST = 12

    fun hash(raw: String): String =
        BCrypt.withDefaults().hashToString(COST, raw.toCharArray())

    fun verify(raw: String, storedHash: String): Boolean =
        BCrypt.verifyer().verify(raw.toCharArray(), storedHash).verified
}
