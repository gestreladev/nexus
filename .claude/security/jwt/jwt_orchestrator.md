---
name: jwt_orchestrator
description: Routing for JWT — structure, signing, claims, revocation via Redis denylist.
agent:
  role: security-auditor
  tier: standard
  weight: medium
  triggers:
    - issuing or verifying a JWT
    - designing token claims, expiry, or revocation
metadata:
  type: orchestrator
---

# JWT Orchestrator — Nexus

JSON Web Tokens carry **verified identity** so a stateless service authorizes a
request with a signature check — no session store, no DB hit. Shipped in
`v0.3.0`; revocation (Redis denylist) lands in Phase 4. Model selection:
[model_decision.md](../../configurations/model_decision.md) · `[[model_decision]]`.

---

## Metadata
| Field | Value |
|---|---|
| Algorithm | HS256 (HMAC + shared secret) |
| Secret | `JWT_SECRET` env; dev fallback only |
| Claims | `sub` (user id), `iss`, `aud`, `iat`, `exp`, `email` |
| Library | `com.auth0:java-jwt` via Ktor `auth-jwt` |
| Revocation | Redis denylist keyed by token id |

---

## Routing Table
| Topic | Scope | Document |
|---|---|---|
| Structure | header.payload.signature; signed ≠ encrypted | [structure.md](structure.md) · `[[structure]]` |
| Signing & verification | HS256, secret, Ktor verifier/validate | [signing_and_verification.md](signing_and_verification.md) · `[[signing_and_verification]]` |
| Claims & expiry | standard + custom claims, short `exp` | [claims_and_expiry.md](claims_and_expiry.md) · `[[claims_and_expiry]]` |
| Revocation & denylist | Redis-backed revocation (closes E5) | [revocation_and_denylist.md](revocation_and_denylist.md) · `[[revocation_and_denylist]]` |

## References
- [security_orchestrator.md](../security_orchestrator.md) · `[[security_orchestrator]]`
- [redis_orchestrator.md](../../infra/redis/redis_orchestrator.md) · `[[redis_orchestrator]]`
- [idempotency_and_statelessness.md](../../fundamentals/http/idempotency_and_statelessness.md) · `[[idempotency_and_statelessness]]`
