---
name: https_tls
description: HTTPS = HTTP + TLS — encryption, authentication, integrity.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - understanding what TLS provides
    - deciding where TLS terminates
metadata:
  type: reference
---

# HTTPS & TLS

HTTPS is just HTTP carried over a **TLS**-encrypted channel. TLS provides three
guarantees:

| Property | Meaning |
|---|---|
| **Encryption** | nobody in the middle can read the traffic |
| **Authentication** | the server is who it claims (via its certificate) |
| **Integrity** | the data wasn't tampered with in transit |

## The handshake (simplified)
```
Client → "hello" + supported ciphers
Server → certificate (proves identity) + chosen cipher
both   → derive a shared session secret (asymmetric → symmetric)
        → all further traffic encrypted with the symmetric key
```
Asymmetric crypto bootstraps trust; symmetric crypto does the bulk work (fast).

## Where TLS terminates
In production you usually terminate TLS at the **reverse proxy** (Nginx/Caddy),
which then talks plain HTTP to the app server over a private network:

```
Internet → [TLS] → Nginx :443 → [plain HTTP] → nexus-api :8080
```

This offloads crypto from the app and centralizes certificate management. See
[web_servers.md](../web/web_servers.md) · `[[web_servers]]`.

## Why it matters in Nexus
Production traffic is HTTPS end-to-edge. Locally and between containers on a
private Docker network, plain HTTP is acceptable. Auth tokens must **only**
travel over TLS — a bearer token on plain HTTP is sniffable.

## References
- [http_orchestrator.md](http_orchestrator.md) · `[[http_orchestrator]]`
- [tcp_and_connections.md](../networking/tcp_and_connections.md) · `[[tcp_and_connections]]`
