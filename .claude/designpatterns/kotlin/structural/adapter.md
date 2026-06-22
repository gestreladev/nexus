---
name: adapter
description: Adapter pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - converting one interface to another
    - wrapping a third-party client to a domain port
metadata:
  type: pattern
---

# Adapter

## Intent
Convert the interface of a class into another interface clients expect. Lets
otherwise-incompatible classes work together.

## Structure
```
Client ──► Target (expected interface)
              ▲
           Adapter ──► Adaptee (existing, incompatible)
```

## Kotlin example
```kotlin
// Domain port the app expects
interface VectorStore { fun upsert(id: String, vec: FloatArray) }

// Third-party client with a different shape
class PgVectorClient { fun write(key: String, data: List<Float>) { /* … */ } }

// Adapter bridges the two
class PgVectorAdapter(private val client: PgVectorClient) : VectorStore {
    override fun upsert(id: String, vec: FloatArray) =
        client.write(id, vec.toList())
}
```

## When to use
- Integrating a third-party/legacy API that doesn't match your domain port.
- Keeping vendor types out of your domain.

## Kotlin idiom
Extension functions can adapt without a wrapper class for simple cases:
`fun PgVectorClient.asVectorStore(): VectorStore = …`.

## In Nexus
External SDKs (LLM client, vector DB) are wrapped behind domain ports so the
domain never imports vendor types — keeps swaps cheap.

## References
- [structural_orchestrator.md](structural_orchestrator.md) · `[[structural_orchestrator]]`
- [facade.md](facade.md) · `[[facade]]`
