---
name: strategy
description: Strategy pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - swapping interchangeable algorithms at runtime
    - injecting a policy or behavior
metadata:
  type: pattern
---

# Strategy

## Intent
Define a family of algorithms, encapsulate each, and make them interchangeable.
The algorithm varies independently from the clients that use it.

## Structure
```
Context(strategy: Strategy) ──► strategy.execute(input)
```

## Kotlin example
```kotlin
// A strategy is just a function type in Kotlin
typealias ChunkStrategy = (String) -> List<String>

val byParagraph: ChunkStrategy = { it.split("\n\n") }
val byFixedSize:  ChunkStrategy = { it.chunked(1000) }

class Chunker(private val strategy: ChunkStrategy) {
    fun chunk(text: String) = strategy(text)
}

Chunker(byParagraph).chunk(doc)
```

## When to use
- Multiple ways to do one thing, chosen at runtime or by config.
- You want to avoid `when (algoType)` branching sprinkled through code.

## Kotlin idiom
Prefer a **function type** (`(In) -> Out`) over a Strategy interface for
single-method strategies. Inject it via constructor or pick from a map.

## State vs Strategy
Same structure. Strategy = client picks the algorithm; State = the object
transitions itself between behaviors.

## In Nexus
`nexus-ingest` chunking and `nexus-search` ranking are pluggable strategies
selected per document type / query.

## References
- [behavioral_orchestrator.md](behavioral_orchestrator.md) · `[[behavioral_orchestrator]]`
- [state.md](state.md) · `[[state]]`
