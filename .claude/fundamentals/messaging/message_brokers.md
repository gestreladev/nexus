---
name: message_brokers
description: Message brokers — queues vs logs, RabbitMQ vs Kafka.
agent:
  role: messaging-specialist
  tier: standard
  weight: soft
  triggers:
    - choosing a message broker
    - queue vs log semantics
metadata:
  type: reference
---

# Message Brokers

A broker sits between services and carries messages. Two families:

## Queue (e.g. RabbitMQ)
A message is delivered to **one** consumer and then **removed**. Great for task
distribution / work queues. Rich routing (exchanges, bindings). Once consumed,
it's gone — you can't "replay" history.

## Log (e.g. Kafka)
An **append-only, retained log**. Messages stay for a retention window;
**multiple** independent consumer groups can read the same stream at their own
offsets, and can **replay** from the past. Built for high-throughput event
streaming and multiple downstream consumers.

| | Queue (RabbitMQ) | Log (Kafka) |
|---|---|---|
| After consume | removed | retained (replayable) |
| Consumers | competing on one queue | many groups, independent offsets |
| Best for | task/work distribution | event streaming, multiple readers |
| Ordering | per-queue | per-partition |

## Choosing for Nexus
Nexus uses **Kafka**: the document lifecycle is an *event* others may also want
(ingest now; analytics/search later), and replay/retention is valuable. A pure
work-queue (RabbitMQ) would also work for the single ingest consumer, but Kafka
keeps options open.

## The contract
Whatever the broker, the integration contract is the **message JSON schema** —
swappable brokers, stable message shape. (See the document.uploaded event.)

## References
- [messaging_orchestrator.md](messaging_orchestrator.md) · `[[messaging_orchestrator]]`
- [kafka.md](kafka.md) · `[[kafka]]`
