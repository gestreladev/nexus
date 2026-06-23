package dev.nexus.api.cache

import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit test with a fake — no Docker. TokenDenylist depends on Cache, so we
 * inject a FakeCache and assert behaviour + the self-cleaning TTL math.
 */
class TokenDenylistTest {

    @Test
    fun `revoke then isRevoked is true`() {
        val denylist = TokenDenylist(FakeCache())
        val jti = "token-abc"
        assertFalse(denylist.isRevoked(jti))
        denylist.revoke(jti, Instant.now().plusSeconds(600))
        assertTrue(denylist.isRevoked(jti))
    }

    @Test
    fun `revoke sets a TTL bounded by the token's remaining life`() {
        val cache = FakeCache()
        TokenDenylist(cache).revoke("jti-1", Instant.now().plusSeconds(300))
        val ttl = cache.ttls["denylist:jwt:jti-1"]
        assertTrue(ttl != null && ttl in 1..300, "ttl should be (0, 300], was $ttl")
    }

    @Test
    fun `an already-expired token is not stored`() {
        val cache = FakeCache()
        TokenDenylist(cache).revoke("stale", Instant.now().minusSeconds(10))
        assertFalse(cache.exists("denylist:jwt:stale"))   // ttl <= 0 → no write
    }
}
