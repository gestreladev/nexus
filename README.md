# Nexus

Distributed AI knowledge platform. Ingest documents, search them semantically, query with AI.

## Services

| Service | Language | Purpose |
|---|---|---|
| `nexus-api` | Kotlin / Ktor | Gateway API — users, documents, auth, routing |
| `nexus-ingest` | Python / FastAPI | Document ingestion, chunking, embedding |
| `nexus-search` | Python / FastAPI | Vector search, RAG pipeline, LLM responses |

## Stack

- **Databases:** PostgreSQL (relational + pgvector), Redis (cache)
- **Messaging:** Kafka
- **Observability:** OpenTelemetry, Grafana
- **Infrastructure:** Docker Compose

## Running locally

```bash
docker compose up
```

## Development

See each service's README for setup instructions.

### Commit convention

```
feat(scope): description
fix(scope): description
chore(scope): description
docs(scope): description
test(scope): description
refactor(scope): description
```

Examples:
```
feat(nexus-api): add document upload endpoint
fix(nexus-ingest): handle empty PDF pages
chore(deps): bump ktor to 3.1.0
```
