package dev.nexus.api.cache

import io.ktor.server.config.*
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import org.slf4j.LoggerFactory

data class CacheConfig(val url: String) {
    companion object {
        /** REDIS_URL env (prod) with a yaml dev default. */
        fun from(config: ApplicationConfig) = CacheConfig(
            url = System.getenv("REDIS_URL")
                ?: config.property("nexus.redis.url").getString(),
        )
    }
}

/**
 * One Lettuce client + connection per process (like DatabaseFactory). Exposes a
 * single shared [Cache] used by the cache layer and the token denylist.
 */
object RedisFactory {
    private val log = LoggerFactory.getLogger(RedisFactory::class.java)

    private lateinit var client: RedisClient
    private lateinit var connection: StatefulRedisConnection<String, String>

    lateinit var cache: Cache
        private set

    fun init(config: CacheConfig) {
        client = RedisClient.create(config.url)
        connection = client.connect()
        cache = RedisCache(connection.sync())
        log.info("Redis connected — ${config.url}")
    }
}
