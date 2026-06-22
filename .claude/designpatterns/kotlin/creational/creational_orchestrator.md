---
name: creational-orchestrator
description: Routing for Kotlin creational GoF patterns.
agent:
  role: pattern-router
  tier: nano
  weight: soft
  triggers:
    - selecting a creational pattern
metadata:
  type: orchestrator
---

# Creational Patterns — Kotlin

Patterns that abstract the instantiation process.

---

## Routing Table

| Pattern | Intent | Document |
|---|---|---|
| Factory Method | Defer instantiation to subclasses | [factory_method.md](factory_method.md) · `[[factory_method]]` |
| Abstract Factory | Families of related objects | [abstract_factory.md](abstract_factory.md) · `[[abstract_factory]]` |
| Builder | Step-by-step construction of complex objects | [builder.md](builder.md) · `[[builder]]` |
| Prototype | Clone existing objects | [prototype.md](prototype.md) · `[[prototype]]` |
| Singleton | One instance, global access | [singleton.md](singleton.md) · `[[singleton]]` |

---

## References
- [kotlin_orchestrator.md](../kotlin_orchestrator.md) · `[[kotlin_orchestrator]]`
