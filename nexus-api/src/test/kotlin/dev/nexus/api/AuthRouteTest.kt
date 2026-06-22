package dev.nexus.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * Integration tests — require the Docker PostgreSQL (port 5433) to be running.
 * Each test uses a unique email so reruns never collide on the unique constraint.
 */
class AuthRouteTest {

    private fun uniqueEmail() = "test-${UUID.randomUUID()}@nexus.dev"

    private suspend fun HttpClient.register(email: String, password: String = "hunter2pass") =
        post("/v1/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"$email","displayName":"Test","password":"$password"}""")
        }

    private suspend fun HttpClient.login(email: String, password: String = "hunter2pass") =
        post("/v1/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"$email","password":"$password"}""")
        }

    private fun tokenOf(body: String) =
        Regex("\"token\"\\s*:\\s*\"([^\"]+)\"").find(body)!!.groupValues[1]

    @Test
    fun `register then login then access protected route`() = testApplication {
        environment { config = testConfig() }
        application { module() }
        val email = uniqueEmail()

        val reg = client.register(email)
        assertEquals(HttpStatusCode.Created, reg.status)
        assertFalse(reg.bodyAsText().contains("password"), "response must never leak password/hash")

        val token = tokenOf(client.login(email).bodyAsText())

        val me = client.get("/v1/auth/me") { header(HttpHeaders.Authorization, "Bearer $token") }
        assertEquals(HttpStatusCode.OK, me.status)
        assertContains(me.bodyAsText(), email)
    }

    @Test
    fun `duplicate email is 409`() = testApplication {
        environment { config = testConfig() }
        application { module() }
        val email = uniqueEmail()
        client.register(email)
        assertEquals(HttpStatusCode.Conflict, client.register(email).status)
    }

    @Test
    fun `wrong password is 401`() = testApplication {
        environment { config = testConfig() }
        application { module() }
        val email = uniqueEmail()
        client.register(email)
        assertEquals(HttpStatusCode.Unauthorized, client.login(email, "wrongpass").status)
    }

    @Test
    fun `protected route without token is 401`() = testApplication {
        environment { config = testConfig() }
        application { module() }
        assertEquals(HttpStatusCode.Unauthorized, client.get("/v1/auth/me").status)
    }

    @Test
    fun `logout revokes the token via denylist`() = testApplication {
        environment { config = testConfig() }
        application { module() }
        val email = uniqueEmail()
        client.register(email)
        val token = tokenOf(client.login(email).bodyAsText())
        val authed: HttpRequestBuilder.() -> Unit = { header(HttpHeaders.Authorization, "Bearer $token") }

        assertEquals(HttpStatusCode.OK, client.get("/v1/auth/me", authed).status)          // works
        assertEquals(HttpStatusCode.NoContent, client.post("/v1/auth/logout", authed).status) // revoke
        assertEquals(HttpStatusCode.Unauthorized, client.get("/v1/auth/me", authed).status)   // now rejected
    }
}
