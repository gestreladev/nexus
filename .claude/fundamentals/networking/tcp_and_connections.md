---
name: tcp_and_connections
description: TCP handshake, HTTP keep-alive, and connection pooling.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - understanding connection cost and reuse
    - reasoning about connection pools
metadata:
  type: reference
---

# TCP & Connections

## The handshake
A TCP connection is a socket pair `(client-ip:port ↔ server-ip:port)`. Opening
one costs a round trip:

```
SYN  →
     ← SYN-ACK
ACK  →            now the byte stream is open
```

With TLS, add another 1–2 round trips for the crypto handshake. So a "cold"
HTTPS request pays DNS + TCP + TLS before any HTTP.

## Keep-alive (connection reuse)
- **HTTP/1.0**: one TCP connection per request — pay the handshake every time.
- **HTTP/1.1** (`Connection: keep-alive`, default): the connection stays open
  and the next request reuses it.
- **HTTP/2**: many requests multiplex over one connection simultaneously.

```
HTTP/1.0:  [handshake] GET /a [close]   [handshake] GET /b [close]
HTTP/1.1:  [handshake] GET /a → GET /b → …                 [close later]
```

## Connection pooling
A pool keeps N connections open and lends them out, so each request skips the
handshake. This is the *same idea* as a DB connection pool — see
[orm_and_connection_pooling.md](../databases/orm_and_connection_pooling.md) · `[[orm_and_connection_pooling]]`.

## Why it matters in Nexus
Ktor/Netty handle keep-alive for inbound traffic. For **service-to-service**
calls (e.g. `nexus-api` → `nexus-ingest`), use a pooled HTTP client — reusing
TCP saves 10–100ms per call at scale. This was the Lesson 1 gap, now solid.

## References
- [networking_orchestrator.md](networking_orchestrator.md) · `[[networking_orchestrator]]`
- [how_the_internet_works.md](how_the_internet_works.md) · `[[how_the_internet_works]]`
