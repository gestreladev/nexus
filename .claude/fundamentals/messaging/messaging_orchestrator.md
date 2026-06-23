---
name: messaging_orchestrator
description: Routing for async messaging fundamentals — brokers, Kafka, delivery, real-time.
agent:
  role: messaging-specialist
  tier: standard
  weight: medium
  triggers:
    - designing async/event-driven flow
    - choosing a broker or delivery guarantee
metadata:
  type: orchestrator
---

# Messaging — Fundamentals

Async communication between services via a **message broker**: producers publish
events; consumers read at their own pace. Decouples services, absorbs load, and
survives outages — at the cost of **eventual consistency**. Taught in Lesson 8.
Model selection: [model_decision.md](../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Why a broker (vs a synchronous call)
| Benefit | What it buys |
|---|---|
| Decoupling | producer doesn't know/care who consumes |
| Resilience | events wait if a consumer is down; nothing lost |
| Responsiveness | API returns fast; slow work happens off-path |
| Scale | a consumer group spreads load across instances |

Trade-off: the result isn't ready when the API responds — track it with a status
field (the State machine).

## Routing Table
| Topic | Scope | Document |
|---|---|---|
| Message brokers | queues vs logs; RabbitMQ vs Kafka | [message_brokers.md](message_brokers.md) · `[[message_brokers]]` |
| Kafka | topics, partitions, consumer groups, offsets | [kafka.md](kafka.md) · `[[kafka]]` |
| Delivery & ordering | at-least-once, idempotency, key-ordering | [delivery_and_ordering.md](delivery_and_ordering.md) · `[[delivery_and_ordering]]` |
| Real-time | WebSockets, SSE, polling | [realtime_websockets_sse.md](realtime_websockets_sse.md) · `[[realtime_websockets_sse]]` |

## References
- [fundamentals_orchestrator.md](../fundamentals_orchestrator.md) · `[[fundamentals_orchestrator]]`
- [observer.md](../../designpatterns/kotlin/behavioral/observer.md) · `[[observer]]`
- [command.md](../../designpatterns/kotlin/behavioral/command.md) · `[[command]]`
