---
name: lesson_11_observability
description: Lesson 11 log — observability; distributed tracing across services (Session A).
agent:
  role: tutor
  tier: nano
  weight: soft
  triggers:
    - reviewing what Lesson 11 covered
    - recapping observability / OpenTelemetry / tracing before a session
metadata:
  type: reference
---

# Lesson 11 — Observability (Phase 10)

| Field | Value |
|---|---|
| Phase | 10 — Observability |
| Roadmap topics | monitoring, observability, telemetry, instrumentation, profiling-performance |
| Deliverable | All three pillars (traces + metrics + logs) across both services, verified |
| Milestone | `v0.10.0` |
| Status | ✅ mastery pass — tracing + RED metrics + correlated logs live in the LGTM stack |

## Concepts taught
- **Three pillars** — logs (what happened), metrics (how much/often), traces (the
  path of *one* request). Complementary, not redundant. "up" (liveness) ≠
  "working well" (re-taught in pt-BR with a visual — see gap rule).
- **OpenTelemetry** — instrument once, export anywhere; SDK vs zero-code agent;
  the Collector; OTLP (4317 gRPC / 4318 HTTP).
- **Distributed tracing** — trace/span; `trace_id` + `parent_span_id`; the hard
  part is **context propagation** across boundaries — the W3C `traceparent`,
  injected by the producer / extracted by the consumer, **across Kafka headers**.
- **Metrics (RED)** + percentiles (p99 vs the lying average) and **structured
  logs** (trace-correlated) — previewed; built in Session B.

## Exercises (recap)
| Q | Topic | Verdict |
|---|---|---|
| R1 | semantic search = compare embeddings by cosine | ✅ sharpened |
| R2 | embed_documents vs embed_query = retrieval asymmetry | 🟡 taught |
| R3 | "up" ≠ "working well" (healthcheck misses latency/errors/saturation) | 🔁 pt-BR + visual |
| E1 | metrics (trend) vs **traces** (one request's path) | #1 ✅ / #2 🔁 traces |
| E2 | `traceId`+`parentSpanId` travel in **Kafka headers** (`traceparent`) | ✅ |

## Built — Session A (verified)
- `grafana/otel-lgtm` all-in-one backend (Collector + Tempo + Loki + Prometheus +
  Grafana) in compose; both services export OTLP to `http://lgtm:4318`.
- `nexus-api`: **zero-code OTel Java agent** (`JAVA_TOOL_OPTIONS` + `OTEL_*`);
  auto-instruments Ktor/JDBC and the Kafka producer (injects `traceparent`).
- `nexus-ingest`: `app/telemetry.py` (init first in lifespan) + FastAPI/aiokafka/
  asyncpg instrumentation + a manual `embed` span; the **consumer explicitly
  extracts** the `traceparent` from the message headers (E2 made real) so the
  processing spans join the producer's trace.
- **Proven:** one upload = **one trace_id, 14 spans, 2 services** — nexus-api
  POST → JDBC → Kafka publish → (across Kafka) → nexus-ingest receive → process →
  `embed` (291 of 318ms) → pgvector upsert. mypy strict + ruff + 13 tests green.

## Built — Session B (verified)
- **Metrics**: nexus-api agent exports JVM/HTTP via OTLP (277 series); nexus-ingest
  `MeterProvider` + custom `nexus.documents.ingested` counter + `nexus.embed.duration`
  histogram. Verified in Prometheus: counter=3, embed p95≈462ms, HTTP server
  duration histogram present (RED Duration).
- **Logs**: OTel `LoggingHandler` ships records over OTLP → Loki; each line carries
  the active span's `trace_id` → **log↔trace correlation** confirmed in Loki.

## Next (Lesson 12)
- AI integration / RAG (`nexus-search`) — Phase 11. Optional polish: a saved
  Grafana RED dashboard JSON; `OTEL_*` sampling for production.

## References
- [lesson_10_search.md](lesson_10_search.md) · `[[lesson_10_search]]`
- [observability_orchestrator.md](../../fundamentals/observability/observability_orchestrator.md) · `[[observability_orchestrator]]`
- [phase_10.md](../../project/phases/phase_10.md) · `[[phase_10]]`
