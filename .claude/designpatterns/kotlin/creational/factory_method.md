---
name: factory_method
description: Factory Method pattern in idiomatic Kotlin.
agent:
  role: pattern-advisor
  tier: standard
  weight: soft
  triggers:
    - deferring object creation to subclasses
    - choosing a concrete type at runtime
metadata:
  type: pattern
---

# Factory Method

## Intent
Define an interface for creating an object, but let subclasses (or a function)
decide which class to instantiate.

## Structure
```
Creator (declares factoryMethod) ──► Product (interface)
   │                                     ▲
ConcreteCreator.factoryMethod() ───► ConcreteProduct
```

## Kotlin example
```kotlin
interface Parser { fun parse(raw: String): Document }

class PdfParser : Parser { override fun parse(raw: String) = Document(/* … */) }
class TxtParser : Parser { override fun parse(raw: String) = Document(/* … */) }

// Factory method as a function — idiomatic Kotlin, no Creator hierarchy needed
fun parserFor(contentType: String): Parser = when (contentType) {
    "application/pdf" -> PdfParser()
    "text/plain"      -> TxtParser()
    else -> throw IllegalArgumentException("Unsupported: $contentType")
}
```

## When to use
- The exact type isn't known until runtime (e.g. by content type, config).
- You want to centralize and name the creation logic.

## Kotlin idiom
Prefer a top-level factory **function** or a `companion object` factory over a
full Creator class hierarchy. Use `sealed` Product types so `when` is exhaustive.

## In Nexus
`nexus-ingest` will select a parser/embedder by document MIME type — a natural
factory-method seam.

## References
- [creational_orchestrator.md](creational_orchestrator.md) · `[[creational_orchestrator]]`
- [abstract_factory.md](abstract_factory.md) · `[[abstract_factory]]`
