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

Most coverage should live at unit level. Integration tests validate
boundaries (DB, external services). E2E tests validate full flows — used
sparingly and only in CI.

## Unit tests — Ktor routes

Use `testApplication {}` from `ktor-server-test-host`. Spins up a full
in-memory Ktor instance — no real port, no real network.

```kotlin
class UserRouteTest {
    @Test
    fun `POST users returns 201 with created user`() = testApplication {
        environment {
            config = MapApplicationConfig(     // inject config — no application.yaml
                "nexus.version"     to "0.1.0",
                "nexus.environment" to "test",
                // DB config omitted — mock the repo instead
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

### Config injection pattern

Always use `MapApplicationConfig` in tests. Never rely on `application.yaml`
loading — it couples tests to file presence and real DB config.

### Repository fakes

For route tests that need DB results, use a fake repository, not a mock:

```kotlin
class FakeUserRepository : UserRepository() {
    private val store = mutableMapOf<UUID, User>()

    override fun create(request: CreateUserRequest): User {
        val user = User(id = UUID.randomUUID(), email = request.email, …)
        store[user.id] = user
        return user
    }

    override fun findById(id: UUID): User? = store[id]
}
```

Fakes are deterministic, fast, and reveal integration contracts better
than mocks with `every { } returns`.

## Integration tests — repositories

Test repositories against a real PostgreSQL instance. Use the same
Docker Compose database, but a dedicated test schema or database.

```kotlin
// These run slower — mark with a tag so they can be excluded locally
@Tag("integration")
class UserRepositoryTest {
    @BeforeEach fun setup() { /* connect to test DB, run migrations */ }
    @AfterEach  fun teardown() { /* truncate tables */ }

    @Test
    fun `create then findByEmail returns same user`() {
        val repo = UserRepository()
        val user = repo.create(CreateUserRequest("a@nexus.dev", "A"))
        assertEquals(user, repo.findByEmail("a@nexus.dev"))
    }
}
```

## File naming

| What | Pattern |
|---|---|
| Route test | `<Resource>RouteTest.kt` |
| Repository integration test | `<Resource>RepositoryTest.kt` |
| Pure logic unit test | `<Class>Test.kt` |

All under `src/test/kotlin/dev/nexus/api/` mirroring the main source tree.

## What NOT to test

- Ktor internals (serialization, routing mechanics)
- Exposed SQL generation
- Data class getters/setters
