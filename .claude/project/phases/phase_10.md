---
name: phase_10
description: Phase 10 tracker — observability (OpenTelemetry, Grafana).
agent:
  role: progress-tracker
  tier: nano
  weight: soft
  triggers:
    - reviewing or planning Phase 10
metadata:
  type: reference
---

# Phase 10 — Observability

| Field | Value |
|---|---|
| Version | `v0.10.0` 🔄 |
| Lesson | 11 |
| Topics | monitoring, observability, telemetry, instrumentation, profiling-performance |
| Status | 🔄 Session A ✅ (tracing live + verified); Session B = metrics + logs |

## Deliverables
- [x] OpenTelemetry tracing across all services (Java agent + Python SDK)
- [x] Trace a request end-to-end (one `trace_id`, 14 spans, across Kafka) — verified
- [x] Grafana LGTM backend (`grafana/otel-lgtm`) in compose
- [ ] Metrics + RED Grafana dashboard (Session B)
- [ ] Structured JSON logs with `trace_id` correlation (Session B)

## Proof (Session A)
One upload = nexus-api POST → JDBC → Kafka publish → (traceparent in headers) →
nexus-ingest receive → process → `embed` (291/318ms) → pgvector upsert, all one
trace in Tempo. See
[lesson_11_observability.md](../../lessons/log/lesson_11_observability.md) · `[[lesson_11_observability]]`.

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
