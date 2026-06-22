---
name: testing-orchestrator
description: Routing for nexus-api testing documentation.
agent:
  role: test-specialist
  tier: standard
  weight: soft
  triggers:
    - writing a test for nexus-api
    - deciding fake vs mock
metadata:
  type: orchestrator
---

# nexus-api — Testing Orchestrator

Test strategy for nexus-api. Model selection governed by
[model_decision.md](../../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Routing Table

| Area | Scope | Document |
|---|---|---|
| Strategy | Test pyramid, `testApplication`, fakes vs mocks, naming | [strategy.md](strategy.md) · `[[strategy]]` |

---

## References
- [nexus_api_orchestrator.md](../nexus_api_orchestrator.md) · `[[nexus_api_orchestrator]]`
