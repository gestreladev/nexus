---
name: conventions
description: URL structure, status codes, response shapes, and pagination for nexus-api.
agent:
  role: api-specialist
  tier: standard
  weight: soft
  triggers:
    - designing or reviewing any HTTP endpoint
    - choosing a status code or error shape
metadata:
  type: reference
---

# API Conventions

## URL structure

```
/v{version}/{resource}/{id?}/{sub-resource?}

/v1/users
/v1/users/{id}
/v1/users/{id}/documents
/v1/documents/{id}
```

- Always lowercase, hyphen-separated words (`/v1/document-tags`)
- Nouns only — never verbs in the path
- Version prefix on every route from day one

## HTTP methods

| Method | Semantics | Body | Idempotent |
|---|---|---|---|
| `GET` | Read — no side effects | none | ✅ |
| `POST` | Create | resource | ❌ |
| `PUT` | Full replace | full resource | ✅ |
| `PATCH` | Partial update | changed fields only | ✅ |
| `DELETE` | Remove | none | ✅ |

## Status codes

| Code | When |
|---|---|
| `200 OK` | Successful GET, PATCH, DELETE |
| `201 Created` | Successful POST |
| `204 No Content` | Successful DELETE with no body |
| `400 Bad Request` | Validation failure, missing field |
| `401 Unauthorized` | Missing or invalid auth token |
| `403 Forbidden` | Valid token, not allowed |
| `404 Not Found` | Resource does not exist |
| `409 Conflict` | Duplicate (e.g. email already registered) |
| `422 Unprocessable` | Structurally valid but semantically wrong |
| `500 Internal Server Error` | Unhandled exception — never expose details |

**Never return `200` with an error body.**

## Response shapes

### Success — the resource itself, no wrapper
```json
{ "id": "uuid", "email": "user@nexus.dev", "createdAt": "…" }
```
For collections:
```json
{ "items": [ { … } ], "total": 42 }
```

### Error — always `ErrorResponse`
```json
{ "error": "DOCUMENT_NOT_FOUND", "message": "Document '123' does not exist" }
```
`error` — SCREAMING_SNAKE_CASE code; `message` — human-readable, safe to show.
Never include stack traces, class names, or SQL errors.

## Field naming
All fields `camelCase`. Decided on day one — never change.

## Dates
ISO 8601 UTC with timezone: `"createdAt": "2026-06-05T23:00:00Z"`.

## Pagination
```
GET /v1/documents?page=1&pageSize=20
```
```json
{ "items": [ … ], "total": 200, "page": 1, "pageSize": 20 }
```

## References
- [api_orchestrator.md](api_orchestrator.md) · `[[api_orchestrator]]`
