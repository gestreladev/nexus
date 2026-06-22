---
name: architecture-orchestrator
description: Routing for nexus-api architecture documentation.
agent:
  role: ktor-specialist
  tier: standard
  weight: medium
  triggers:
    - deciding where new code goes
    - reviewing a layer violation
    - applying the Repository pattern
metadata:
  type: orchestrator
---

# nexus-api — Architecture Orchestrator

Layering, dependency rules, and architectural (non-GoF) patterns for nexus-api.
GoF patterns live in the top-level
[design_patterns_orchestrator.md](../../../designpatterns/design_patterns_orchestrator.md)
· `[[design_patterns_orchestrator]]`.

---

## Routing Table

| Area | Scope | Document |
|---|---|---|
| Layers | 4-layer map, dependency direction, where new code goes | [layers.md](layers.md) · `[[layers]]` |
| Repository | Repository pattern (PoEAA) — transaction ownership, mappers | [repository.md](repository.md) · `[[repository]]` |

---

## References
- [nexus_api_orchestrator.md](../nexus_api_orchestrator.md) · `[[nexus_api_orchestrator]]`
- [design_patterns_orchestrator.md](../../../designpatterns/design_patterns_orchestrator.md) · `[[design_patterns_orchestrator]]`
