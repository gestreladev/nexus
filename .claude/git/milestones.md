---
name: milestones
description: Semver-to-milestone mapping and closure criteria for Nexus.
agent:
  role: git-workflow-specialist
  tier: nano
  weight: soft
  triggers:
    - planning issues under a milestone
    - checking which version maps to which phase
    - closing a milestone
metadata:
  type: reference
---

# Milestones — Nexus

GitHub milestones are **semver-named**, one per roadmap phase.

| Milestone | Phase | Status |
|---|---|---|
| `v0.1.0 — First Service` | Web servers, REST, OpenAPI | ✅ |
| `v0.2.0 — Database Layer` | PostgreSQL, ACID, migrations, ORM | ✅ |
| `v0.3.0 — Auth & Security` | JWT, OAuth2, OWASP, TLS, bcrypt | ⏳ |
| `v0.4.0 — Caching` | Redis, Memcached, HTTP caching | ⏳ |
| `v0.5.0 — Testing & CI/CD` | Tests, GitHub Actions | ⏳ |
| `v0.6.0 — Python Service` | nexus-ingest, FastAPI | ⏳ |
| `v0.7.0 — Async & Messaging` | Kafka, WebSockets, SSE | ⏳ |
| `v0.8.0 — Containers & Architecture` | Docker, microservices, 12-factor | ⏳ |
| `v0.9.0 — Search & Vectors` | pgvector, embeddings, Elasticsearch | ⏳ |
| `v0.10.0 — Observability` | OpenTelemetry, Grafana | ⏳ |
| `v0.11.0 — AI Integration` | RAG, agents, LLM APIs, MCP | ⏳ |
| `v1.0.0 — Production Ready` | CAP, sharding, hardening | ⏳ |

---

## Closure criteria

A milestone closes when:
1. Every issue in it is closed.
2. The corresponding `feat/*` PR is merged to `main`.
3. The version is tagged and a GitHub Release is published.
4. `versioning.md` history is updated.

## References
- [git_orchestrator.md](git_orchestrator.md) · `[[git_orchestrator]]`
- [versioning.md](versioning.md) · `[[versioning]]`
