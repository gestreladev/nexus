---
name: lesson_04_auth
description: Lesson 4 log — auth & security (bcrypt, JWT, OWASP-aware) + auth layer.
agent:
  role: tutor
  tier: nano
  weight: soft
  triggers:
    - reviewing what Lesson 4 covered
    - recapping auth before a session
metadata:
  type: reference
---

# Lesson 4 — Auth & Security (Phase 3)

| Field | Value |
|---|---|
| Phase | 3 — Auth & Security |
| Roadmap topics | authentication, jwt, oauth (concept), token-auth, web-security, owasp, cors, ssltls, bcrypt |
| Deliverable | Auth layer in `nexus-api` — register/login/me, bcrypt, JWT |
| Milestone | `v0.3.0` (auth layer shipped) |
| Status | ✅ mastery pass |

## Concepts taught
Threat model (assume the DB leaks); hash vs encrypt; why `SHA-256(password)` is
wrong (speed + no salt); bcrypt (salt + cost embedded, 60 chars); JWT
(header.payload.signature, signed-not-encrypted, `exp`, secret sacred,
no-secrets-in-payload, hard to revoke); login anti-enumeration.

## Exercises
| Q | Topic | Verdict |
|---|---|---|
| E1 | Two attacks on unsalted SHA-256 (speed + rainbow) | ✅ |
| E2 | Encryption reversibility = key is a liability | ✅ |
| E3 | bcrypt embeds salt+cost → one column | ❌→taught ✅ |
| E4 | Tampered payload → signature mismatch → reject | ✅ |
| E5 | `role` in JWT goes stale (mutable authz) | ❌→taught |
| E6 | bcrypt@login only; protected route = JWT verify, no DB | ✅ (login refined) |

## Gaps to revisit
- E5 (JWT staleness / revocation) — taught; confirm at Lesson 5 recap.

## Deliverable detail
- `V3__add_password_hash.sql` (VARCHAR(60))
- `PasswordHasher` (bcrypt cost 12), `JwtService` (HS256, secret via `JWT_SECRET`)
- `AuthService` (register/login, generic 401 to avoid user enumeration)
- `plugins/Security.kt` + `authentication()` pipeline stage (before routing)
- `routes/v1/AuthRoute.kt`: register(201)/login/me(`authenticate`)
- Verified: curl flow + 5 integration tests (register→login→me, 409, 401×2, 400)

## References
- [lesson_03_database.md](lesson_03_database.md) · `[[lesson_03_database]]`
- [lesson_orchestrator.md](../lesson_orchestrator.md) · `[[lesson_orchestrator]]`
