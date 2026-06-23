package dev.nexus.api.cache

/**
 * In-memory [Cache] for unit tests — no Redis, no Docker. This is the payoff of
 * making Cache an interface: anything depending on it can be tested with a fake.
 * Records TTLs so tests can assert expiry behaviour.
 */
class FakeCache : Cache {
    val store = mutableMapOf<String, String>()
    val ttls = mutableMapOf<String, Long>()

    override fun get(key: String): String? = store[key]

    override fun set(key: String, value: String, ttlSeconds: Long) {
        store[key] = value
        ttls[key] = ttlSeconds
    }

    override fun delete(key: String) {
        store.remove(key)
        ttls.remove(key)
    }

    override fun exists(key: String): Boolean = store.containsKey(key)
}
