---
name: http_caching
description: HTTP-level caching — Cache-Control, ETag, conditional requests.
agent:
  role: caching-specialist
  tier: standard
  weight: soft
  triggers:
    - setting cache headers on a response
    - enabling conditional GET / 304
metadata:
  type: reference
---

# HTTP Caching

Caching at the protocol layer — browsers, CDNs, and proxies cache responses so
requests never reach `nexus-api`. Controlled by response headers.

## Cache-Control
```
Cache-Control: no-store              # never cache (auth, sensitive)
Cache-Control: private, max-age=60   # per-user, cache 60s in the browser
Cache-Control: public, max-age=3600  # shared caches/CDN may cache 1h
```
- `no-store` — for anything sensitive (auth responses, `/me`).
- `private` — only the end client may cache (per-user data).
- `public` + `max-age` — CDN-cacheable static/immutable content.

## Validation — ETag + conditional GET
For data that changes occasionally, let clients revalidate cheaply:
```
# response
ETag: "v3-abc123"
# next request
If-None-Match: "v3-abc123"
# server, if unchanged → 304 Not Modified (no body)
```
A `304` saves bandwidth: the client reuses its copy; the server sends only headers.

## Where it fits vs Redis
- **HTTP caching** — edge/client side; cuts requests *before* they reach the app.
- **Redis cache-aside** — server side; cuts DB load *within* the app.
They compose: a CDN absorbs anonymous reads; Redis absorbs per-user/db reads.

## In Nexus
Auth endpoints send `Cache-Control: no-store`. Future public document metadata
can use `ETag` + `max-age`. (See conventions for response shape.)

## References
- [caching_orchestrator.md](caching_orchestrator.md) · `[[caching_orchestrator]]`
- [http_basics.md](../http/http_basics.md) · `[[http_basics]]`
