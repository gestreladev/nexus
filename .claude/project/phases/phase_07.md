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
| Version | `v0.7.0` ⏳ |
| Lesson | 8 |
| Topics | message-brokers, kafka, rabbitmq, real-time-data, websockets, server-sent-events |
| Status | ⏳ planned |

## Planned deliverables
- [ ] Kafka in Docker Compose
- [ ] `nexus-api` produces ingestion events
- [ ] `nexus-ingest` consumes and processes (Observer/Command patterns)
- [ ] WebSocket/SSE for live document status

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
