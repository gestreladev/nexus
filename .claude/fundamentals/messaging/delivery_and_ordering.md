---
name: delivery_and_ordering
description: Delivery guarantees, idempotency, and ordering in messaging.
agent:
  role: messaging-specialist
  tier: standard
  weight: medium
  triggers:
    - reasoning about duplicate or out-of-order messages
    - making a consumer idempotent
metadata:
  type: reference
---

# Delivery & Ordering

## Delivery guarantees
| Guarantee | Meaning | Cost |
|---|---|---|
| **At-most-once** | may be lost, never duplicated | commit before processing |
| **At-least-once** | never lost, may be duplicated | commit *after* processing (default) |
| **Exactly-once** | never lost or duplicated | transactions; complex, slower |

Kafka's practical default is **at-least-once**: a consumer that crashes after
processing but before committing its offset will **re-receive** the message on
restart.

## Therefore: make consumers idempotent
Because duplicates happen, processing the same event twice must be **safe**:
- Use a natural key (`documentId`) and check current state before acting.
- Re-processing a document already `ready` → a no-op, not a double-embed.
- Or dedupe on an idempotency key.

This is why the `documents.status` State machine matters: it lets the consumer
ask "is this already done?" before working.

## Ordering
Order holds **only within a partition**. Key by the entity (`documentId`) so a
document's events stay ordered; across documents, order isn't guaranteed (and
usually doesn't matter).

## Producer durability
`acks=all` + an idempotent producer (Nexus uses both) means a published event is
durably replicated and not duplicated on producer retries.

## In Nexus
`nexus-ingest`'s handler is designed to be idempotent (re-consuming an event is
safe) — the foundation for the Phase 9 status-update + embedding step.

## References
- [messaging_orchestrator.md](messaging_orchestrator.md) · `[[messaging_orchestrator]]`
- [kafka.md](kafka.md) · `[[kafka]]`
- [state.md](../../designpatterns/kotlin/behavioral/state.md) · `[[state]]`
