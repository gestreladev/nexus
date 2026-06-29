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
| Version | `v0.10.0` ✅ |
| Lesson | 11 |
| Topics | monitoring, observability, telemetry, instrumentation, profiling-performance |
| Status | ✅ all three pillars live + verified in the LGTM stack |

## Deliverables
- [x] OpenTelemetry **tracing** across all services (Java agent + Python SDK)
- [x] Trace a request end-to-end (one `trace_id`, 14 spans, across Kafka) — verified
- [x] Grafana LGTM backend (`grafana/otel-lgtm`) in compose
- [x] **Metrics** (RED) — custom counter/histogram + auto HTTP/JVM; p95 computed in Prometheus
- [x] **Structured logs** over OTLP → Loki, each carrying `trace_id` (log↔trace correlation)

## Proof
- Trace: one upload = POST → JDBC → publish → (traceparent in Kafka headers) →
  receive → process → `embed` → upsert, all one trace in Tempo.
- Metrics: `nexus_documents_ingested_total`=3, embed-duration histogram p95≈462ms,
  nexus-api 277 series.
- Logs: nexus-ingest lines in Loki tagged with `trace_id`. See
  [lesson_11_observability.md](../../lessons/log/lesson_11_observability.md) · `[[lesson_11_observability]]`.

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
