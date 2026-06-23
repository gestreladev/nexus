---
name: lesson_08_messaging
description: Lesson 8 log — Kafka producer (nexus-api) → consumer (nexus-ingest).
agent:
  role: tutor
  tier: nano
  weight: soft
  triggers:
    - reviewing what Lesson 8 covered
    - recapping messaging/Kafka before a session
metadata:
  type: reference
---

# Lesson 8 — Async & Messaging (Phase 7)

| Field | Value |
|---|---|
| Phase | 7 — Async & Messaging |
| Roadmap topics | message-brokers, kafka, rabbitmq, real-time-data, websockets, server-sent-events |
| Deliverable | Kafka event flow: nexus-api produces → nexus-ingest consumes |
| Milestone | `v0.7.0` |
| Status | ✅ mastery pass |

## Concepts taught
Why a broker (decouple, resilience, responsiveness, scale) vs sync HTTP; the
status column reflects async state (not latency control); Kafka in 5 terms
(topic, partition, consumer group, offset, at-least-once); idempotent consumers;
key-ordering; queue (RabbitMQ) vs log (Kafka); WS/SSE/polling for client real-time.

## Exercises (recap)
| Q | Topic | Verdict |
|---|---|---|
| R1 | hints enforced by mypy (static checker) | ✅ closed |
| R2 | contract = the event JSON schema (Kafka is the pipe) | 🟡 sharpened |
| R3 | produce + 201 over sync HTTP: resilience + responsiveness | ✅ |
| FU | status column = reflects async state, not latency control | corrected |

## Built (verified end-to-end)
- Kafka (`confluentinc/cp-kafka:7.8.0`, KRaft) in docker-compose
- nexus-api **producer**: `KafkaFactory` + `DocumentEventPublisher`, `kafka()`
  pipeline stage, `POST /v1/documents` (create pending + publish, keyed by id,
  acks=all, idempotent) + `GET /v1/documents/{id}` (owner-only, 403 otherwise)
- nexus-ingest **consumer**: `aiokafka` `DocumentConsumer` via FastAPI lifespan
- **Proven:** POST in Kotlin → `Published … document.uploaded-2@1` → Python
  `consumed document.uploaded id=… title=…`. Both test suites green; CI gains Kafka.
- Fixed a latent bug: `documents.status` PG enum → VARCHAR (V4) so inserts work.

## Gaps to revisit
- Consumer currently logs only; status update + embedding is Phase 9.

## References
- [lesson_07_python_service.md](lesson_07_python_service.md) · `[[lesson_07_python_service]]`
- [messaging_orchestrator.md](../../fundamentals/messaging/messaging_orchestrator.md) · `[[messaging_orchestrator]]`
