---
name: how_the_internet_works
description: The request lifecycle and network layers behind an API call.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - understanding what happens when a client calls the API
    - reviewing the network layers
metadata:
  type: reference
---

# How the Internet Works

## The request lifecycle
What happens when a client hits `api.nexus.dev`:

```
Client
 ├─ DNS lookup       api.nexus.dev → 203.0.113.42
 ├─ TCP handshake    SYN → SYN-ACK → ACK   (reliable byte stream)
 ├─ TLS handshake    cert exchange → shared secret → encrypted channel
 ├─ HTTP request     GET /v1/health  (sent over the channel)
 └─ Response         server replies; connection may be reused (keep-alive)
```

## The layers (systems view)
Think of it as abstractions over sockets:

| Layer | Unit | Concern |
|---|---|---|
| Link | frame | physical/MAC, local network |
| Internet (IP) | packet | addressing + routing between hosts |
| Transport (TCP/UDP) | segment | reliable stream (TCP) vs datagrams (UDP) |
| Application (HTTP) | message | what your API actually speaks |

Each layer wraps the one above in its own header — like nested structs. A
backend dev lives at the Application layer but benefits from knowing the stack:
latency, retries, and connection reuse all originate below HTTP.

## Why it matters in Nexus
Every `/v1/*` call pays DNS + TCP + (TLS) before a byte of HTTP. Understanding
this is why connection pooling (TCP reuse) and keep-alive matter for
service-to-service calls — see [tcp_and_connections.md](tcp_and_connections.md) · `[[tcp_and_connections]]`.

## References
- [networking_orchestrator.md](networking_orchestrator.md) · `[[networking_orchestrator]]`
- [dns.md](dns.md) · `[[dns]]`
- [lesson_01_foundations.md](../../lessons/log/lesson_01_foundations.md) · `[[lesson_01_foundations]]`
