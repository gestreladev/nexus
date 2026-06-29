---
name: structured_logging
description: JSON logs with embedded trace context so logs correlate with traces in an observability stack.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - choosing a log format or wiring app logs into Loki/Grafana
    - making logs correlate with traces via trace_id/span_id
    - deciding log levels, what to log, or sampling policy
metadata:
  type: reference
---

# Structured logging

A log line is data, not prose. **Structured logging** emits each event as a map of
key/value fields (almost always JSON) instead of a free-text sentence. The shift is
from *human reads one line* to *machine queries millions of lines*.

## Free-text vs structured

```
# dev text / logback pattern — readable, unqueryable
14:02:11.限 INFO  c.n.UploadHandler - uploaded doc 8821 for tenant acme in 318ms

# structured — same event, queryable fields
{"ts":"2026-06-28T14:02:11Z","level":"INFO","msg":"document uploaded",
 "doc_id":8821,"tenant":"acme","duration_ms":318,"trace_id":"a1b2…","span_id":"c3d4…"}
```

Free-text forces brittle regex/grep to extract a number. Structured logs let the
backend index `tenant`, `duration_ms`, `trace_id` as first-class fields:
`{tenant="acme"} | duration_ms > 300` is a query, not a grep.

## Why it wins

| Property | Payoff |
|---|---|
| Machine-parseable | No regex; the ingester reads fields directly |
| Queryable fields | Filter/aggregate by `level`, `tenant`, `route`, latency |
| Stable schema | Dashboards and alerts don't break when wording changes |
| Correlatable | Embedded `trace_id`/`span_id` link a log to its trace |

## The big win: trace correlation

The reason structured logging matters in an observability stack is **correlation**.
When every log line carries the active `trace_id` and `span_id`, the three pillars
join up: you click a slow span in a trace and jump straight to the exact log lines
emitted while it ran — and back from a log line to the full request trace.

The trace/span IDs come from the same context that [opentelemetry.md](opentelemetry.md) ·
`[[opentelemetry]]` propagates (see [distributed_tracing.md](distributed_tracing.md) ·
`[[distributed_tracing]]`). The logging layer's only job is to read the *current*
context and stamp those IDs onto every event. This is the glue that makes the
[[three_pillars]] ([three_pillars.md](three_pillars.md)) one investigation instead of three.

## Log levels

Use levels as a query and alerting axis, not decoration.

| Level | Meaning |
|---|---|
| ERROR | A request/operation failed; needs attention |
| WARN | Degraded but handled (retry, fallback, near a limit) |
| INFO | Notable business events (upload accepted, embed done) |
| DEBUG | Developer detail; off in prod or sampled |

Default production threshold is INFO. DEBUG behind a flag or sampled — it is the
bulk of volume and cost.

## What to log — and what not to

Log: the event message, stable identifiers (`doc_id`, `tenant`, `route`,
`status`), outcomes, durations, and the trace/span IDs.

**Never log secrets or PII.** No passwords, tokens, API keys, full auth headers,
raw request bodies, emails, or document *contents*. Log the document **id**, never
its text. Redact at the source; a leaked field is forever once it lands in Loki.

## Sampling

High-volume logs cost storage and money. Keep all ERROR/WARN; **sample** the
chatty success path (e.g. keep 1 in N INFO/DEBUG). Prefer **tail/trace-based**
sampling that keeps logs for traces that were slow or errored — so the lines you
kept are the ones tied to interesting traces.

## In Nexus

Nexus replaces the dev text/logback pattern with JSON logs carrying OTel trace
context. **nexus-api** (Kotlin/Ktor, instrumented by the zero-code OTel Java agent)
and **nexus-ingest** (Python/FastAPI, OTel SDK) both emit JSON with `trace_id`/
`span_id` from the active span. An upload trace spans nexus-api HTTP → JDBC → Kafka
→ nexus-ingest consume → the manual `embed` span (~291 of 318ms) → pgvector upsert
(W3C `traceparent` carried in Kafka headers — see [messaging_orchestrator.md](../messaging/messaging_orchestrator.md) ·
`[[messaging_orchestrator]]`). Logs ship to **Loki**, traces to **Tempo**, both in
the `grafana/otel-lgtm` all-in-one; in Grafana, a slow `embed` span links straight
to its log lines via the shared `trace_id`. **Phase 10 Session B** wires this
correlation end-to-end. Latency-side complement: [metrics_red.md](metrics_red.md) ·
`[[metrics_red]]`.

## References

- Area orchestrator: [observability_orchestrator.md](observability_orchestrator.md) · `[[observability_orchestrator]]`
- Taught in: [lesson_11_observability.md](../../lessons/log/lesson_11_observability.md) · `[[lesson_11_observability]]`
