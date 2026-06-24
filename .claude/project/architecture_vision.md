---
name: architecture_vision
description: Target end-state architecture diagram for the Nexus platform.
agent:
  role: progress-tracker
  tier: standard
  weight: soft
  triggers:
    - reviewing the target system architecture
    - placing a new component in the overall design
metadata:
  type: reference
---

# Architecture Vision — Nexus

Target end-state (v1.0.0). Current build (v0.8.0) reaches the full 5-service
containerized system — `nexus-api` + PostgreSQL + Redis + Kafka + `nexus-ingest`,
all running from one `docker compose up`; search/observability/RAG arrive next.

```
                         ┌───────────────────────────────────────┐
   Client ───────────────► nexus-api (Kotlin / Ktor)   [auth: JWT/OAuth2]
                         │      │                                  │
                         │      ├──► PostgreSQL + pgvector         │  users, docs, embeddings
                         │      ├──► Redis                         │  cache, sessions
                         │      └──► Kafka  ──────────────┐        │  async ingestion events
                         │                                ▼        │
                         │                     nexus-ingest (Python/FastAPI)
                         │                     parse → chunk → embed → pgvector
                         │                                │        │
                         │                     nexus-search (Python/FastAPI)
                         │                     vector search → RAG → LLM (Anthropic)
                         │                                                  │
                         │   OpenTelemetry → Grafana  (all services)        │
                         └───────────────────────────────────────┘
        All services containerized via Docker Compose.
```

## Build status by component
| Component | Phase | Status |
|---|---|---|
| nexus-api gateway | 1 | ✅ |
| PostgreSQL data layer | 2 | ✅ |
| Auth (JWT/OAuth2) | 3 | ✅ |
| Redis cache | 4 | ✅ |
| nexus-ingest + Kafka | 6–7 | ✅ |
| Containerization (whole system, Compose) | 8 | ✅ |
| pgvector + search | 9 | ⏳ |
| Observability | 10 | ⏳ |
| nexus-search RAG | 11 | ⏳ |

## References
- [objective.md](objective.md) · `[[objective]]`
- [services_orchestrator.md](../services/services_orchestrator.md) · `[[services_orchestrator]]`
