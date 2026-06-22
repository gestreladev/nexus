---
name: structure
description: JWT structure — header, payload, signature; signed not encrypted.
agent:
  role: security-auditor
  tier: standard
  weight: soft
  triggers:
    - understanding what a JWT contains
    - deciding what to put in a token
metadata:
  type: reference
---

# JWT Structure

Three Base64URL parts joined by dots:
```
header.payload.signature
eyJhbGci...  .  eyJzdWIi...  .  K3pQ9x...
```

| Part | Contains |
|---|---|
| **Header** | `alg` (e.g. HS256), `typ: JWT` |
| **Payload** | claims — `sub`, `exp`, `iat`, `iss`, `aud`, custom |
| **Signature** | `HMAC(base64(header) + "." + base64(payload), secret)` |

## Signed, not encrypted (the crucial point)
Header and payload are **Base64, not encryption** — anyone can decode and read
them. A JWT is **not secret**; it is **tamper-evident**. The signature lets the
server detect any modification.

Consequences:
- **Never put secrets in the payload** (passwords, keys) — it's readable.
- Storing identity (`sub`) and non-sensitive claims (`email`) is fine.
- Confidentiality in transit comes from **TLS**, not the token.

## Verification, intuitively
Server recomputes the signature from the received header+payload using its
secret and compares. Mismatch → reject. So an attacker can't forge or alter a
token without the secret.

## In Nexus
`sub` = user UUID; `email` for convenience; signed with `JWT_SECRET`. See
[signing_and_verification.md](signing_and_verification.md) · `[[signing_and_verification]]`.

## References
- [jwt_orchestrator.md](jwt_orchestrator.md) · `[[jwt_orchestrator]]`
- [claims_and_expiry.md](claims_and_expiry.md) · `[[claims_and_expiry]]`
