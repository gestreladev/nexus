package dev.nexus.api.cache

import io.lettuce.core.SetArgs
import io.lettuce.core.api.sync.RedisCommands

/** Lettuce-backed [Cache]. Lettuce commands are thread-safe over one connection. */
class RedisCache(private val commands: RedisCommands<String, String>) : Cache {

    override fun get(key: String): String? = commands.get(key)

    override fun set(key: String, value: String, ttlSeconds: Long) {
        commands.set(key, value, SetArgs.Builder.ex(ttlSeconds))   // SET key val EX ttl
    }

    override fun delete(key: String) {
        commands.del(key)
    }

    override fun exists(key: String): Boolean = (commands.exists(key) ?: 0L) > 0L
}
