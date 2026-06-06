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
- Nouns only — never verbs in the path (`/v1/search`, not `/v1/doSearch`)
- Version prefix on every route from day one

## HTTP methods

| Method | Semantics | Body | Idempotent |
|---|---|---|---|
| `GET` | Read — no side effects | none | ✅ |
| `POST` | Create | resource | ❌ |
| `PUT` | Full replace | full resource | ✅ |
| `PATCH` | Partial update | changed fields only | ✅ |
| `DELETE` | Remove | none | ✅ |

## Status codes used in Nexus

| Code | When |
|---|---|
| `200 OK` | Successful GET, PATCH, DELETE |
| `201 Created` | Successful POST (resource created) |
| `204 No Content` | Successful DELETE with no body |
| `400 Bad Request` | Validation failure, missing required field |
| `401 Unauthorized` | Missing or invalid auth token |
| `403 Forbidden` | Valid token, but not allowed to access this resource |
| `404 Not Found` | Resource does not exist |
| `409 Conflict` | Duplicate (e.g. email already registered) |
| `422 Unprocessable` | Structurally valid but semantically wrong |
| `500 Internal Server Error` | Unhandled exception — never expose details |

**Never return `200` with an error body.**

## Response shapes

### Success

The resource itself, no wrapper:
```json
{ "id": "uuid", "email": "user@nexus.dev", "createdAt": "…" }
```

For collections:
```json
{
  "items": [ { … }, { … } ],
  "total": 42
}
```

### Error

Always `ErrorResponse` — no other shape:
```json
{
  "error": "DOCUMENT_NOT_FOUND",
  "message": "Document with id '123' does not exist"
}
```

`error` — machine-readable SCREAMING_SNAKE_CASE code  
`message` — human-readable explanation, safe to show to callers

Never include stack traces, internal class names, or SQL errors in responses.

## Field naming

All fields `camelCase`. Decided on day one — never change.

## Dates

All timestamps in **ISO 8601 UTC** with timezone:
```
"createdAt": "2026-06-05T23:00:00Z"
```

Never return raw epoch milliseconds or locale-formatted strings.

## Pagination (when needed)

```
GET /v1/documents?page=1&pageSize=20
```

```json
{
  "items": [ … ],
  "total": 200,
  "page": 1,
  "pageSize": 20
}
```
