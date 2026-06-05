package dev.nexus.api

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContains

class HealthRouteTest {
    @Test
    fun `health endpoint returns 200 with ok status`() = testApplication {
        environment {
            config = MapApplicationConfig(
                "nexus.version" to "0.1.0",
                "nexus.environment" to "test",
            )
        }
        application { module() }
        val response = client.get("/v1/health")
        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "\"status\":\"ok\"")
    }
}
