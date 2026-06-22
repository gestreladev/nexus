---
name: compose
description: Docker Compose — services, depends_on service_healthy, healthchecks, env.
agent:
  role: infra-specialist
  tier: standard
  weight: medium
  triggers:
    - writing or reviewing a compose file
    - ordering service startup by health
metadata:
  type: reference
---

# Docker Compose

Compose defines a multi-container app in one YAML file and runs it with
`docker compose up`. (Verified against docs.docker.com / docker/compose via
Context7.)

## Nexus today
```yaml
services:
  postgres:
    image: pgvector/pgvector:pg17
    environment:
      POSTGRES_DB: nexus
      POSTGRES_USER: nexus
      POSTGRES_PASSWORD: nexus
    ports: ["5433:5432"]
    volumes: ["nexus-postgres-data:/var/lib/postgresql/data"]
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U nexus -d nexus"]
      interval: 5s
      timeout: 5s
      retries: 10
volumes:
  nexus-postgres-data:
```

## Startup ordering — depends_on + health (Phase 8)
`depends_on` alone only waits for *start*, not *readiness*. Gate on the
healthcheck so `nexus-api` never boots before the DB accepts connections:
```yaml
  nexus-api:
    build: ./nexus-api
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      DB_URL: jdbc:postgresql://postgres:5432/nexus   # service-name host, not localhost
      JWT_SECRET: ${JWT_SECRET}
    ports: ["8080:8080"]
```

## Environment precedence
`docker compose run --env` > service `environment:` > `env_file:` > image `ENV`.
Keep secrets in env (passed at runtime), never baked into the image.

## Common commands
```bash
docker compose up -d        # start detached
docker compose ps           # status + health
docker compose logs -f api  # follow logs
docker compose down         # stop + remove (keeps named volumes)
```

## References
- [docker_orchestrator.md](docker_orchestrator.md) · `[[docker_orchestrator]]`
- [volumes_and_networks.md](volumes_and_networks.md) · `[[volumes_and_networks]]`
- [best_practices.md](best_practices.md) · `[[best_practices]]` — 12-factor config
