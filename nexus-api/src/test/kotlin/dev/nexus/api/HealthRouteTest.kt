package dev.nexus.api

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class HealthRouteTest {
    @Test
    fun `health endpoint returns 200 with ok status`() = testApplication {
        environment { config = testConfig() }
        application { module() }
        val response = client.get("/v1/health")
        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "\"status\":\"ok\"")
    }
}
