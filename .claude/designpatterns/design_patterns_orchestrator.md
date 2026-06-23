---
name: design-patterns-orchestrator
description: Top-level routing for GoF design patterns, by language and category.
agent:
  role: pattern-router
  tier: standard
  weight: soft
  triggers:
    - selecting a GoF pattern before writing code
    - routing a design-pattern query by language or category
metadata:
  type: orchestrator
---

# Design Patterns Orchestrator

Governs all GoF (Gang of Four) design-pattern reference docs. Per Rule 7, any
Kotlin/Python code task must declare the applicable GoF pattern (or justify
none) before implementation.

---

## Metadata

| Field | Value |
|---|---|
| Methodology | GoF — Gang of Four (23 patterns) |
| Source | Refactoring.Guru, GoF book |
| Languages in use | Kotlin (nexus-api) |
| Planned | Python (nexus-ingest v0.6.0, nexus-search v0.11.0) |

---

## New-language rule

**Any newly-introduced language must receive its own full GoF tree (all 23
patterns, language-idiomatic) before code is written in it.** When `nexus-ingest`
begins, build `designpatterns/python/**` first.

---

## Language routing

| Language | Status | Folder |
|---|---|---|
| Kotlin | ✅ active | [kotlin_orchestrator.md](kotlin/kotlin_orchestrator.md) · `[[kotlin_orchestrator]]` |
| Python | ✅ active | [python_orchestrator.md](python/python_orchestrator.md) · `[[python_orchestrator]]` |

> Python GoF files use a `py_` filename prefix (e.g. `py_strategy.md`) so
> wikilinks stay unique alongside the unprefixed Kotlin tree.

---

## Category routing (Kotlin)

| Category | Patterns | Document |
|---|---|---|
| Creational | Factory Method, Abstract Factory, Builder, Prototype, Singleton | [creational_orchestrator.md](kotlin/creational/creational_orchestrator.md) · `[[creational_orchestrator]]` |
| Structural | Adapter, Bridge, Composite, Decorator, Facade, Flyweight, Proxy | [structural_orchestrator.md](kotlin/structural/structural_orchestrator.md) · `[[structural_orchestrator]]` |
| Behavioral | Chain of Responsibility, Command, Interpreter, Iterator, Mediator, Memento, Observer, State, Strategy, Template Method, Visitor | [behavioral_orchestrator.md](kotlin/behavioral/behavioral_orchestrator.md) · `[[behavioral_orchestrator]]` |

---

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
- [Refactoring.Guru — Design Patterns](https://refactoring.guru/design-patterns)
