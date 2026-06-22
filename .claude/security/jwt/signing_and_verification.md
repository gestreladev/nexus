---
name: signing_and_verification
description: JWT signing and verification — HS256, the secret, Ktor verifier/validate.
agent:
  role: security-auditor
  tier: standard
  weight: medium
  triggers:
    - signing or verifying a token in code
    - configuring the Ktor jwt provider
metadata:
  type: reference
---

# Signing & Verification

## HS256 (what Nexus uses)
Symmetric HMAC: the **same secret** signs and verifies. Simple and fast — right
when one service (or a trusted set sharing the secret) both issues and validates.
Alternative: **RS256** (asymmetric) — sign with a private key, verify with a
public key — for when verifiers shouldn't be able to mint tokens (multi-party).

## Sign (at login)
```kotlin
JWT.create()
  .withIssuer(issuer).withAudience(audience)
  .withSubject(userId.toString())
  .withClaim("email", email)
  .withIssuedAt(now).withExpiresAt(now + ttl)
  .sign(Algorithm.HMAC256(secret))
```

## Verify (Ktor jwt provider)
```kotlin
jwt("auth-jwt") {
  verifier(JWT.require(Algorithm.HMAC256(secret))
             .withIssuer(issuer).withAudience(audience).build())
  validate { cred -> if (cred.payload.subject != null) JWTPrincipal(cred.payload) else null }
  challenge { _, _ -> respond(401, ErrorResponse("UNAUTHORIZED", "...")) }
}
```
The `verifier` checks signature + issuer + audience + expiry; `validate` turns a
valid token into a principal (and is where the denylist check will go).

## The secret is sacred
`JWT_SECRET` from env, never in code/repo. Leak it → anyone forges valid tokens
for any user. Rotate by supporting old+new during a window.

## In Nexus
`JwtService` signs; `configureSecurity()` builds the verifier. Both read the same
secret so signer and verifier agree.

## References
- [jwt_orchestrator.md](jwt_orchestrator.md) · `[[jwt_orchestrator]]`
- [structure.md](structure.md) · `[[structure]]`
