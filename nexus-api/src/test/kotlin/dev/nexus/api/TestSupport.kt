package dev.nexus.api

import io.ktor.server.config.*

/**
 * Full config for booting the real module() in tests — including DB + JWT.
 * These are integration tests: they require the Docker PostgreSQL (port 5433)
 * to be running (`docker compose up -d`).
 */
fun testConfig() = MapApplicationConfig(
    "nexus.version" to "0.1.0",
    "nexus.environment" to "test",
    "nexus.db.url" to "jdbc:postgresql://localhost:5433/nexus",
    "nexus.db.user" to "nexus",
    "nexus.db.password" to "nexus",
    "nexus.db.maxPoolSize" to "5",
    "nexus.jwt.secret" to "test-secret",
    "nexus.jwt.issuer" to "nexus-api",
    "nexus.jwt.audience" to "nexus-api",
    "nexus.jwt.realm" to "nexus",
    "nexus.jwt.expiresInMinutes" to "60",
)
