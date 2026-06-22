---
name: claims_and_expiry
description: JWT claims (standard + custom) and expiry strategy.
agent:
  role: security-auditor
  tier: standard
  weight: soft
  triggers:
    - choosing what claims to include
    - setting token lifetime / refresh
metadata:
  type: reference
---

# Claims & Expiry

## Standard (registered) claims
| Claim | Meaning |
|---|---|
| `sub` | subject — the user id (Nexus uses the UUID) |
| `iss` | issuer — who minted it (`nexus-api`) |
| `aud` | audience — who it's for (`nexus-api`) |
| `iat` | issued-at timestamp |
| `exp` | expiry timestamp — **always set this** |

`iss`/`aud` are verified so a token minted for another system/audience is
rejected.

## Custom claims
Add small, non-sensitive identity data to save lookups — e.g. `email`. **Caveat
(Lesson 4 E5):** the payload is an **immutable snapshot**. Mutable authorization
data like `role` goes *stale* — a demoted user's old token still says `admin`
until expiry. So:
- Store stable identity (`sub`, `email`) — fine.
- Don't trust mutable authz (`role`) solely from the token; re-check for
  sensitive actions, or keep expiry short.

## Expiry strategy
- **Short-lived access token** (e.g. 15–60 min) bounds the blast radius of theft.
- **Refresh token** (longer-lived, revocable, stored server-side) issues new
  access tokens without re-login — a later addition.
- Short `exp` is also what makes statelessness tolerable despite weak revocation
  — and pairs with the denylist for hard revocation.

## In Nexus
`sub` + `email`, `exp` = `expiresInMinutes` (config, dev 60). Refresh tokens are
a future addition; revocation via denylist is Phase 4.

## References
- [jwt_orchestrator.md](jwt_orchestrator.md) · `[[jwt_orchestrator]]`
- [revocation_and_denylist.md](revocation_and_denylist.md) · `[[revocation_and_denylist]]`
