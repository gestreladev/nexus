---
name: three_pillars
description: Logs, metrics, and traces — three complementary observability signals, what each answers, and their cost/cardinality tradeoffs.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - choosing which signal (log/metric/trace) answers a given operational question
    - explaining why a healthcheck passing does not mean the system works well
    - reasoning about cardinality, cost, or observability vs monitoring
metadata:
  type: reference
---

# Three pillars of observability

Logs, metrics, and traces are the three primary telemetry signals. They are
**complementary, not redundant**: each answers a class of question the others
structurally cannot. A single log line is one point in space and time — it
cannot show aggregate trends (that's metrics) and cannot show one request's
path across services (that's traces).

## The three signals

### Logs — "what happened, exactly?"
A timestamped, immutable record of a discrete event. Highest fidelity, richest
context (stack traces, payloads, IDs), but **one point per event**. You cannot
ask "what's the p99 over the last hour?" of raw logs without scanning and
aggregating them — expensive and slow. Prefer **structured** logs (key/value,
not free text) so they're queryable. See `[[structured_logging]]`.

- Shape: discrete event records, often `{timestamp, level, message, fields...}`.
- Cost driver: **volume** (every event stored). High retention cost.

### Metrics — "how much / how many, over time?"
A numeric measurement aggregated over a time window: counters, gauges,
histograms. Cheap, dense, and pre-aggregated, so they answer trend and
threshold questions (rate, error %, latency percentiles) instantly. The cost
is **cardinality**: each unique combination of label values is a separate time
series. A `user_id` label with a million values = a million series = a storage
and query explosion. Keep labels low-cardinality (status, route, method). See
RED method in `[[metrics_red]]`.

- Shape: `(name, labels{}, value, timestamp)` time series.
- Cost driver: **cardinality** (label-value combinations), not raw volume.

### Traces — "where did this one request go, and what was slow?"
A trace is a tree of spans following a **single request** across service
boundaries, with causal parent/child links and per-span timing. It answers
"which hop dominated latency?" and "which service failed in this flow?" — the
one-request, cross-service view neither logs nor metrics give. Propagated via
context (W3C `traceparent`). See `[[distributed_tracing]]` and `[[opentelemetry]]`.

- Shape: trace = tree of spans; span = `{trace_id, span_id, parent, start, end, attrs}`.
- Cost driver: **volume × span count**; usually **sampled** to control cost.

## Comparison

| Aspect | Logs | Metrics | Traces |
|---|---|---|---|
| Question answered | What happened? | How much, over time? | Where did one request go? |
| Granularity | Per event | Per time window (aggregated) | Per request, per span |
| Data shape | Event records | Time series | Span trees |
| Aggregate trends | No (must scan) | Yes (native) | No |
| One request's path | Partial (if correlated) | No | Yes |
| Primary cost driver | Volume / retention | Cardinality | Volume × spans (sample) |
| Typical sampling | Rarely | Never (pre-aggregated) | Often |

The diagonal is the point: each signal is strong exactly where the others are
weak. Correlate them with shared IDs (`trace_id` in logs, exemplars on metrics)
to pivot between aggregate → request → event.

## Observability vs monitoring

- **Monitoring** answers **known-unknowns**: questions you predefined —
  dashboards and alerts on metrics you knew to collect ("is error rate > 1%?").
- **Observability** is the property of being able to answer **unknown-unknowns**:
  novel questions about states you never anticipated, asked *after* the
  incident, by exploring high-cardinality telemetry (especially traces and
  structured logs) without shipping new code.

Monitoring tells you *that* something is wrong; observability lets you ask *why*.

### "Up" ≠ "working well"
A liveness/healthcheck endpoint returning `200 OK` proves the process is
running and can answer one trivial request. It says nothing about correctness
or quality of service: requests can be 10× slower than normal, a downstream
dependency can be failing, a queue can be backing up, or 5% of responses can be
wrong — all while `/health` stays green. "Up" is a binary liveness signal;
"working well" is a distribution (latency percentiles, error rate, saturation)
that only metrics + traces + logs together can describe.

## In Nexus

A document upload is traced end-to-end across services: nexus-api (Kotlin/Ktor,
instrumented by the **OTel Java agent**, zero code) handles HTTP → JDBC → Kafka
publish; the W3C `traceparent` rides in Kafka headers to nexus-ingest
(Python/FastAPI, **OTel SDK** with FastAPI/aiokafka/asyncpg instrumentation),
which consumes, runs a manual `embed` span, and upserts to pgvector. The trace
revealed the `embed` span dominated: **~291 of 318 ms**. A metric alone would
show "uploads are slow"; only the trace pinpointed *which span* across two
services. All three signals land in **grafana/otel-lgtm** (Tempo for traces,
Loki for logs, Prometheus for metrics, Grafana to view). Kafka context
propagation: see `[[messaging_orchestrator]]`.

## References

- Area orchestrator: [observability_orchestrator.md](observability_orchestrator.md) · `[[observability_orchestrator]]`
- Lesson that taught this: [lesson_11_observability.md](../../lessons/log/lesson_11_observability.md) · `[[lesson_11_observability]]`
- Siblings: `[[structured_logging]]` · `[[metrics_red]]` · `[[distributed_tracing]]` · `[[opentelemetry]]`
