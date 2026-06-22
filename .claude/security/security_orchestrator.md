---
name: security_orchestrator
description: Routing for security — auth, JWT, password hashing, OWASP, TLS, CORS.
agent:
  role: security-auditor
  tier: standard
  weight: medium
  triggers:
    - any auth or security task
    - routing a security question
    - threat-modeling an endpoint
metadata:
  type: orchestrator
---

# Security Orchestrator — Nexus

Cross-cutting security for all services. Covers the Phase 3 (`v0.3.0`) auth work
and the standards that guard every endpoint. Model selection:
[model_decision.md](../configurations/model_decision.md) · `[[model_decision]]`.

Per root Rule 7, consult design patterns before security code; per OWASP, review
the threat surface of any new endpoint.

---

## Metadata
| Field | Value |
|---|---|
| Auth model | Stateless JWT (HS256) + bcrypt password hashing |
| Shipped | `v0.3.0` — register / login / protected `/me` |
| Secret | `JWT_SECRET` env (never in code/repo) |
| Revocation | Redis denylist (Phase 4) |

---

## Routing Table
| Topic | Scope | Document |
|---|---|---|
| JWT | Tokens: structure, signing, claims, revocation | [jwt_orchestrator.md](jwt/jwt_orchestrator.md) · `[[jwt_orchestrator]]` |
| Password hashing | bcrypt, salt, work factor | [password_hashing.md](password_hashing.md) · `[[password_hashing]]` |
| OWASP | Top 10 risks + Nexus mitigations | [owasp.md](owasp.md) · `[[owasp]]` |
| TLS | Encryption in transit, termination | [tls.md](tls.md) · `[[tls]]` |
| CORS | Cross-origin request policy | [cors.md](cors.md) · `[[cors]]` |

**OAuth2 / OpenID Connect:** covered *conceptually* — authorization-code flow
(delegated auth: redirect → code → token exchange) — but a full external
provider is deferred beyond `v0.3.0`. Nexus issues its own JWTs for now; a leaf
is added here when an OAuth provider is integrated.

## References
- [CLAUDE.md](../CLAUDE.md) · `[[CLAUDE]]`
- [lesson_04_auth.md](../lessons/log/lesson_04_auth.md) · `[[lesson_04_auth]]`
- [conventions.md](../services/nexus-api/api/conventions.md) · `[[conventions]]`
