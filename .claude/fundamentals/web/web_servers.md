---
name: web_servers
description: Application server vs reverse proxy, and what each does.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - understanding the role of a web server / reverse proxy
    - deciding what sits in front of the app
metadata:
  type: reference
---

# Web Servers

A web server listens on a port, accepts TCP connections, parses HTTP, and
returns responses. Two distinct roles often get conflated:

| Role | Does | Examples |
|---|---|---|
| **Application server** | runs your business logic | Ktor, Spring Boot, FastAPI |
| **Reverse proxy** | sits in front, forwards to app | Nginx, Caddy, Traefik |

## Why a reverse proxy
You rarely expose the app server directly to the internet:

```
Internet → Nginx :443 (TLS, rate-limit, compression, load-balance)
              → nexus-api :8080 (plain HTTP, business logic only)
```

The proxy handles cross-cutting infra concerns; the app handles only logic. It
also load-balances across multiple app instances — which works precisely because
the app is stateless (see idempotency_and_statelessness).

## Engines
Ktor runs on a pluggable engine; Nexus uses **Netty** — non-blocking,
high-concurrency, the production default on the JVM.

## Why it matters in Nexus
`nexus-api` is the application server. Local/dev runs it directly on :8080, and
Phase 8 containerized the whole system (one `docker compose up`). The reverse
proxy (Nginx/Caddy) was **deferred past Phase 8** to a later infra phase — taught
as a concept there, not yet built.

## References
- [web_orchestrator.md](web_orchestrator.md) · `[[web_orchestrator]]`
- [https_tls.md](../http/https_tls.md) · `[[https_tls]]`
