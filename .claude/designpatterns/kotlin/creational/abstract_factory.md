---
name: abstract_factory
description: Abstract Factory pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - creating families of related objects
    - swapping a whole product family by config
metadata:
  type: pattern
---

# Abstract Factory

## Intent
Provide an interface for creating **families** of related objects without
specifying their concrete classes.

## Structure
```
AbstractFactory ──► createA(): ProductA
                └─► createB(): ProductB
ConcreteFactory1 → ProductA1, ProductB1
ConcreteFactory2 → ProductA2, ProductB2
```

## Kotlin example
```kotlin
interface Embedder { fun embed(text: String): FloatArray }
interface VectorStore { fun upsert(id: String, vec: FloatArray) }

interface AiStack {                 // the abstract factory
    fun embedder(): Embedder
    fun vectorStore(): VectorStore
}

class OpenAiStack : AiStack {
    override fun embedder() = OpenAiEmbedder()
    override fun vectorStore() = PgVectorStore()
}
class LocalStack : AiStack {
    override fun embedder() = LocalEmbedder()
    override fun vectorStore() = PgVectorStore()
}
```

## When to use
- Several products must be used together and stay consistent (same family).
- You want to switch the entire family via one configuration point.

## Kotlin idiom
An `interface` with factory methods, or a function returning a small holder.
Wire the chosen factory once at startup (DI / pipeline stage).

## In Nexus
The RAG layer (v0.11.0) may swap an "AI stack" (embedder + vector store +
LLM client) between hosted and local implementations.

## References
- [creational_orchestrator.md](creational_orchestrator.md) · `[[creational_orchestrator]]`
- [factory_method.md](factory_method.md) · `[[factory_method]]`
