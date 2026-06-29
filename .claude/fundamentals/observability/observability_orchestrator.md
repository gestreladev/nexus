---
name: observability_orchestrator
description: Routing for observability fundamentals — pillars, OpenTelemetry, tracing, metrics, logs.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - instrumenting a service or debugging where time goes
    - grounding logs / metrics / traces or OpenTelemetry concepts
metadata:
  type: orchestrator
---

# Observability Fundamentals — Orchestrator

The durable, language-agnostic observability knowledge taught in **Phase 10**:
how to see what a running system is actually doing — beyond "is it up?". This is
**the knowledge itself** — distinct from `infra/` (the LGTM stack that stores it)
and `services/` (how each service is instrumented). Model selection governed by
[model_decision.md](../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Routing Table

| Topic | Scope | Document |
|---|---|---|
| Three pillars | Logs vs metrics vs traces; observability vs monitoring; "up" ≠ "working well" | [three_pillars.md](three_pillars.md) · `[[three_pillars]]` |
| OpenTelemetry | The standard: SDK, auto-instrumentation, Collector, OTLP, semantic conventions | [opentelemetry.md](opentelemetry.md) · `[[opentelemetry]]` |
| Distributed tracing | trace/span, context propagation, W3C `traceparent`, crossing Kafka | [distributed_tracing.md](distributed_tracing.md) · `[[distributed_tracing]]` |
| Metrics (RED) | Rate/Errors/Duration; histograms; p50/p95/p99; why the average lies | [metrics_red.md](metrics_red.md) · `[[metrics_red]]` |
| Structured logging | JSON logs; `trace_id` correlation; levels; what not to log | [structured_logging.md](structured_logging.md) · `[[structured_logging]]` |

---

## How Nexus applies it
`nexus-api` (Kotlin) runs the **zero-code OTel Java agent**; `nexus-ingest`
(Python) uses the OTel SDK + FastAPI/aiokafka/asyncpg instrumentation. A document
upload is traced **end-to-end** — the W3C `traceparent` rides the Kafka headers,
so one `trace_id` spans both services (the `embed` span dominates). Backend is
the `grafana/otel-lgtm` all-in-one (Collector + Tempo + Loki + Prometheus +
Grafana). Session A shipped tracing; metrics (RED) + structured logs land in
Session B.

## References
- [fundamentals_orchestrator.md](../fundamentals_orchestrator.md) · `[[fundamentals_orchestrator]]`
- [lesson_11_observability.md](../../lessons/log/lesson_11_observability.md) · `[[lesson_11_observability]]`
- [messaging_orchestrator.md](../messaging/messaging_orchestrator.md) · `[[messaging_orchestrator]]`
