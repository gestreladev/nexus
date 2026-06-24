---
name: nexus-api-orchestrator
description: Routing and metadata for the nexus-api service documentation.
agent:
  role: ktor-specialist
  tier: standard
  weight: medium
  triggers:
    - starting a nexus-api task
    - routing a nexus-api request to the correct doc
metadata:
  type: orchestrator
---

# nexus-api — Service Orchestrator

Governs all documentation scoped to `nexus-api` (Kotlin / Ktor gateway service).
Model selection governed by
[model_decision.md](../../configurations/model_decision.md) · `[[model_decision]]`.

**Code tasks (Rule 7):** before writing Kotlin, consult
[design_patterns_orchestrator.md](../../designpatterns/design_patterns_orchestrator.md)
· `[[design_patterns_orchestrator]]` and declare the GoF pattern, or justify none.

---

## Metadata

| Field | Value |
|---|---|
| Path | `nexus-api/` |
| Language | Kotlin 2.1.21 |
| Framework | Ktor 3.1.3 (Netty engine) |
| Build | Gradle wrapper 8.13, version catalog |
| Database | PostgreSQL 17 via Exposed + HikariCP + Flyway |
| Port | 8080 |
| Container | multi-stage `Dockerfile` (Temurin 17, non-root); runs in Compose with 12-factor env + `/v1/health` healthcheck |
| Version | v0.8.0 |

---

## Routing Table

| Area | Scope | Document |
|---|---|---|
| Architecture | Layer map, dependency rules, Repository pattern | [architecture_orchestrator.md](architecture/architecture_orchestrator.md) · `[[architecture_orchestrator]]` |
| API | URL structure, status codes, response shapes | [api_orchestrator.md](api/api_orchestrator.md) · `[[api_orchestrator]]` |
| Idioms | Kotlin DSL, Exposed DSL patterns | [idioms_orchestrator.md](idioms/idioms_orchestrator.md) · `[[idioms_orchestrator]]` |
| Testing | Test pyramid, testApplication, fakes | [testing_orchestrator.md](testing/testing_orchestrator.md) · `[[testing_orchestrator]]` |

---

## References
- [services_orchestrator.md](../services_orchestrator.md) · `[[services_orchestrator]]`
- [CLAUDE.md](../../CLAUDE.md) · `[[CLAUDE]]`
