---
name: password_hashing
description: Password hashing with bcrypt — salt, work factor, why not SHA-256.
agent:
  role: security-auditor
  tier: standard
  weight: soft
  triggers:
    - storing or verifying a password
    - choosing a hashing algorithm/cost
metadata:
  type: reference
---

# Password Hashing

**Never store plaintext. Hash, don't encrypt** — you only ever *verify* a
password, never recover it, so reversibility (and its key) is pure liability.

## Why not `SHA-256(password)`
Two fatal flaws:
1. **Too fast** — GPUs do billions/sec → offline brute-force is cheap.
2. **No salt** — identical passwords hash identically → rainbow tables + shared-
   password detection.

## The fix: a slow, salted hash — bcrypt
- **Salt** — per-user random value; kills rainbow tables, hides shared passwords.
- **Work factor (cost)** — tunable slowness; each +1 *doubles* the work. Pick a
  cost where one hash ≈ 100–250ms.
- bcrypt embeds variant + cost + salt in a **60-char** output → one column,
  zero salt management.

```
$2b$12$<22-char salt><31-char hash>
 │   └ cost 12 (2^12 rounds)
 └ variant
```

## In Nexus
`PasswordHasher` (cost 12) via `at.favre.lib:bcrypt`; stored in
`users.password_hash VARCHAR(60)`. `verify(raw, storedHash)` at login — bcrypt
reads cost+salt back out of the stored string. Modern alternative: Argon2id
(memory-hard); bcrypt is the proven, sufficient choice here.

## References
- [security_orchestrator.md](security_orchestrator.md) · `[[security_orchestrator]]`
- [jwt_orchestrator.md](jwt/jwt_orchestrator.md) · `[[jwt_orchestrator]]`
- [lesson_04_auth.md](../lessons/log/lesson_04_auth.md) · `[[lesson_04_auth]]`
