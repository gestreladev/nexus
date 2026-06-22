---
name: services-orchestrator
description: Routes to each Nexus service's technical documentation.
agent:
  role: service-router
  tier: standard
  weight: soft
  triggers:
    - starting work on a specific service
    - routing a service-scoped request
metadata:
  type: orchestrator
---

# Services Orchestrator — Nexus

Routes to per-service documentation. Each service owns its architecture, API,
idioms, and testing docs. Model selection governed by
[model_decision.md](../configurations/model_decision.md) · `[[model_decision]]`.

---

## Routing Table

| Service | Language | Scope | Document |
|---|---|---|---|
| nexus-api | Kotlin / Ktor | Gateway API — users, documents, auth, routing | [nexus_api_orchestrator.md](nexus-api/nexus_api_orchestrator.md) · `[[nexus_api_orchestrator]]` |
| nexus-ingest | Python / FastAPI | Document ingestion, chunking, embedding (v0.6.0) | ⏳ planned |
| nexus-search | Python / FastAPI | Vector search, RAG, LLM responses (v0.11.0) | ⏳ planned |

---

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
