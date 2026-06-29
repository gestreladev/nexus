---
name: metrics_red
description: RED and USE methods for service metrics, metric types, and why latency must be a histogram.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - choosing what metrics to expose for a service
    - debugging slow tail latency or wrong percentile dashboards
    - deciding between Prometheus scrape and OTLP push
metadata:
  type: reference
---

# Metrics: RED and USE

Metrics are cheap, aggregatable numbers sampled over time. Where a trace explains one request and a log records one event, a metric answers "how is the whole system doing right now?" at near-zero cost. This is Nexus Phase 10, Session B.

## RED — for services (request-driven)

Instrument every service that handles requests with three signals:

| Signal | Question | Typical metric |
|--------|----------|----------------|
| **Rate** | How many requests/sec? | counter, rate'd over time |
| **Errors** | How many are failing? | counter, split by status |
| **Duration** | How long do they take? | **histogram** (see below) |

RED is the demand-side view: it tracks what users experience. If Rate drops, Errors spike, or Duration's tail grows, something is wrong.

## USE — for resources (supply-side)

For a finite resource (CPU, memory, disk, connection pool, Kafka consumer lag):

- **Utilization** — % of time the resource was busy.
- **Saturation** — how much work is queued/waiting (the most predictive early-warning signal).
- **Errors** — error events (e.g. failed allocations, dropped packets).

RED tells you the service is slow; USE tells you *which resource* is the bottleneck. Use them together.

## Metric types

| Type | Behavior | Examples |
|------|----------|----------|
| **Counter** | monotonically increases; reset on restart. Query as a *rate*. | requests_total, errors_total |
| **Gauge** | goes up and down; a snapshot value. | queue_depth, mem_bytes, consumer_lag |
| **Histogram** | buckets observations into ranges; emits per-bucket counts + sum + count. | request_duration_seconds |

A counter alone gives throughput. A gauge gives a level. Only a histogram lets you recover the *distribution*, which is what latency requires.

## Why latency MUST be a histogram

Latency is not a single number — it is a distribution, and the part users feel is the **tail**. The average actively lies about it.

Consider 100 requests: 99 take 20 ms, one takes 8000 ms.

```
mean  = (99*20 + 8000) / 100 ≈ 99 ms     # looks "fine"
p50   = 20 ms                            # half are this fast
p99   = 8000 ms                          # 1 in 100 users waits 8s
```

The 8 s request barely moves the mean — it is diluted by the fast majority — but **p99 screams**. If your dashboard shows only the average, that 8 s outage is invisible. Yet at 1000 req/s, p99 = 8 s means ~10 users every second are having a terrible experience.

A histogram pre-buckets durations at collection time, so the backend can compute percentiles (p50/p95/p99) by interpolating across buckets. You cannot average pre-aggregated averages to get a percentile — the raw distribution is gone. That is why **Duration is a histogram, not a gauge of "avg latency."**

Rules of thumb:
- Alert and SLO on **p95/p99**, not the mean.
- Choose bucket boundaries around your SLO (e.g. 50 ms, 100 ms, 250 ms, 500 ms, 1 s) so the percentile you care about lands between buckets, not above the top one.
- Watch the gap between p50 and p99: a widening gap is tail-latency degradation even when the median looks healthy.

## Prometheus scrape vs OTLP push

| | Prometheus scrape (**pull**) | OTLP **push** |
|--|------------------------------|---------------|
| Direction | Backend pulls `/metrics` on an interval | App/agent pushes to a collector |
| Discovery | Backend needs service discovery | App needs collector endpoint |
| Liveness | A missed scrape = target is down (free up signal) | Must infer liveness separately |
| Fit | Long-lived services, stable topology | Short-lived jobs, OTel-native stacks |

Both can coexist: instrument with OpenTelemetry, push OTLP to the Collector, and let the Collector expose a Prometheus endpoint (or remote-write) so existing scrape-based dashboards keep working.

## In Nexus

`nexus-api` (Kotlin/Ktor, OTel Java agent) and `nexus-ingest` (Python/FastAPI, OTel SDK) both push **OTLP** to the `grafana/otel-lgtm` Collector, which feeds **Prometheus** for metrics (and Tempo/Loki for traces/logs). Grafana renders RED dashboards from the histogram-backed `http.server.duration`. The same upload that traced end-to-end — where the manual `embed` span dominated (~291 of 318 ms) — shows up in metrics as a fat upper tail: the mean upload looks acceptable, but p99 is pinned by embedding. The trace explains *why*; the histogram is what makes the tail *visible* in the first place.

## References

- Area orchestrator: [observability_orchestrator.md](observability_orchestrator.md) · `[[observability_orchestrator]]`
- Siblings: [three_pillars.md](three_pillars.md) · `[[three_pillars]]`, [opentelemetry.md](opentelemetry.md) · `[[opentelemetry]]`, [distributed_tracing.md](distributed_tracing.md) · `[[distributed_tracing]]`, [structured_logging.md](structured_logging.md) · `[[structured_logging]]`
- Lesson: [lesson_11_observability.md](../../lessons/log/lesson_11_observability.md) · `[[lesson_11_observability]]`
