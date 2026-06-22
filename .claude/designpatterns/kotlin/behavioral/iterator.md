---
name: iterator
description: Iterator pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - sequential access over a collection without exposing internals
    - streaming large or paged results lazily
metadata:
  type: pattern
---

# Iterator

## Intent
Provide sequential access to elements of an aggregate without exposing its
underlying representation.

## Structure
```
Iterable.iterator() ──► Iterator.hasNext() / next()
```

## Kotlin example
```kotlin
// Implement operator iterator() — works with for-loops directly
class PageStream(private val fetch: (Int) -> List<String>) : Iterable<String> {
    override fun iterator() = iterator {
        var page = 0
        while (true) {
            val batch = fetch(page++)
            if (batch.isEmpty()) break
            yieldAll(batch)         // lazy — fetches pages on demand
        }
    }
}

for (item in PageStream(::fetchPage)) { /* … */ }
```

## When to use
- Hide traversal logic; expose elements uniformly.
- Stream large/remote data lazily without materializing it all.

## Kotlin idiom
Implement `operator fun iterator()`, or use the `iterator { yield(...) }`
coroutine builder / `Sequence` for lazy pull-based streams. Rarely hand-written
otherwise — the stdlib gives you iteration for free.

## In Nexus
Paged DB/search results are exposed as a lazy `Sequence`/`Iterable` so callers
loop without loading every page up front.

## References
- [behavioral_orchestrator.md](behavioral_orchestrator.md) · `[[behavioral_orchestrator]]`
