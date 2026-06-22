---
name: tls
description: TLS in the Nexus security model — encryption in transit and termination.
agent:
  role: security-auditor
  tier: standard
  weight: soft
  triggers:
    - reasoning about transport security
    - deciding where TLS terminates
metadata:
  type: reference
---

# TLS — Security View

TLS gives **encryption, server authentication, and integrity** in transit. The
protocol mechanics are in
[https_tls.md](../fundamentals/http/https_tls.md) · `[[https_tls]]`; this doc is
the *security policy*.

## Rules for Nexus
- **All external traffic is HTTPS.** Bearer tokens and passwords must never cross
  plaintext — a token on plain HTTP is sniffable and replayable.
- **Terminate at the reverse proxy** (Nginx/Caddy, Phase 8); plain HTTP only on
  the private container network between services.
- **HSTS** header in production so browsers refuse plaintext downgrades.
- **Modern TLS only** (1.2+/1.3); disable legacy ciphers — a Security
  Misconfiguration item in OWASP.

## Token + TLS interplay
JWT is signed, not encrypted — its payload is readable. TLS is what keeps it
*confidential in transit*. Both are needed: signing stops tampering, TLS stops
eavesdropping.

## In Nexus
Local/dev and inter-container traffic use plain HTTP on a private network;
production fronts everything with TLS at the proxy. No app-level TLS code —
it's an infrastructure boundary.

## References
- [security_orchestrator.md](security_orchestrator.md) · `[[security_orchestrator]]`
- [https_tls.md](../fundamentals/http/https_tls.md) · `[[https_tls]]`
- [web_servers.md](../fundamentals/web/web_servers.md) · `[[web_servers]]`
