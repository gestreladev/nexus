---
name: roadmap
description: The 12-phase Nexus roadmap — topics, languages, versions, lessons.
agent:
  role: progress-tracker
  tier: standard
  weight: medium
  triggers:
    - viewing the full roadmap
    - mapping a phase to topics/version/lesson
metadata:
  type: reference
---

# Roadmap — Nexus

Aligned to roadmap.sh/backend. Each phase = a semver milestone + one or more
lessons. Detailed trackers in [phases/](phases/).

| Phase | Version | Topics | Lang | Nexus piece | Lesson | Status |
|---|---|---|---|---|---|---|
| 0 | — | internet, http, dns, git, github | — | repo + structure | 1 | ✅ |
| 1 | v0.1.0 | web servers, REST, JSON, OpenAPI | Kotlin | `nexus-api` scaffold | 2 | ✅ |
| 2 | v0.2.0 | postgres, ACID, indexes, migrations, ORM | Kotlin+SQL | data layer | 3 | ✅ |
| 3 | v0.3.0 | auth, JWT, OAuth2, OWASP, TLS, bcrypt | Kotlin | auth layer | 4 | ✅ |
| 4 | v0.4.0 | caching, redis, memcached, nosql | Kotlin | cache layer | 5 | ✅ |
| 5 | v0.5.0 | testing, unit/integration, CI/CD | Kotlin | tests + Actions | 6 | ✅ |
| 6 | v0.6.0 | python, FastAPI | Python | `nexus-ingest` skeleton | 7 | ✅ |
| 7 | v0.7.0 | kafka, rabbitmq, websockets, SSE | Kotlin+Py | async pipeline | 8 | ✅ |
| 8 | v0.8.0 | docker, 12-factor, monolith/microservices/SOA/serverless, service-mesh, circuit-breaker | Docker | full compose | 9 | ✅ |
| 9 | v0.9.0 | search, elasticsearch, vectors, embeddings | Python+SQL | vector search | 10 | ✅ |
| 10 | v0.10.0 | monitoring, observability, telemetry | Kotlin+Py | OTel + Grafana | 11 | ⏳ |
| 11 | v0.11.0 | LLMs, RAG, agents, function-calling, MCP | Python | `nexus-search` RAG | 12 | ⏳ |
| 12 | v1.0.0 | scale, CAP, replication, sharding, N+1 | Kotlin+Py | prod hardening | 13 | ⏳ |

## Language plan
Kotlin = JVM services; Python = AI/data; SQL throughout. New language → focused
ramp + full GoF tree first (see designpatterns orchestrator).

## References
- [project_develop_orchestrator.md](project_develop_orchestrator.md) · `[[project_develop_orchestrator]]`
- [progress.md](progress.md) · `[[progress]]`
- [git/milestones.md](../git/milestones.md) · `[[milestones]]`
