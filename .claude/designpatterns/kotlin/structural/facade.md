---
name: facade
description: Facade pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - simplifying access to a complex subsystem
    - hiding multi-step orchestration behind one call
metadata:
  type: pattern
---

# Facade

## Intent
Provide a unified, simplified interface to a set of interfaces in a subsystem.

## Structure
```
Client ──► Facade ──► (subsystemA, subsystemB, subsystemC)
```

## Kotlin example
```kotlin
class IngestionFacade(
    private val parser: Parser,
    private val chunker: Chunker,
    private val embedder: Embedder,
    private val store: VectorStore,
) {
    fun ingest(raw: String, contentType: String) {        // one simple call
        val doc = parser.parse(raw)
        val chunks = chunker.split(doc)
        chunks.forEach { store.upsert(it.id, embedder.embed(it.text)) }
    }
}
```

## When to use
- A workflow spans several subsystems and clients shouldn't orchestrate them.
- You want a stable entry point while internals change.

## Kotlin idiom
A plain class (or a single top-level function) that composes injected
collaborators. Keep the facade thin — orchestration only, no business rules.

## In Nexus
`nexus-ingest` exposes an `IngestionFacade` so callers trigger
parse→chunk→embed→store with one method.

## References
- [structural_orchestrator.md](structural_orchestrator.md) · `[[structural_orchestrator]]`
- [adapter.md](adapter.md) · `[[adapter]]`
