---
name: infra_orchestrator
description: Routing for Nexus infrastructure — containerization, and later CI/CD and proxy.
agent:
  role: infra-specialist
  tier: standard
  weight: medium
  triggers:
    - starting infrastructure work
    - routing a containerization / deployment question
metadata:
  type: orchestrator
---

# Infrastructure Orchestrator — Nexus

Governs how Nexus is packaged, run, and deployed. Today this is **Docker**;
CI/CD (GitHub Actions) and a reverse proxy join in later phases. Model selection
governed by [model_decision.md](../configurations/model_decision.md) · `[[model_decision]]`.

---

## Metadata

| Field | Value |
|---|---|
| Local runtime | Docker Compose (`docker-compose.yml` at repo root) |
| Running now | PostgreSQL 17 + pgvector (port 5433) |
| Full system | Phase 8 (`v0.8.0`) — all services containerized |
| CI/CD | Phase 5 (`v0.5.0`) — GitHub Actions; see [git/git_orchestrator.md](../git/git_orchestrator.md) · `[[git_orchestrator]]` |

---

## Routing Table

| Area | Scope | Document |
|---|---|---|
| Docker | Images, Dockerfiles, Compose, volumes, best practices | [docker_orchestrator.md](docker/docker_orchestrator.md) · `[[docker_orchestrator]]` |

---

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
- [architecture_vision.md](../project/architecture_vision.md) · `[[architecture_vision]]`
