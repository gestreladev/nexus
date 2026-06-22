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
| Version | `v0.8.0` ⏳ |
| Lesson | 9 |
| Topics | containers/lxc, twelve-factor-apps, architectural-patterns, microservices, monolith, soa, service-mesh, circuit-breaker |
| Status | ⏳ planned |

## Planned deliverables
- [ ] Dockerfiles for each service (multi-stage)
- [ ] Full `docker compose up` runs the whole system
- [ ] 12-factor config (env vars, no hardcoded config)
- [ ] Nginx/Caddy reverse proxy in front; circuit-breaker concept

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
- [architecture_vision.md](../architecture_vision.md) · `[[architecture_vision]]`
- [docker_orchestrator.md](../../infra/docker/docker_orchestrator.md) · `[[docker_orchestrator]]`
