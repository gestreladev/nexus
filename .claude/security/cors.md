---
name: cors
description: CORS — cross-origin request policy for the Nexus API.
agent:
  role: security-auditor
  tier: standard
  weight: soft
  triggers:
    - a browser frontend calling the API cross-origin
    - configuring allowed origins/methods/headers
metadata:
  type: reference
---

# CORS

**Cross-Origin Resource Sharing** is a browser security model: by default a page
at origin A cannot read responses from API origin B. The *server* opts specific
origins in via response headers; the browser enforces it.

## What it is (and isn't)
- It protects **users' browsers** from unauthorized cross-origin reads.
- It is **not** server-side access control — it doesn't stop curl, scripts, or
  other servers. Authn/authz (JWT, ownership checks) still do the real gatekeeping.

## Key headers
```
Access-Control-Allow-Origin: https://app.nexus.dev   # specific origin, not * with credentials
Access-Control-Allow-Methods: GET, POST, PATCH, DELETE
Access-Control-Allow-Headers: Authorization, Content-Type
Access-Control-Allow-Credentials: true               # only with an explicit origin
```
Browsers send a **preflight** `OPTIONS` for non-simple requests; the server must
answer it.

## Rules for Nexus
- **Allow-list specific origins** (the frontend's), never `*` when credentials/
  `Authorization` are involved.
- Configure via Ktor's `CORS` plugin as its own `ModulePipeline` stage when a
  browser client exists.

## In Nexus
No browser frontend yet, so CORS is unconfigured. When the SPA arrives, add the
CORS stage with an explicit origin allow-list — never wildcard with bearer tokens.

## References
- [security_orchestrator.md](security_orchestrator.md) · `[[security_orchestrator]]`
- [http_basics.md](../fundamentals/http/http_basics.md) · `[[http_basics]]`
