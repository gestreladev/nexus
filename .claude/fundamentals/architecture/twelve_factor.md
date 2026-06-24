---
name: twelve_factor
description: The Twelve-Factor App methodology for scalable, deploy-portable backend services.
agent:
  role: subject-expert
  tier: standard
  weight: medium
  triggers:
    - designing a service for portable, reproducible deployment across environments
    - deciding where config, secrets, and backing-service URLs should live
    - making a service stateless, fast to start, and safe to kill
metadata:
  type: reference
---
# Twelve-Factor App

A methodology (Heroku, ~2011) for building services that are **declarative** to set up,
**portable** across execution environments, **deployable** to the cloud, and **scalable**
without rearchitecting. The unit is a *single deployable codebase*, not a whole estate.

## The Twelve Factors

| # | Factor | One line |
|---|--------|----------|
| I | Codebase | One repo tracked in version control, many deploys (dev/stage/prod) from it. |
| II | Dependencies | Declare all deps explicitly; isolate them — never rely on system-wide packages. |
| III | Config | Store config that varies per deploy in the **environment**, not in code. |
| IV | Backing services | Treat DBs, queues, caches as **attached resources** addressed by URL/creds. |
| V | Build, release, run | Strictly separate the three stages; releases are immutable and versioned. |
| VI | Processes | Run the app as one or more **stateless**, share-nothing processes. |
| VII | Port binding | Export services by binding to a port; be self-contained, no injected runtime. |
| VIII | Concurrency | Scale **out** via the process model — add processes, don't fatten one. |
| IX | Disposability | Maximize robustness: fast startup, graceful shutdown on SIGTERM. |
| X | Dev/prod parity | Keep dev, staging, prod as similar as possible — same backing services. |
| XI | Logs | Treat logs as **event streams** to stdout; the environment routes/stores them. |
| XII | Admin processes | Run one-off admin/migration tasks as identical, short-lived processes. |

## Most load-bearing here

**III — Config.** The litmus test: could you open-source the repo right now without
leaking a credential? Config is everything that differs between deploys (URLs, ports,
secrets, feature flags). It lives in env vars — not constants, not a checked-in YAML.
This decouples config from code so the *same* artifact runs anywhere.

**IV — Backing services.** Every dependency (Postgres, Redis, Kafka, an SMTP host) is an
attached resource reached purely through a URL + credentials in config. A local Postgres
and a managed cloud one are swapped by changing one env var — no code change. Resources
are pluggable, not hardwired.

**VI — Processes.** Processes are stateless and share-nothing. Any data that must persist
goes to a backing service (DB/cache); never to local disk or in-process memory between
requests. Session state in a process breaks horizontal scaling (X, VIII) and disposability
(IX) — a killed process must lose nothing important.

**IX — Disposability.** Processes start fast (seconds, so scaling and deploys are quick)
and shut down gracefully: on `SIGTERM`, stop accepting new work, finish in-flight requests,
release connections, exit. Crash-only design — a process can die at any instant without
corrupting state, because state lives in backing services (VI).

### In Nexus

Phase 8 wires these together via Docker Compose:

```
# compose injects env; values come from a gitignored .env (NEVER committed)
DB_URL, REDIS_URL, KAFKA_BOOTSTRAP_SERVERS, JWT_SECRET
```

- **III**: secrets and connection strings are read from the environment; the real `.env`
  is gitignored — only an `.env.example` (no live secrets) is tracked.
- **IV**: Postgres, Redis, and Kafka are attached purely by their URLs — swapping the
  broker or DB is an env change, not a code change. See `messaging_orchestrator`.
- **VI**: services hold no per-request state; durable state sits in Postgres/Redis.
- **IX**: containers start fast and handle `SIGTERM` for graceful shutdown; orchestration
  details live in `docker_orchestrator`.
- **XI**: every service logs to **stdout**; Compose/the platform captures the stream.

> Net effect: one image, many deploys — local, CI, prod differ only by injected env.

## References

- Architecture orchestrator: [architecture_fundamentals_orchestrator.md](architecture_fundamentals_orchestrator.md) · `[[architecture_fundamentals_orchestrator]]`
- Lesson that taught it: [lesson_09_containers.md](../../lessons/log/lesson_09_containers.md) · `[[lesson_09_containers]]`
- Docker setup: [docker_orchestrator.md](../../infra/docker/docker_orchestrator.md) · `[[docker_orchestrator]]`
- Backing services in Nexus: [messaging_orchestrator.md](../messaging/messaging_orchestrator.md) · `[[messaging_orchestrator]]`
