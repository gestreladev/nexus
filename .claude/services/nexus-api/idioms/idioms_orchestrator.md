---
name: idioms-orchestrator
description: Routing for nexus-api Kotlin idiom documentation.
agent:
  role: ktor-specialist
  tier: standard
  weight: soft
  triggers:
    - writing idiomatic Kotlin for nexus-api
    - reviewing DSL or Exposed usage
metadata:
  type: orchestrator
---

# nexus-api — Idioms Orchestrator

Kotlin idioms used across nexus-api. Model selection governed by
[model_decision.md](../../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Routing Table

| Area | Scope | Document |
|---|---|---|
| Kotlin DSL | `@DslMarker`, lambdas with receivers, builder pattern | [dsl.md](dsl.md) · `[[dsl]]` |
| Exposed DSL | Tables, queries, ResultRow mappers, transactions | [exposed_dsl.md](exposed_dsl.md) · `[[exposed_dsl]]` |

---

## References
- [nexus_api_orchestrator.md](../nexus_api_orchestrator.md) · `[[nexus_api_orchestrator]]`
