---
name: behavioral-orchestrator
description: Routing for Kotlin behavioral GoF patterns.
agent:
  role: pattern-router
  tier: nano
  weight: soft
  triggers:
    - selecting a behavioral pattern
metadata:
  type: orchestrator
---

# Behavioral Patterns — Kotlin

Patterns concerned with algorithms and the assignment of responsibilities.

---

## Routing Table

| Pattern | Intent | Document |
|---|---|---|
| Chain of Responsibility | Pass a request along a handler chain | [chain_of_responsibility.md](chain_of_responsibility.md) · `[[chain_of_responsibility]]` |
| Command | Encapsulate a request as an object | [command.md](command.md) · `[[command]]` |
| Interpreter | Evaluate sentences in a language | [interpreter.md](interpreter.md) · `[[interpreter]]` |
| Iterator | Sequential access without exposing structure | [iterator.md](iterator.md) · `[[iterator]]` |
| Mediator | Centralize complex communication | [mediator.md](mediator.md) · `[[mediator]]` |
| Memento | Capture and restore state | [memento.md](memento.md) · `[[memento]]` |
| Observer | Notify dependents of state changes | [observer.md](observer.md) · `[[observer]]` |
| State | Alter behavior when state changes | [state.md](state.md) · `[[state]]` |
| Strategy | Interchangeable algorithms | [strategy.md](strategy.md) · `[[strategy]]` |
| Template Method | Skeleton with overridable steps | [template_method.md](template_method.md) · `[[template_method]]` |
| Visitor | Operations on an object structure | [visitor.md](visitor.md) · `[[visitor]]` |

---

## References
- [kotlin_orchestrator.md](../kotlin_orchestrator.md) · `[[kotlin_orchestrator]]`
