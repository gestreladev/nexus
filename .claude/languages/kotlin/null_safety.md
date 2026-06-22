---
name: null_safety
description: Kotlin null safety — nullable types, safe calls, Elvis, platform types.
agent:
  role: language-expert
  tier: standard
  weight: soft
  triggers:
    - handling nullability
    - interop nulls from Java
metadata:
  type: reference
---

# Null Safety

Kotlin encodes nullability in the type system — `String` cannot be null,
`String?` can. The compiler forces you to handle the null case.

```kotlin
val a: String = "x"      // never null
val b: String? = null    // may be null

b.length                 // compile error — must handle null first
b?.length                // safe call → Int? (null if b is null)
b?.length ?: 0           // Elvis → default when null
b!!.length               // assert non-null → throws NPE if wrong (avoid)
```

## Idioms
- **Safe call chains:** `user?.profile?.email` short-circuits to null.
- **Elvis for early return:** `val id = req.id ?: return badRequest()`.
- **`let` for null-guarded blocks:** `name?.let { render(it) }` runs only if non-null.
- **Avoid `!!`** — it's an explicit "trust me"; each one is a latent NPE.

## Platform types (Java interop)
Values from Java have *platform types* (`String!`) — nullability unknown. Kotlin
won't force a check, so a Java method returning null can still cause an NPE.
Annotate or wrap Java boundaries; treat them as nullable by default.

## In Nexus
Repository reads return nullable domain types (`User?`) — the route uses Elvis
to map a missing row to `404` rather than risking an NPE. Aligns with the
"errors at the boundary" stance.

## References
- [kotlin_language_orchestrator.md](kotlin_language_orchestrator.md) · `[[kotlin_language_orchestrator]]`
- [scope_functions.md](scope_functions.md) · `[[scope_functions]]`
