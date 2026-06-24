---
name: phase_08
description: Phase 8 tracker — containers & architecture (Docker, microservices).
agent:
  role: progress-tracker
  tier: nano
  weight: soft
  triggers:
    - reviewing or planning Phase 8
metadata:
  type: reference
---

# Phase 8 — Containers & Architecture

| Field | Value |
|---|---|
| Version | `v0.8.0` ✅ |
| Lesson | 9 |
| Topics | containers/lxc, twelve-factor-apps, architectural-patterns, microservices, monolith, soa, service-mesh, circuit-breaker |
| Status | ✅ shipped — whole system runs from one `docker compose up` |

## Deliverables
- [x] Dockerfiles for each service (multi-stage, non-root) — `nexus-api`, `nexus-ingest`
- [x] Full `docker compose up` runs the whole system (5 services healthy)
- [x] 12-factor config (env vars override yaml defaults; no hardcoded secrets)
- [x] Kafka dual-listener (EXTERNAL host + INTERNAL in-network) — container-to-container flow proven
- [ ] Nginx/Caddy reverse proxy in front; circuit-breaker as code — deferred (concepts taught)

## Proof
`POST /v1/documents` on `localhost:8080` → nexus-api `Published … document.uploaded-0@0`
→ nexus-ingest `consumed … (partition 0 offset 0)` over `kafka:29092`. See
[lesson_09_containers.md](../../lessons/log/lesson_09_containers.md) · `[[lesson_09_containers]]`.

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
- [architecture_vision.md](../architecture_vision.md) · `[[architecture_vision]]`
- [docker_orchestrator.md](../../infra/docker/docker_orchestrator.md) · `[[docker_orchestrator]]`
