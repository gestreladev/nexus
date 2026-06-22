---
name: phase_03
description: Phase 3 tracker — auth & security (JWT, OAuth2, OWASP, TLS).
agent:
  role: progress-tracker
  tier: standard
  weight: soft
  triggers:
    - reviewing or planning Phase 3
    - starting the auth layer
metadata:
  type: reference
---

# Phase 3 — Auth & Security

| Field | Value |
|---|---|
| Version | `v0.3.0` ✅ shipped |
| Lesson | 4 |
| Topics | authentication, jwt, oauth, token-authentication, web-security, owasp-risks, cors, ssltls, bcrypt |
| Status | ✅ complete (auth layer) |

## Deliverables
- [x] Registration + login endpoints (`POST /v1/auth/register`, `/login`)
- [x] Password hashing with bcrypt cost 12 (never plaintext; 60-char column)
- [x] JWT issuance + verification (HS256); `Authorization: Bearer`
- [x] Protected route `GET /v1/auth/me` (401 unauthenticated)
- [x] OWASP-aware: generic 401 (anti-enumeration), no secrets in payload, env secret
- [~] OAuth2 — authorization-code flow covered **conceptually**; full provider deferred
- [ ] CORS config + 403 ownership checks — arrive with resource ownership (later)

## Acceptance — met
- [x] Register → login → protected route end-to-end (curl + 5 integration tests)
- [x] No secrets hardcoded; JWT signed with `JWT_SECRET` (env, dev fallback)
- [x] Exercises passed (E1–E6); `authentication()` stage ordered before routing

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
- [progress.md](../progress.md) · `[[progress]]`
