---
name: revocation_and_denylist
description: JWT revocation via a Redis denylist — closing the stateless-token gap.
agent:
  role: security-auditor
  tier: standard
  weight: medium
  triggers:
    - revoking a token before expiry (logout, compromise)
    - implementing the denylist
metadata:
  type: reference
---

# Revocation & Denylist

## The problem (Lesson 4 E5)
A JWT is valid until `exp` by definition — there's no server state to "turn it
off". So plain stateless JWTs **can't be revoked early**: logout, a compromised
token, or a banned user all need the token to stop working *now*.

## The fix: a Redis denylist
Re-introduce the *minimum* server state — a small set of revoked token ids:
- Give each token a unique id claim (`jti`).
- On revoke (logout/ban), write `denylist:jwt:{jti}` to Redis with
  **`EX` = the token's remaining seconds** — so it self-cleans exactly when the
  token would have expired anyway (no unbounded growth).
- In the JWT `validate` step, `EXISTS denylist:jwt:{jti}` → reject if present.

```
logout(token):  redis.set("denylist:jwt:$jti", "1", EX = secondsUntil(exp))
validate(token): if redis.exists("denylist:jwt:$jti") -> reject (401)
```

## Why this is the right shape
- The denylist holds only *revoked, not-yet-expired* tokens — tiny and TTL-bounded.
- Keeps the common path stateless (most tokens are never revoked); only adds one
  fast `EXISTS` per request.
- Short token `exp` keeps the denylist small and limits exposure if Redis is down.

## Failure mode
For sensitive revocation, the check should **fail closed** if Redis is
unavailable (reject), or lean on short `exp`. A recorded trade-off (see
[use_in_nexus.md](../../infra/redis/use_in_nexus.md) · `[[use_in_nexus]]`).

## In Nexus
Adds `jti` to issued tokens, a `logout` endpoint that denylists, and an `EXISTS`
check in `validate` — built in Phase 4 alongside the cache layer.

## References
- [jwt_orchestrator.md](jwt_orchestrator.md) · `[[jwt_orchestrator]]`
- [claims_and_expiry.md](claims_and_expiry.md) · `[[claims_and_expiry]]`
- [redis_orchestrator.md](../../infra/redis/redis_orchestrator.md) · `[[redis_orchestrator]]`
