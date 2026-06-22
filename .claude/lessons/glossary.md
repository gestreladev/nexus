---
name: glossary
description: Running glossary of concepts taught across Nexus lessons.
agent:
  role: reference-reader
  tier: nano
  weight: soft
  triggers:
    - looking up a term covered in a lesson
    - adding a newly-taught concept
metadata:
  type: reference
---

# Glossary

Concepts introduced in lessons, newest phase last. Grows each lesson.

---

## Phase 0 — Foundations
- **TCP handshake** — SYN/SYN-ACK/ACK to open a reliable byte stream.
- **HTTP Keep-Alive** — reusing one TCP connection for many requests (HTTP/1.1+).
- **Idempotency** — same effect whether called once or N times (GET/PUT/DELETE).
- **Statelessness** — server keeps no per-client state between requests; state
  lives in the token or the database.
- **DNS records** — A, AAAA, CNAME, TXT.

## Phase 1 — First Service
- **Reverse proxy** — fronts the app server (TLS, rate-limit, load-balance).
- **REST resource modeling** — nouns in URLs, verbs as HTTP methods.
- **API versioning** — `/v1/`; additive changes safe, removals/renames breaking.
- **OpenAPI** — machine-readable API contract.
- **Chain of Responsibility / `@DslMarker`** — ordered pipeline + scoped DSL
  (see designpatterns + idioms).

## Phase 2 — Data Layer
- **ACID** — Atomicity, Consistency, Isolation, Durability.
- **Transaction boundary** — unit of work; repositories own it.
- **Index (B-tree)** — fast lookup structure; primary/foreign keys indexed.
- **Partial index** — indexes only rows matching a predicate (high selectivity).
- **Normalization (3NF)** — non-key columns depend only on the primary key.
- **Migration** — versioned, ordered schema change (Flyway `V1__`, `V2__`).
- **ORM (Exposed DSL)** — type-safe SQL builder; `ResultRow` mapped to domain.
- **Connection pool (HikariCP)** — reuse open DB connections.

## Phase 3 — Auth & Security
- **Hash vs encrypt** — hash is one-way (store it); encryption is reversible (key liability).
- **bcrypt** — deliberately slow hash; salt + cost (work factor) embedded in 60 chars.
- **Salt** — per-user random value; kills rainbow tables, hides shared passwords.
- **Work factor (cost)** — tunable slowness; +1 doubles the work.
- **JWT** — `header.payload.signature`; **signed, not encrypted** (payload is readable).
- **Claims** — payload fields: `sub` (user id), `exp`, `iat`, custom.
- **Bearer token** — `Authorization: Bearer <jwt>` sent per request; no server session.
- **User enumeration** — leaking whether an email exists; avoided via generic 401.

---

## References
- [lesson_orchestrator.md](lesson_orchestrator.md) · `[[lesson_orchestrator]]`
