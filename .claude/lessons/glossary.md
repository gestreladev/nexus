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

## Phase 4 — Caching
- **Cache-aside** — check cache; on miss load source, populate, return.
- **Invalidation** — bounding staleness; prefer delete-on-write + TTL.
- **TTL** — per-key expiry (freshness); **eviction** — capacity policy under maxmemory.
- **Cache stampede** — hot key expiry → herd of misses; fix with single-flight/jitter.
- **Redis** — in-memory KV store; `SET key v EX ttl`, `EXISTS`, RDB/AOF.
- **HTTP caching** — Cache-Control / ETag / 304 at the edge.
- **JWT denylist** — Redis set of revoked `jti`s (TTL = token life) → real revocation.

## Phase 5 — Testing & CI/CD
- **Test pyramid** — many unit / some integration / few E2E.
- **Unit test** — pure logic, no I/O, milliseconds; the wide base.
- **Integration test** — exercises a real boundary (DB, Redis).
- **Fake vs mock** — fake = working in-memory impl; mock = scripted expectations. Prefer fakes.
- **Dependency rule** — no deps → pure unit; dep behind an interface → unit with a fake.
- **CI/CD** — automated build + test on every PR (GitHub Actions).
- **Service container** — a real DB/Redis spun up by CI so integration tests run.

## Phase 6 — Python Service
- **Polyglot backend** — multiple backend services, each in the best-fit language.
- **FastAPI** — async Python web framework; auto-generates OpenAPI.
- **ASGI / Uvicorn** — the async server interface / server FastAPI runs on.
- **Pydantic** — runtime-validated models at the HTTP boundary.
- **Static type checker (mypy)** — what makes type hints *enforced* (not Protocols).
- **uv** — fast Python package/venv manager (managed CPython builds).

## Phase 7 — Async & Messaging
- **Message broker** — middle layer carrying events producer → consumer.
- **Topic / partition** — named event stream, split for parallelism; order per partition.
- **Consumer group** — consumers sharing a group split partitions → scale.
- **Offset** — a consumer's position; committing = "processed up to here".
- **At-least-once** — default delivery; may duplicate → consumers must be idempotent.
- **Key-ordering** — key by entity (documentId) to keep its events ordered.
- **SSE / WebSockets** — server→client / full-duplex real-time to the client.

## Phase 8 — Containers & Architecture
- **Multi-stage build** — fat build stage compiles; slim runtime stage ships only
  the artifact → smaller image + smaller attack surface.
- **Non-root container** — run as an unprivileged user; limits blast radius.
- **12-factor config** — config in the environment, not the image; same image, env per deploy.
- **Service-name DNS** — Compose resolves a service name to its container; use it
  in-network (`kafka:29092`), `localhost:<published>` from the host.
- **Dual-listener (Kafka)** — a broker advertises an address back to clients, so it
  needs EXTERNAL (host) + INTERNAL (in-network) listeners.
- **depends_on: service_healthy** — gate startup on a healthcheck (readiness), not start.
- **Monolith / microservices / SOA / serverless** — deployment-unit granularity spectrum.
- **Service mesh** — sidecar layer handling service-to-service networking.
- **Circuit breaker** — fail fast when a dependency is down; stop hammering it.

## Phase 9 — Search & Vectors
- **Lexical search** — full-text matching on exact lexemes (`tsvector`/`tsquery`, GIN).
- **Semantic search** — match by meaning via embedding vectors.
- **Embedding** — text→fixed-length vector; similar meaning ⇒ nearby (cosine).
- **Retrieval asymmetry** — documents and queries embedded differently (`input_type`/prompt).
- **ANN** — approximate nearest neighbor; trades a little recall for sub-linear speed.
- **HNSW / IVFFlat** — graph-based / clustering-based vector indexes (HNSW = 2026 default).
- **pgvector** — Postgres extension: `vector(N)` column, `<=>` cosine distance.
- **Chunking** — split a document into embeddable pieces, usually with overlap.
- **Hybrid search** — fuse lexical + semantic scores (e.g. Reciprocal Rank Fusion).
- **Strategy pattern** — swap embedding/chunking implementations behind one interface.

## Phase 10 — Observability
- **Observability** — inferring internal state from outputs; "up" ≠ "working well".
- **Three pillars** — logs (what happened), metrics (how much/often), traces (one request's path).
- **Trace / span** — a request as a tree of timed operations; span = one op.
- **Context propagation** — carrying `trace_id`+`parent_span_id` across boundaries.
- **W3C traceparent** — `00-traceid-spanid-flags`; rides Kafka headers producer→consumer.
- **OpenTelemetry (OTel)** — vendor-neutral standard; instrument once, export anywhere.
- **OTLP** — the OTel wire protocol (gRPC 4317 / HTTP 4318).
- **RED** — Rate, Errors, Duration; service-level golden signals.
- **Percentile (p99)** — tail latency; the average hides the slow requests users feel.
- **Structured logging** — JSON logs with `trace_id` so logs correlate to traces.

---

## References
- [lesson_orchestrator.md](lesson_orchestrator.md) · `[[lesson_orchestrator]]`
