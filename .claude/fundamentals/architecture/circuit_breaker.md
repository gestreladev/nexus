---
name: circuit_breaker
description: Stability pattern that trips OPEN on a failing dependency to fail fast and prevent cascading failures.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - A slow or failing remote dependency is causing cascading failures or thread/connection exhaustion.
    - Choosing resilience patterns (timeouts, retries, bulkheads, fallbacks) for an outbound call.
metadata:
  type: reference
---
# Circuit Breaker

## Problem
A remote dependency (DB, downstream service, third-party API) becomes slow or fails. Callers keep issuing requests — and worse, **retry** them. Each in-flight call holds a thread, connection, and memory while it waits on a timeout. The pool drains, latency spreads upstream, and one sick dependency drags down healthy callers. This is a **cascading failure**: the fault propagates against the call graph until the whole system is degraded.

## The pattern
Wrap the remote call in a stateful proxy — the breaker — that watches outcomes. When failures cross a threshold, the breaker **trips OPEN** and short-circuits: subsequent calls return *immediately* with an error or a fallback, never touching the ailing dependency. This does two things at once:
1. **Fails fast** — callers stop blocking on doomed calls, so resources are freed instead of exhausted.
2. **Sheds load** — the dependency gets breathing room to recover instead of being hammered while down.

After a cooldown the breaker probes for recovery and closes again. The breaker turns a slow, resource-eating failure into a fast, cheap one.

## Three states

```
        failures >= threshold
  CLOSED ───────────────────────▶ OPEN
    ▲                              │
    │ probe succeeds               │ cooldown elapsed
    │                              ▼
    └──────────── HALF-OPEN ◀──────┘
                     │
                     └─ probe fails ─▶ OPEN
```

| State | Behavior | Exit |
|-------|----------|------|
| **CLOSED** | Calls pass through; failures counted (rate or consecutive count). | Threshold breached → OPEN |
| **OPEN** | Calls rejected instantly (fail fast / fallback); dependency untouched. | Cooldown timer elapses → HALF-OPEN |
| **HALF-OPEN** | A limited number of trial calls allowed through to test recovery. | Trials succeed → CLOSED; any fails → OPEN |

The HALF-OPEN gate is what prevents a "thundering herd" the instant cooldown ends: only a probe or two is admitted, not the full backlog.

## Tuning knobs
- **Failure threshold** — error rate (e.g. ≥50% over a sliding window) or N consecutive failures.
- **Cooldown / wait duration** — how long OPEN holds before probing.
- **Half-open permits** — how many trial calls are allowed.
- **What counts as failure** — exceptions, timeouts, and 5xx; usually *not* 4xx caller errors.

## Companion patterns
The breaker is one layer in a defense stack — it does not replace the others:

- **Timeouts** — bound every remote call. Without a timeout the breaker rarely sees a "failure" in time; a hung call just holds a thread forever. Timeouts are what *make* a slow dependency look like a failure.
- **Retries with backoff + jitter** — recover from transient blips, but they *amplify* load on a struggling dependency. Cap attempts, space them with exponential backoff, and add **jitter** (randomized delay) so retries from many callers don't align into synchronized waves. Pair retries *inside* a breaker so a tripped breaker stops the retry storm.
- **Bulkheads** — isolate resources (separate thread/connection pools per dependency) so one saturated dependency can't starve calls to the others. The breaker stops the bleeding; the bulkhead contains the blast radius.
- **Fallback** — a degraded-but-useful response (cached value, default, empty list) returned while OPEN, so the caller stays functional.

Mental model: **timeout** detects, **breaker** decides, **bulkhead** contains, **retry+backoff** recovers, **fallback** degrades gracefully.

## Where it lives
- **In-app library** — co-located with the call, language-native. On the JVM, **Resilience4j** (functional, lightweight; successor to Netflix Hystrix) composes breaker + retry + bulkhead + timelimiter as decorators. Other ecosystems have Polly (.NET), gobreaker (Go), etc.
- **Service mesh** — a sidecar proxy (Envoy) enforces outlier detection and breaking at the infra layer, transparent to app code. Centralized policy, no per-language library, but coarser than in-app context. See [service_mesh.md](service_mesh.md) · `[[service_mesh]]`.

Library = fine-grained, business-aware fallbacks. Mesh = uniform, language-agnostic policy. They can coexist.

## In Nexus
A **concept for now**, not yet wired in. The async path already provides structural decoupling: the API publishes to **Kafka** and returns, so a slow or failing downstream **consumer** does not block or fail the ingestion request — the broker buffers, and the consumer drains at its own pace. That backpressure-by-design covers the highest-risk path today. A breaker becomes worthwhile once Nexus makes a **synchronous** outbound call (an external API, a downstream service) where failing fast matters. Until then, document the intent, don't add the dependency. See the messaging design at [messaging_orchestrator.md](../messaging/messaging_orchestrator.md) · `[[messaging_orchestrator]]`.

## References
- Architecture orchestrator: [architecture_fundamentals_orchestrator.md](architecture_fundamentals_orchestrator.md) · `[[architecture_fundamentals_orchestrator]]`
- Sibling: [architectural_patterns.md](architectural_patterns.md) · `[[architectural_patterns]]`
- Sibling: [service_mesh.md](service_mesh.md) · `[[service_mesh]]`
- Async decoupling: [messaging_orchestrator.md](../messaging/messaging_orchestrator.md) · `[[messaging_orchestrator]]`
