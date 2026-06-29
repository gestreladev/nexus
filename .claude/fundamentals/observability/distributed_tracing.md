---
name: distributed_tracing
description: How traces, spans, and W3C context propagation reconstruct one request's path across services and queues.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - debugging where latency is spent across multiple services
    - propagating trace context across HTTP or Kafka boundaries
metadata:
  type: reference
---
# Distributed tracing

A **trace** is the full causal tree of one logical operation (e.g. "upload a document"). A **span** is one unit of work inside it: an HTTP handler, a JDBC query, a Kafka publish, an embed call. Tracing exists to answer a question logs cannot: *where did the time go across services* — because a trace shares one id end-to-end and records durations as a hierarchy, while logs are isolated, unordered lines per process.

## The tree: trace_id + parent_span_id

Every span carries:

| Field | Meaning |
|---|---|
| `trace_id` | 16-byte id, identical for every span in the trace — the join key |
| `span_id` | 8-byte id unique to this span |
| `parent_span_id` | the `span_id` of the span that caused this one (empty = root) |
| `name` | operation label, e.g. `POST /documents`, `embed` |
| start / duration | wall-clock start and elapsed time |
| attributes | typed key/values: `http.method`, `db.system`, `messaging.system` |

Spans sharing a `trace_id` and chained by `parent_span_id` form a tree. Rendered by start time and indented by depth, it becomes a **waterfall** — siblings run sequentially or overlap; children nest under parents.

```
trace 4bf92f...  document upload (318ms)
POST /documents        [================================] 318ms  nexus-api
  INSERT documents     [==]                                12ms  (JDBC)
  kafka publish        [=]                                  6ms  (producer)
   ── traceparent in Kafka headers ──>
  consume doc.uploaded   [=]                                4ms  nexus-ingest
    embed                 [============================]  291ms  <-- the cost
    pgvector upsert       [==]                              9ms
```

The waterfall makes the bottleneck obvious: the `embed` span dominates (~291 of 318ms). No log line, however well-structured, would let you sum and attribute that across two processes.

## The hard part: context propagation

A trace only stays whole if the child process learns its parent's `trace_id` and `span_id`. Inside one process the SDK carries this implicitly (thread-local / async context). **Across a process boundary it must be serialized into the wire format and reconstructed on the other side.** That is context propagation.

### W3C `traceparent`

The standard carrier is one HTTP-style header, version-prefixed and hyphen-delimited:

```
traceparent: 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01
             ^version  ^trace_id (16 byte)        ^parent span_id   ^flags
```

- `00` — format version.
- `trace_id` — 32 hex chars; copied unchanged down the whole tree.
- `parent-id` — the **caller's** current `span_id`, which becomes the child's `parent_span_id`.
- `flags` — `01` = sampled (record & export), `00` = not sampled.

A sibling `tracestate` header carries vendor-specific state; `traceparent` alone is enough to stitch the tree.

### Inject and extract via TextMapPropagator

The `TextMapPropagator` is the codec. It has two operations against any string key/value carrier (HTTP headers, Kafka record headers, gRPC metadata):

- **inject** — on the **producer/caller**: read the active span's context, write `traceparent` into the outgoing carrier.
- **extract** — on the **consumer/callee**: read `traceparent` from the incoming carrier, rebuild the remote `SpanContext`, and start new spans as its children.

Because the propagator is carrier-agnostic, the same mechanism crosses HTTP and message queues — only the "carrier" object differs.

## Crossing Kafka

Kafka is asynchronous, so there is no live call stack to inherit from — the link must travel *inside the message*. Kafka records carry a headers map, which is exactly a text map:

1. **Producer**: at send time, inject `traceparent` (and `tracestate`) into the record's headers. The publishing span's `span_id` is stamped there.
2. Broker stores and forwards the record; headers ride along untouched.
3. **Consumer**: on receive, extract `traceparent` from the record headers, set it as the parent context, and open the processing span as a child — continuing the *same* trace across the queue, possibly seconds later and on a different host.

Without this, the consumer would start a brand-new orphan trace and the waterfall would break in two at the queue.

## In Nexus

A document upload is traced end-to-end across two runtimes: `nexus-api` (Kotlin/Ktor, instrumented by the **OTel Java agent** — zero code) handles the HTTP request, writes via JDBC, then publishes to Kafka. The Java agent injects `traceparent` into the Kafka record headers automatically. `nexus-ingest` (Python/FastAPI, **OTel SDK** + FastAPI/aiokafka/asyncpg instrumentation) consumes the record and **explicitly extracts** the `traceparent` from the headers to continue the same trace, then opens a manual `embed` span before the pgvector upsert. The unified trace lands in **Tempo** (via the OTel Collector in the `grafana/otel-lgtm` backend) and renders as one waterfall in Grafana — where the `embed` span is visibly the cost (~291 of 318ms). See [opentelemetry.md](opentelemetry.md) · `[[opentelemetry]]` for the agent/SDK split and [structured_logging.md](structured_logging.md) · `[[structured_logging]]` for how `trace_id` ties logs back to this trace.

## See also

- [three_pillars.md](three_pillars.md) · `[[three_pillars]]` — traces vs metrics vs logs
- [metrics_red.md](metrics_red.md) · `[[metrics_red]]` — aggregate rates/errors/durations, the complement to per-trace detail
- [messaging_orchestrator.md](../messaging/messaging_orchestrator.md) · `[[messaging_orchestrator]]` — Kafka headers and the producer/consumer model

## References

- [observability_orchestrator.md](observability_orchestrator.md) · `[[observability_orchestrator]]`
- [lesson_11_observability.md](../../lessons/log/lesson_11_observability.md) · `[[lesson_11_observability]]`
