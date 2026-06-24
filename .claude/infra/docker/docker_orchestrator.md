---
name: docker_orchestrator
description: Routing for Docker knowledge — images, Dockerfiles, Compose, volumes, best practices.
agent:
  role: infra-specialist
  tier: standard
  weight: medium
  triggers:
    - writing a Dockerfile or compose file
    - containerizing a service
    - routing a Docker question
metadata:
  type: orchestrator
---

# Docker Orchestrator — Nexus

How Nexus is containerized. Content verified against current Docker docs
(Context7). Model selection governed by
[model_decision.md](../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Metadata

| Field | Value |
|---|---|
| Compose file | `docker-compose.yml` (repo root) |
| Base images | `eclipse-temurin:17-jre-jammy` (nexus-api), `python:3.13-slim` (nexus-ingest), `pgvector/pgvector:pg17` (DB) |
| Compose spec | services / depends_on `service_healthy` / healthchecks / named volumes |
| Source | docs.docker.com (Context7) |

---

## Routing Table

| Topic | Scope | Document |
|---|---|---|
| Dockerfile | Instructions, layers, build cache, .dockerignore | [dockerfile.md](dockerfile.md) · `[[dockerfile]]` |
| Multi-stage builds | JDK build → slim JRE runtime for the Ktor fat jar | [multistage_builds.md](multistage_builds.md) · `[[multistage_builds]]` |
| Compose | services, depends_on healthy, healthchecks, env | [compose.md](compose.md) · `[[compose]]` |
| Volumes & networks | persistence, bind mounts, service-name DNS | [volumes_and_networks.md](volumes_and_networks.md) · `[[volumes_and_networks]]` |
| Best practices | non-root, slim base, pinning, healthcheck, 12-factor | [best_practices.md](best_practices.md) · `[[best_practices]]` |

---

## References
- [infra_orchestrator.md](../infra_orchestrator.md) · `[[infra_orchestrator]]`
- [facade.md](../../designpatterns/kotlin/structural/facade.md) · `[[facade]]`
