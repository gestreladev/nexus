---
name: opentelemetry
description: Vendor-neutral standard unifying traces, metrics, and logs — instrument once, export anywhere.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - choosing how to instrument a service for traces/metrics/logs
    - setting up an OTel Collector or OTLP export pipeline
    - deciding between auto-instrumentation and the SDK
metadata:
  type: reference
---

# OpenTelemetry (OTel)

OpenTelemetry is the CNCF vendor-neutral standard for **observability telemetry**: one set of APIs, SDKs, wire format, and conventions for producing traces, metrics, and logs. Its core promise is **instrument once, export anywhere** — application code depends only on the OTel API, never on a vendor; the destination is a deploy-time config choice.

## The data model: signals

OTel defines three **signals**, each a distinct data type with a shared correlation backbone.

| Signal | What it captures | Correlated by |
|--------|------------------|---------------|
| Traces | Causal tree of **spans** across a request (timing, parent/child, attributes, events) | `trace_id`, `span_id` |
| Metrics | Aggregated numeric measurements (counters, gauges, histograms) | exemplars link to `trace_id` |
| Logs    | Timestamped records, structured | `trace_id`/`span_id` stamped on emit |

A **span** is a named, timed operation with attributes and a parent. **Context propagation** carries `trace_id` + `span_id` across process boundaries (W3C `traceparent`), stitching independent services into one trace. A **Resource** describes the producer (`service.name`, `service.version`, host) and is attached to every signal. See [distributed_tracing.md](distributed_tracing.md) · `[[distributed_tracing]]` and [three_pillars.md](three_pillars.md) · `[[three_pillars]]`.

## SDK vs auto-instrumentation

Two ways to emit telemetry; they coexist.

- **SDK (manual/programmatic)** — your code calls the OTel API to start spans, record metrics, and configure exporters. Maximum control; needed for domain-specific spans the runtime cannot infer.
- **Auto-instrumentation (zero-code)** — bytecode/monkey-patching wraps known libraries (HTTP servers, JDBC, Kafka, gRPC) so spans appear with no source changes.

**Java** — a *zero-code* agent attaches at JVM startup; no recompile:

```bash
java -javaagent:/otel/opentelemetry-javaagent.jar -jar app.jar
# or, without touching the launch command:
export JAVA_TOOL_OPTIONS="-javaagent:/otel/opentelemetry-javaagent.jar"
```

Configured purely by env (`OTEL_SERVICE_NAME`, `OTEL_EXPORTER_OTLP_ENDPOINT`, `OTEL_TRACES_SAMPLER`).

**Python** — install per-library *instrumentation* packages (e.g. `opentelemetry-instrumentation-fastapi`, `-asyncpg`, `-aiokafka`) and initialize the SDK, or use `opentelemetry-instrument` as a launcher wrapper for bootstrap-time patching.

## The OTel Collector

A standalone, vendor-agnostic relay that decouples apps from backends. Pipeline: **receive → process → export**.

- **Receivers** ingest telemetry (OTLP, Prometheus scrape, Jaeger, …).
- **Processors** batch, sample, filter, redact, and enrich (e.g. `batch`, `tail_sampling`, attribute scrubbing).
- **Exporters** ship to one or more backends.

Running a Collector means apps emit OTLP to one local endpoint; routing, sampling, and backend swaps happen in Collector config — apps never redeploy. This is where **instrument once, export anywhere** becomes operational.

## OTLP: the wire protocol

**OTLP** (OpenTelemetry Protocol) is the canonical transport for all three signals.

| Transport | Default port |
|-----------|--------------|
| gRPC      | 4317 |
| HTTP/protobuf | 4318 |

`OTEL_EXPORTER_OTLP_ENDPOINT` points a producer at a Collector or a native-OTLP backend. Because OTLP is standardized, any compliant producer talks to any compliant receiver.

## Semantic conventions

Standardized attribute names and units so telemetry is portable and queryable regardless of language or library — e.g. `http.request.method`, `http.response.status_code`, `db.system`, `messaging.system`, `service.name`. Conventions are what let a dashboard or alert written once work across every instrumented service; without them, attribute keys would be a free-for-all.

## In Nexus

- **nexus-api** (Kotlin/Ktor) uses the **OTel Java agent** via `-javaagent` — zero code changes; HTTP, JDBC, and Kafka producer spans appear automatically.
- **nexus-ingest** (Python/FastAPI) uses the **SDK + instrumentation libs** (FastAPI, aiokafka, asyncpg) plus a **manual `embed` span**.
- A document upload is traced end-to-end: `nexus-api` HTTP → JDBC → Kafka publish → **W3C `traceparent` in Kafka headers** → `nexus-ingest` consume → manual `embed` span → pgvector upsert. See [messaging_orchestrator.md](../messaging/messaging_orchestrator.md) · `[[messaging_orchestrator]]` for the header propagation.
- The `embed` span dominated the trace (~291 of 318 ms) — context propagation made that visible across the process boundary.
- Backend is **grafana/otel-lgtm** all-in-one: OTel Collector + **Tempo** (traces) + **Loki** (logs) + **Prometheus** (metrics) + **Grafana**. One container receives OTLP on 4317/4318 and exposes all three pillars in Grafana. See [metrics_red.md](metrics_red.md) · `[[metrics_red]]` and [structured_logging.md](structured_logging.md) · `[[structured_logging]]`.

## References

- Area orchestrator: [observability_orchestrator.md](observability_orchestrator.md) · `[[observability_orchestrator]]`
- Lesson that taught this: [lesson_11_observability.md](../../lessons/log/lesson_11_observability.md) · `[[lesson_11_observability]]`
