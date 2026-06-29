---
name: observability_stack
description: The grafana/otel-lgtm observability backend and OTLP wiring (Phase 10).
agent:
  role: infra-specialist
  tier: standard
  weight: soft
  triggers:
    - running or wiring the observability backend
    - debugging OTLP export / why a trace isn't showing in Grafana
metadata:
  type: reference
---

# Observability Stack — Nexus

The telemetry backend. The *concepts* live in
[observability_orchestrator.md](../fundamentals/observability/observability_orchestrator.md)
· `[[observability_orchestrator]]`; this is how it runs in Compose.

## The one container
`grafana/otel-lgtm:<pinned>` bundles the whole stack — chosen over five separate
containers for a single-node local setup (ADR 19):

| Inside | Role |
|---|---|
| OTel **Collector** | receives OTLP, routes to the stores |
| **Tempo** | trace store |
| **Loki** | log store (Session B) |
| **Prometheus** | metric store (Session B) |
| **Grafana** | UI, datasources pre-provisioned |

## Ports & wiring
| Port | Purpose |
|---|---|
| `3000` | Grafana UI (`admin/admin`) |
| `4317` | OTLP/gRPC |
| `4318` | OTLP/HTTP |

Both services export to **`http://lgtm:4318`** (service-name DNS, never
`localhost` — that's the container itself). `nexus-api` reads `OTEL_*` env (the
Java agent); `nexus-ingest` reads `OTEL_EXPORTER_OTLP_*` (the SDK). Both
`depends_on: lgtm` (healthy). The healthcheck hits `:3000/api/health` with
**curl** — the image has curl, not wget.

## Verify a trace
1. Drive a request (e.g. `POST /v1/documents`).
2. Grafana → Explore → Tempo → search `{ resource.service.name = "nexus-api" } && { resource.service.name = "nexus-ingest" }`.
3. One trace spanning both services = propagation across Kafka worked.

## References
- [observability_orchestrator.md](../fundamentals/observability/observability_orchestrator.md) · `[[observability_orchestrator]]`
- [compose.md](docker/compose.md) · `[[compose]]`
- [lesson_11_observability.md](../lessons/log/lesson_11_observability.md) · `[[lesson_11_observability]]`
