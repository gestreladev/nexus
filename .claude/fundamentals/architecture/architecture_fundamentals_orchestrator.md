---
name: architecture_fundamentals_orchestrator
description: Routing for architecture fundamentals — deployment styles, 12-factor, mesh, resilience.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - choosing a deployment style (monolith vs microservices vs serverless)
    - grounding 12-factor config, service mesh, or circuit-breaker concepts
metadata:
  type: orchestrator
---

# Architecture Fundamentals — Orchestrator

The durable, language-agnostic architecture knowledge taught in **Phase 8**
(Containers & Architecture): how backend systems are decomposed, configured for
scale, and kept resilient. This is **the knowledge itself** — distinct from
`infra/docker/` (how Nexus is containerized) and `services/` (how each service
is built). Model selection governed by
[model_decision.md](../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Routing Table

| Topic | Scope | Document |
|---|---|---|
| Architectural patterns | Monolith / microservices / SOA / serverless; Conway's law; tradeoffs | [architectural_patterns.md](architectural_patterns.md) · `[[architectural_patterns]]` |
| Twelve-factor apps | Config in env, backing services, stateless processes, disposability | [twelve_factor.md](twelve_factor.md) · `[[twelve_factor]]` |
| Service mesh | Sidecar data/control plane; mTLS, retries, traffic shifting, observability | [service_mesh.md](service_mesh.md) · `[[service_mesh]]` |
| Circuit breaker | Fail fast on a failing dependency; open/half-open/closed; companions | [circuit_breaker.md](circuit_breaker.md) · `[[circuit_breaker]]` |

---

## How Nexus applies it
A small set of independently-deployable services (`nexus-api` Kotlin, `nexus-ingest`
Python, future `nexus-search`) over HTTP + Kafka — pragmatic microservices, not a
sprawl. Phase 8 made the whole system **12-factor** (env config, no committed
secrets) and runnable from one `docker compose up`. Service mesh and circuit
breaker are taught here as concepts, deferred as code until scale warrants them.

## References
- [fundamentals_orchestrator.md](../fundamentals_orchestrator.md) · `[[fundamentals_orchestrator]]`
- [lesson_09_containers.md](../../lessons/log/lesson_09_containers.md) · `[[lesson_09_containers]]`
- [docker_orchestrator.md](../../infra/docker/docker_orchestrator.md) · `[[docker_orchestrator]]`
