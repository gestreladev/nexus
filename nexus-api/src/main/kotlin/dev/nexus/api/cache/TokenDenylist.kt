package dev.nexus.api.cache

import java.time.Instant

/**
 * Redis-backed JWT revocation. Stores only revoked, not-yet-expired token ids,
 * each with a TTL equal to the token's remaining life — so the set stays tiny
 * and self-cleans. Closes the stateless-token revocation gap (Lesson 4 E5).
 */
class TokenDenylist(private val cache: Cache) {

    private fun key(jti: String) = "denylist:jwt:$jti"

    /** Revoke a token until it would have expired anyway. */
    fun revoke(jti: String, expiresAt: Instant) {
        val ttl = expiresAt.epochSecond - Instant.now().epochSecond
        if (ttl > 0) cache.set(key(jti), "1", ttl)
    }

    fun isRevoked(jti: String): Boolean = cache.exists(key(jti))
}
