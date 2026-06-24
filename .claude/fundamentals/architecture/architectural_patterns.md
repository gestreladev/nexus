---
name: architectural_patterns
description: The deployment-granularity spectrum — monolith, modular monolith, microservices, SOA, serverless — and how to choose.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - deciding how to split (or not split) a system into deployable units
    - reasoning about deploy independence, data ownership, or operational complexity tradeoffs
metadata:
  type: reference
---

# Architectural Patterns: The Deployment-Granularity Spectrum

The core axis is **the unit of deployment** — how much code ships and scales as one
artifact. Finer granularity buys independence and isolated scaling at the cost of
operational and data-consistency complexity. There is no "best"; there is a fit.

## The Patterns

### Monolith
One codebase, one deployable artifact, usually one shared database. A single process
handles all concerns.
- **Unit of deployment**: the whole application.
- **Fits**: small teams, early products, unproven domains where boundaries are still moving.
- **Tradeoffs**: simplest ops and lowest latency (in-process calls); but deploys are
  all-or-nothing, one slow module can't scale alone, and the codebase tends toward
  coupling over time.

### Modular Monolith
A monolith with **enforced internal module boundaries** (clear interfaces, no
cross-module DB reaching). Still one artifact, but structured so it *could* be split later.
- **Unit of deployment**: the whole application (logical modules inside).
- **Fits**: teams who want clean boundaries without distributed-systems overhead — often
  the right first step before microservices.
- **Tradeoffs**: keeps deploy/ops simplicity; discipline is enforced by convention/tooling,
  not the network, so boundaries can erode if unguarded.

### Microservices
Many small services, each **independently deployable**, each owning its data store, talking
over the network (HTTP/gRPC/messaging).
- **Unit of deployment**: a single service.
- **Fits**: multiple teams needing to ship independently; components with very different
  scaling or reliability profiles.
- **Tradeoffs**: deploy independence and per-service scaling — but you pay in operational
  complexity (observability, deployment, networking), eventual-consistency across stores,
  and network latency/partial failure. Communication must be *managed* (see service mesh,
  circuit breaker below).

### SOA (Service-Oriented Architecture)
The enterprise predecessor of microservices: coarser services often integrated through a
central **ESB** (enterprise service bus) with shared, canonical schemas.
- **Unit of deployment**: a service, but coupled through shared bus/contracts.
- **Fits**: large orgs integrating heterogeneous legacy systems.
- **Tradeoffs**: reuse and central governance — but the ESB becomes a bottleneck and a
  point of coupling. Microservices are essentially "SOA done with dumb pipes, smart endpoints."

### Serverless (FaaS)
Code deployed as **functions**; the platform provisions, scales (to zero), and bills per
invocation. Event-driven.
- **Unit of deployment**: a single function.
- **Fits**: spiky/event-driven workloads, glue logic, low-baseline-traffic endpoints.
- **Tradeoffs**: zero idle cost and elastic scale — but cold starts add latency, state must
  live elsewhere, vendor lock-in is real, and debugging distributed function graphs is hard.

## Comparison

| Pattern            | Deploy unit | Deploy independence | Data ownership      | Op. complexity | Latency        | Team scaling |
|--------------------|-------------|---------------------|---------------------|----------------|----------------|--------------|
| Monolith           | whole app   | none                | one shared DB       | low            | low (in-proc)  | poor         |
| Modular monolith   | whole app   | none                | shared, partitioned | low            | low (in-proc)  | moderate     |
| Microservices      | per service | high                | per-service DB      | high           | network hops   | excellent    |
| SOA                | per service | moderate (ESB)      | shared canonical    | high           | bus hops       | moderate     |
| Serverless (FaaS)  | per function| very high           | external state      | moderate       | cold starts    | good         |

## Conway's Law

> "Organizations design systems that mirror their own communication structure." — Melvin Conway

Architecture and org chart converge. Three teams tend to produce three services; one team
fighting a microservice sprawl produces a distributed monolith. The corollary (*Inverse
Conway Maneuver*) is to shape teams around the architecture you want. Pick a granularity
your team structure can actually sustain.

## In Nexus

Nexus is **pragmatic microservices, not a sprawl**: a small set of independently-deployable
services, each owning its concern, communicating over **HTTP + Kafka**.

- `nexus-api` (Kotlin) — request/serving edge.
- `nexus-ingest` (Python) — ingestion pipeline.
- `nexus-search` (future) — search/indexing.

The split is justified by genuinely different runtimes (JVM vs Python) and scaling
profiles, not by reflex. Synchronous calls go over HTTP; asynchronous, decoupled flow goes
over Kafka (see [messaging_orchestrator.md](../messaging/messaging_orchestrator.md) ·
`[[messaging_orchestrator]]`). Because services now talk over the network, their
communication needs explicit management — resilience via
[circuit_breaker.md](circuit_breaker.md) · `[[circuit_breaker]]`, and routing/observability
via [service_mesh.md](service_mesh.md) · `[[service_mesh]]`. The operational substrate is
containers — see [docker_orchestrator.md](../../infra/docker/docker_orchestrator.md) ·
`[[docker_orchestrator]]`. Keeping the service count small is the deliberate hedge against
microservice complexity.

## References

- Architecture orchestrator: [architecture_fundamentals_orchestrator.md](architecture_fundamentals_orchestrator.md) · `[[architecture_fundamentals_orchestrator]]`
- Sibling patterns: [service_mesh.md](service_mesh.md) · `[[service_mesh]]`, [circuit_breaker.md](circuit_breaker.md) · `[[circuit_breaker]]`, [twelve_factor.md](twelve_factor.md) · `[[twelve_factor]]`
- Lesson that grounded deployment units: [lesson_09_containers.md](../../lessons/log/lesson_09_containers.md) · `[[lesson_09_containers]]`
