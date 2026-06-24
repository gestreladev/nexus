---
name: kafka
description: Kafka core concepts — topics, partitions, consumer groups, offsets.
agent:
  role: messaging-specialist
  tier: standard
  weight: medium
  triggers:
    - working with Kafka topics/partitions/consumers
    - reasoning about ordering and scale
metadata:
  type: reference
---

# Kafka

Kafka is a distributed, append-only **log** of events. Five concepts run the show:

| Term | Meaning |
|---|---|
| **Topic** | a named stream of events (`document.uploaded`) |
| **Partition** | a topic is split into partitions for parallelism; **order is guaranteed only within a partition** |
| **Producer** | appends records to a topic; a record has key + value |
| **Consumer group** | consumers sharing a `group.id` **split** the partitions → one partition per consumer in the group |
| **Offset** | each consumer's position per partition; *committing* it = "processed up to here" |

## Keys → ordering
The record **key** decides the partition (`hash(key) % partitions`). Keying by
`documentId` means all events for one document land on the same partition, so
they're processed **in order**. Different documents spread across partitions for
parallelism.

## Consumer groups → scale
Add consumers to the same group and Kafka **rebalances** partitions across them
(one partition → one consumer in the group). N partitions cap parallelism at N
consumers. A *second* group reading the same topic gets its own full copy of the
stream (independent offsets).

## KRaft
Modern Kafka runs in **KRaft** mode — no ZooKeeper; the broker also acts as the
metadata controller. Nexus runs a single-node KRaft broker in Compose.

## In Nexus
`nexus-api` produces `document.uploaded` keyed by `documentId` (acks=all,
idempotent producer). `nexus-ingest` consumes in group `nexus-ingest`.

## References
- [messaging_orchestrator.md](messaging_orchestrator.md) · `[[messaging_orchestrator]]`
- [delivery_and_ordering.md](delivery_and_ordering.md) · `[[delivery_and_ordering]]`
