---
name: phase_07
description: Phase 7 tracker — async & messaging (Kafka, WebSockets).
agent:
  role: progress-tracker
  tier: nano
  weight: soft
  triggers:
    - reviewing or planning Phase 7
metadata:
  type: reference
---

# Phase 7 — Async & Messaging

| Field | Value |
|---|---|
| Version | `v0.7.0` 🔄 in progress |
| Lesson | 8 |
| Topics | message-brokers, kafka, rabbitmq, real-time-data, websockets, server-sent-events |
| Status | 🔄 producer→consumer flow shipped; consumer status-update + WS/SSE pending |

## Deliverables
- [x] Kafka (`cp-kafka`, KRaft) in Docker Compose + CI service
- [x] `nexus-api` produces `document.uploaded` on `POST /v1/documents` (keyed, acks=all)
- [x] `nexus-ingest` consumes via `aiokafka` (lifespan-managed, idempotent design)
- [x] `GET /v1/documents/{id}` (owner-only, 403) for status polling
- [ ] Consumer updates `documents.status` + embeds (Phase 9)
- [ ] WebSocket/SSE for live document status (later)

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
- [messaging_orchestrator.md](../../fundamentals/messaging/messaging_orchestrator.md) · `[[messaging_orchestrator]]`
