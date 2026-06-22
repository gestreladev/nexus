---
name: api-orchestrator
description: Routing for nexus-api HTTP API documentation.
agent:
  role: api-specialist
  tier: standard
  weight: soft
  triggers:
    - designing or reviewing an HTTP endpoint
    - choosing a status code or response shape
metadata:
  type: orchestrator
---

# nexus-api — API Orchestrator

HTTP API design conventions for nexus-api. Model selection governed by
[model_decision.md](../../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Routing Table

| Area | Scope | Document |
|---|---|---|
| Conventions | URL structure, methods, status codes, response shapes, pagination | [conventions.md](conventions.md) · `[[conventions]]` |

---

## References
- [nexus_api_orchestrator.md](../nexus_api_orchestrator.md) · `[[nexus_api_orchestrator]]`
