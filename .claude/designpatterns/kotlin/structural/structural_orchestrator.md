---
name: structural-orchestrator
description: Routing for Kotlin structural GoF patterns.
agent:
  role: pattern-router
  tier: nano
  weight: soft
  triggers:
    - selecting a structural pattern
metadata:
  type: orchestrator
---

# Structural Patterns — Kotlin

Patterns that compose objects and classes into larger structures.

---

## Routing Table

| Pattern | Intent | Document |
|---|---|---|
| Adapter | Convert one interface to another | [adapter.md](adapter.md) · `[[adapter]]` |
| Bridge | Decouple abstraction from implementation | [bridge.md](bridge.md) · `[[bridge]]` |
| Composite | Tree structures, uniform leaf/branch | [composite.md](composite.md) · `[[composite]]` |
| Decorator | Add behavior dynamically | [decorator.md](decorator.md) · `[[decorator]]` |
| Facade | Simplified interface to a subsystem | [facade.md](facade.md) · `[[facade]]` |
| Flyweight | Share state to reduce memory | [flyweight.md](flyweight.md) · `[[flyweight]]` |
| Proxy | Surrogate controlling access | [proxy.md](proxy.md) · `[[proxy]]` |

---

## References
- [kotlin_orchestrator.md](../kotlin_orchestrator.md) · `[[kotlin_orchestrator]]`
