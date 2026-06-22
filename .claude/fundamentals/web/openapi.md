---
name: openapi
description: OpenAPI — machine-readable API contracts.
agent:
  role: subject-expert
  tier: nano
  weight: soft
  triggers:
    - documenting or generating an API contract
    - generating clients or contract tests
metadata:
  type: reference
---

# OpenAPI

OpenAPI (formerly Swagger) is a standard JSON/YAML format describing a REST API:
what routes exist, what they accept, what they return, and what errors are
possible. It is the **single source of truth** for the API surface.

## What it drives
- **Docs** — interactive API documentation (Swagger UI / Redoc)
- **Client generation** — typed clients in any language from the spec
- **Contract testing** — verify the implementation matches the spec
- **Mocking** — stand up a fake server from the spec

## Shape (sketch)
```yaml
paths:
  /v1/documents/{id}:
    get:
      parameters: [ { name: id, in: path, required: true } ]
      responses:
        '200': { description: OK, content: { application/json: {...} } }
        '404': { description: Not found }
```

## Code-first vs spec-first
- **Spec-first**: write the YAML, generate server stubs + clients.
- **Code-first**: annotate the code, generate the spec from it (keeps code and
  docs in sync automatically). Nexus leans code-first — generate OpenAPI from the
  Ktor routes so docs never drift.

## Why it matters in Nexus
A generated OpenAPI doc lets future services (and you) consume `nexus-api`
without reading its source. Aligns with the project's "contract from day one"
stance.

## References
- [web_orchestrator.md](web_orchestrator.md) · `[[web_orchestrator]]`
- [rest.md](rest.md) · `[[rest]]`
