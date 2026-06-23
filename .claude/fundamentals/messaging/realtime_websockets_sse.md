---
name: realtime_websockets_sse
description: Real-time delivery to clients — polling, SSE, WebSockets.
agent:
  role: messaging-specialist
  tier: standard
  weight: soft
  triggers:
    - pushing live updates to a client
    - choosing polling vs SSE vs WebSockets
metadata:
  type: reference
---

# Real-time: Polling, SSE, WebSockets

Kafka decouples *services*. To get an update to the **client** (e.g. "your
document is ready"), pick a client-facing channel:

| Technique | Direction | Use |
|---|---|---|
| **Polling** | client asks repeatedly | simplest; `GET /v1/documents/{id}` until `ready` |
| **Long polling** | server holds the request until there's news | fewer requests than naive polling |
| **SSE** (Server-Sent Events) | server → client, one-way stream over HTTP | live status/notifications; auto-reconnect |
| **WebSockets** | full duplex, both directions | chat, collaborative editing, live cursors |

## Choosing
- **One-way updates** (status, notifications) → **SSE** — simpler than WebSockets,
  rides plain HTTP, built-in reconnect.
- **Two-way / interactive** → **WebSockets**.
- **Occasional check** → **polling** is fine; don't over-engineer.

## How it connects to the broker
A service consumes the Kafka event, then **fans it out** to connected clients
over SSE/WebSocket. The broker handles service-to-service; SSE/WS handles
service-to-client. Different layers, composed.

## In Nexus
Today the client **polls** `GET /v1/documents/{id}` for status. A later
enhancement: `nexus-api` consumes a `document.ready` event and pushes it to the
client via SSE — no polling.

## References
- [messaging_orchestrator.md](messaging_orchestrator.md) · `[[messaging_orchestrator]]`
- [http_basics.md](../http/http_basics.md) · `[[http_basics]]`
