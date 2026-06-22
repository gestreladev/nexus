---
name: testing_strategy
description: Test pyramid, testApplication, fakes vs mocks, and naming for nexus-api.
agent:
  role: test-specialist
  tier: standard
  weight: medium
  triggers:
    - writing a route, repository, or unit test
    - choosing between a fake and a mock
metadata:
  type: reference
---

# Testing Strategy

## Pyramid

```
         /\
        /  \  E2E (rare — full Docker stack)
       /────\
      / Intg  \  Integration (DB, Kafka, Redis)
     /──────────\
    /    Unit    \  Unit (pure logic, no I/O)
   /______________\
```

Most coverage at unit level. Integration validates boundaries. E2E sparingly.

## Unit tests — Ktor routes

Use `testApplication {}` — full in-memory Ktor instance, no real port.

```kotlin
class UserRouteTest {
    @Test
    fun `POST users returns 201`() = testApplication {
        environment {
            config = MapApplicationConfig(    // inject config — no application.yaml
                "nexus.version"     to "0.1.0",
                "nexus.environment" to "test",
            )
        }
        application { module() }
        val response = client.post("/v1/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"test@nexus.dev","displayName":"Test"}""")
        }
        assertEquals(HttpStatusCode.Created, response.status)
    }
}
```

### Config injection
Always use `MapApplicationConfig` — never rely on `application.yaml` loading.

### Repository fakes (not mocks)
```kotlin
class FakeUserRepository : UserRepository() {
    private val store = mutableMapOf<UUID, User>()
    override fun create(request: CreateUserRequest): User {
        val user = User(id = UUID.randomUUID(), email = request.email, …)
        store[user.id] = user; return user
    }
    override fun findById(id: UUID): User? = store[id]
}
```

Fakes are deterministic, fast, and reveal contracts better than mocks.

## Integration tests — repositories

Test against real PostgreSQL (the Docker DB, a dedicated test schema).

```kotlin
@Tag("integration")
class UserRepositoryTest {
    @BeforeEach fun setup() { /* connect, migrate */ }
    @AfterEach  fun teardown() { /* truncate */ }

    @Test fun `create then findByEmail returns same user`() {
        val repo = UserRepository()
        val u = repo.create(CreateUserRequest("a@nexus.dev", "A"))
        assertEquals(u, repo.findByEmail("a@nexus.dev"))
    }
}
```

## File naming

| What | Pattern |
|---|---|
| Route test | `<Resource>RouteTest.kt` |
| Repository integration test | `<Resource>RepositoryTest.kt` |
| Pure logic unit test | `<Class>Test.kt` |

## What NOT to test
Ktor internals, Exposed SQL generation, data class getters/setters.

## References
- [testing_orchestrator.md](testing_orchestrator.md) · `[[testing_orchestrator]]`
