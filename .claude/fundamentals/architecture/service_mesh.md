---
name: service_mesh
description: Dedicated infra layer for service-to-service comms via sidecar proxies — mTLS, retries, traffic shifting, observability without app code changes.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - deciding whether to adopt a service mesh as the service count grows
    - needing mTLS, canary releases, or circuit breaking across many services without touching app code
metadata:
  type: reference
---
# Service Mesh

A **service mesh** is a dedicated infrastructure layer that handles
service-to-service communication. It moves cross-cutting network concerns
(security, reliability, observability) **out of application code** and into a
uniform layer operated by the platform — not each app.

## Sidecar model: data plane vs control plane

The mesh splits into two planes:

| Plane | What it is | Example | Job |
|-------|-----------|---------|-----|
| **Data plane** | A proxy deployed next to *every* service instance (the **sidecar**) | **Envoy** | Intercepts all in/out traffic, applies policy, emits telemetry |
| **Control plane** | Central brain that configures every sidecar | **Istio**, **Linkerd** | Distributes config, certs, routing rules; aggregates telemetry |

```
            ┌──────────── control plane (Istio/Linkerd) ───────────┐
            │   policy · certs · routing · telemetry aggregation    │
            └───────┬───────────────────────────────────┬──────────┘
                    ▼ config/certs                       ▼
        ┌──────────────────┐                 ┌──────────────────┐
        │  svc A           │                 │  svc B           │
        │  ┌────────────┐  │   mTLS traffic  │  ┌────────────┐  │
        │  │ app        │◀─┼─────────────────┼─▶│ app        │  │
        │  └────────────┘  │                 │  └────────────┘  │
        │  │ sidecar(Envoy)│◀───────────────▶│ sidecar(Envoy)│  │
        └──────────────────┘                 └──────────────────┘
```

The app talks to `localhost`; the sidecar does the network work. Apps stay
unaware they are meshed — **no library, no redeploy of business code**.

## What it provides (without app code changes)

- **mTLS** — automatic mutual TLS between every service: encryption +
  cryptographic identity. Certs issued/rotated by the control plane.
- **Retries / timeouts** — uniform, per-route policy applied at the proxy.
- **Traffic shifting / canary** — route X% to v2, gradually shift; blue/green,
  A/B, mirroring — all by config, no deploy.
- **Observability (golden signals)** — latency, traffic, errors, saturation
  emitted uniformly for every hop, for free.
- **Circuit breaking** — eject failing endpoints, cap concurrent requests; see
  [circuit_breaker.md](circuit_breaker.md) · `[[circuit_breaker]]`.
- **Service discovery + load balancing** — proxy resolves and balances across
  healthy instances.

The pitch: these capabilities become **platform features**, consistent across
every service and language, instead of N libraries drifting out of sync.

## Where to put cross-cutting concerns

Three places can own retries/mTLS/observability. They are not mutually
exclusive — a gateway guards the edge; a mesh governs the interior.

| Approach | Scope | Pros | Cons |
|----------|-------|------|------|
| **In-app library** (e.g. Resilience4j, gRPC interceptors) | Per service | Full control, no infra | Per-language, drifts, must redeploy to change policy |
| **API gateway** | **North-south** (client↔system edge) | Auth, rate-limit, TLS termination at one door | Blind to **east-west** service↔service traffic |
| **Service mesh** | **East-west** (service↔service interior) | Uniform, language-agnostic, config-driven | Operational + latency cost of a proxy per pod |

A mesh shines for **east-west** traffic the gateway never sees.

## When it's worth it — and when it's overkill

**Worth it** when you have *many* services (tens+), multiple languages, and
real needs for uniform mTLS, fine-grained traffic control, and per-hop golden
signals — where reimplementing all of that per service is the larger cost.

**Overkill** for a handful of services: the mesh adds a proxy per pod (latency,
memory), a control plane to operate, and a steep debugging surface. A library
or gateway covers the same needs at a fraction of the operational burden.

> Rule of thumb: adopt a mesh when the cost of *not* having uniform
> policy/observability across services exceeds the cost of running the mesh.

### In Nexus

**Not adopted.** Nexus runs a handful of services; the mesh's operational cost
(sidecars, control plane, debugging complexity) outweighs the benefit at this
scale. Cross-cutting concerns are handled in-app and at the edge today. A mesh
becomes relevant only **if the platform grows** to many services across
languages — at which point uniform mTLS and east-west observability would
justify the overhead. Microservices growth is the motivating force; see
[architectural_patterns.md](architectural_patterns.md) ·
`[[architectural_patterns]]`.

Container/orchestration context (where sidecars live) is covered in
[docker_orchestrator.md](../../infra/docker/docker_orchestrator.md) ·
`[[docker_orchestrator]]`.

## References

- Architecture orchestrator: [architecture_fundamentals_orchestrator.md](architecture_fundamentals_orchestrator.md) · `[[architecture_fundamentals_orchestrator]]`
- Sibling — circuit breaking (a mesh capability): [circuit_breaker.md](circuit_breaker.md) · `[[circuit_breaker]]`
- Sibling — microservices motivate the mesh: [architectural_patterns.md](architectural_patterns.md) · `[[architectural_patterns]]`
- Lesson 9 (containers): [lesson_09_containers.md](../../lessons/log/lesson_09_containers.md) · `[[lesson_09_containers]]`
- Messaging (async alternative to sync mesh calls): [messaging_orchestrator.md](../messaging/messaging_orchestrator.md) · `[[messaging_orchestrator]]`
