---
name: rest
description: REST architectural constraints and resource modeling.
agent:
  role: subject-expert
  tier: standard
  weight: soft
  triggers:
    - modeling a resource or designing an endpoint
    - reviewing REST- fulness
metadata:
  type: reference
---

# REST

REST is a set of architectural constraints, not a protocol. The constraints
with real day-to-day consequence:

## 1. Resource-based URLs — nouns, not verbs
```
✓ GET    /v1/documents          list
✓ POST   /v1/documents          create
✓ GET    /v1/documents/{id}     read one
✓ PATCH  /v1/documents/{id}     partial update
✓ DELETE /v1/documents/{id}     delete
✗ POST   /v1/getDocuments       RPC-style — wrong
```

## 2. HTTP methods carry the verb
The method *is* the action; the URL names the resource. Don't put `create`/
`delete` in the path.

## 3. Status codes are part of the contract
Map outcomes to codes honestly (201 created, 404 missing, 409 conflict…).

## 4. Statelessness
No server session between requests — identity travels per request (token).

## 5. Versioning
APIs are public contracts. **Additive** changes (new field) are safe;
**removals/renames** are breaking → bump the version (`/v2`). Pick a field-naming
convention day one and never change it.

## REST vs RPC vs GraphQL
REST models *resources*; RPC models *actions*; GraphQL exposes a query language
over a graph. Nexus uses REST for its gateway — predictable, cacheable, simple.

## Why it matters in Nexus
All routes live under `/v1/`; concrete conventions (status codes, error shape,
camelCase, pagination) are in
[conventions.md](../../services/nexus-api/api/conventions.md) · `[[conventions]]`.

## References
- [web_orchestrator.md](web_orchestrator.md) · `[[web_orchestrator]]`
- [openapi.md](openapi.md) · `[[openapi]]`
