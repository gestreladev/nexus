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
| Version | `v0.3.0` ⏭️ next |
| Lesson | 4 |
| Topics | authentication, jwt, oauth, token-authentication, web-security, owasp-risks, cors, ssltls, bcrypt |
| Status | ⏭️ planned |

## Planned deliverables
- [ ] User registration + login endpoints (`POST /v1/auth/register`, `/login`)
- [ ] Password hashing with bcrypt (never plaintext)
- [ ] JWT issuance + verification; `Authorization: Bearer`
- [ ] Protected routes (401 unauthenticated, 403 forbidden)
- [ ] CORS configuration; OWASP Top 10 review of the auth surface
- [ ] OAuth2 concepts (authorization code flow) — at least documented/spiked

## Acceptance
- Register → login → access a protected route end-to-end (tested)
- No secrets hardcoded; tokens signed with an env-provided key
- Exercises passed; PR merged; tag `v0.3.0`

## Open gaps to fold in
- Recap 3NF normalization (carried from Lesson 3)

## References
- [roadmap.md](../roadmap.md) · `[[roadmap]]`
- [progress.md](../progress.md) · `[[progress]]`
