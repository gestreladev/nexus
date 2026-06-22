---
name: compose_and_client
description: Redis in docker-compose and the Lettuce client setup for nexus-api.
agent:
  role: infra-specialist
  tier: standard
  weight: medium
  triggers:
    - adding the Redis service to compose
    - wiring the Lettuce client in Kotlin
metadata:
  type: reference
---

# Redis — Compose & Client

## docker-compose service
Add Redis beside Postgres (verified shape via Context7):
```yaml
  redis:
    image: redis:7-alpine
    container_name: nexus-redis
    command: ["redis-server", "--appendonly", "yes"]   # AOF for denylist durability
    ports: ["6379:6379"]
    volumes: ["nexus-redis-data:/data"]
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 5s
      timeout: 3s
      retries: 10
volumes:
  nexus-redis-data:
```
`nexus-api` then `depends_on: redis: {condition: service_healthy}` (see
[compose.md](../docker/compose.md) · `[[compose]]`). Host address inside the
network is `redis:6379`; from the host it's `localhost:6379`.

## Lettuce client (Kotlin)
Lettuce is thread-safe — one client + one connection multiplexes all commands
(verified via Context7 / redis/lettuce):
```kotlin
val client = RedisClient.create("redis://localhost:6379")   // env-driven in prod
val connection = client.connect()                            // StatefulRedisConnection
val redis = connection.sync()                                // RedisCommands

redis.set("doc:123", json, SetArgs.Builder.ex(300))          // value + TTL
val v: String? = redis.get("doc:123")
redis.del("doc:123")
// on shutdown: connection.close(); client.shutdown()
```
Wire it as a `redis()` stage in `ModulePipeline` (one client per process, like
`DatabaseFactory`).

## Config
`REDIS_URL` from env (dev default `redis://localhost:6379`), mirroring how
`JWT_SECRET`/DB creds are handled.

## References
- [redis_orchestrator.md](redis_orchestrator.md) · `[[redis_orchestrator]]`
- [use_in_nexus.md](use_in_nexus.md) · `[[use_in_nexus]]`
- [compose.md](../docker/compose.md) · `[[compose]]`
