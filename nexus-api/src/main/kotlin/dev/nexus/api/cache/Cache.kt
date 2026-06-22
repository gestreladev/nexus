package dev.nexus.api.cache

/**
 * Minimal cache abstraction. Keeps the rest of the app independent of Redis —
 * routes/services depend on this interface, not Lettuce.
 */
interface Cache {
    fun get(key: String): String?
    fun set(key: String, value: String, ttlSeconds: Long)
    fun delete(key: String)
    fun exists(key: String): Boolean
}
