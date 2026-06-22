---
name: coroutines
description: Kotlin coroutines — suspend, structured concurrency, Flow.
agent:
  role: language-expert
  tier: standard
  weight: medium
  triggers:
    - writing asynchronous/concurrent code
    - reasoning about suspend functions and scopes
metadata:
  type: reference
---

# Coroutines

Coroutines are lightweight alternatives to threads: a `suspend` function can
pause without blocking an OS thread, so a server handles many concurrent
requests on few threads. Most APIs live in `kotlinx.coroutines`.

```kotlin
suspend fun fetchUser(id: UUID): User { ... }   // can suspend, not block
```

## Structured concurrency
Coroutines launched in a scope are tied to it: the scope won't complete until its
children do, and cancelling the scope cancels them. This prevents leaks.

```kotlin
suspend fun loadDashboard(): Dashboard = coroutineScope {
    val docs = async { repo.recentDocuments() }   // run concurrently
    val stats = async { repo.stats() }
    Dashboard(docs.await(), stats.await())          // both joined here
}
```

- `coroutineScope { }` — waits for all children; cancels all if one fails.
- `launch` — fire-and-forget within a scope (returns `Job`).
- `async`/`await` — concurrent value-producing work (returns `Deferred<T>`).
- Dispatchers: `Dispatchers.IO` for blocking I/O, `Default` for CPU work.

## Flow (async streams)
`Flow<T>` is the cold, backpressure-aware async stream — the idiomatic Observer
for event pipelines (e.g. document status updates).

## In Nexus
Ktor handlers are `suspend` — the server is non-blocking end to end. Calls to
other services / the DB should suspend (or run on `Dispatchers.IO`) so threads
aren't blocked. This becomes central in the async pipeline (Phase 7) and RAG
streaming (Phase 11).

## References
- [kotlin_language_orchestrator.md](kotlin_language_orchestrator.md) · `[[kotlin_language_orchestrator]]`
- [observer.md](../../designpatterns/kotlin/behavioral/observer.md) · `[[observer]]`
