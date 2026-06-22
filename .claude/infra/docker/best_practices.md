---
name: best_practices
description: Docker best practices — non-root, slim base, pinning, healthcheck, 12-factor.
agent:
  role: infra-specialist
  tier: standard
  weight: soft
  triggers:
    - hardening a Dockerfile or image
    - reviewing container security/size
metadata:
  type: reference
---

# Docker Best Practices

Distilled from current Docker docs (Context7) and applied to Nexus.

## Security
- **Run as non-root.** Create a user and `USER appuser` — a container process
  shouldn't be root. (See the multi-stage Dockerfile.)
- **Never bake secrets** into images (`ENV PASSWORD=...` is readable in history).
  Pass secrets at runtime via env / secrets.
- **Slim, minimal base** — JRE not JDK at runtime; fewer packages = smaller
  attack surface. Consider `-alpine`/distroless when feasible.
- **Scan images** for known CVEs (`docker scout`, Trivy) in CI.

## Reproducibility
- **Pin versions**, and for strict reproducibility pin a **digest**:
  `eclipse-temurin:21-jre-jammy@sha256:...`. A floating tag can change under you.
- **`# syntax=docker/dockerfile:1`** at the top for stable builder features.

## Size & speed
- **Multi-stage** — ship only the artifact, not the toolchain.
- **`.dockerignore`** — keep `.git`, `build`, `.env` out of the context.
- **Cache-friendly ordering** — deps before source (see
  [dockerfile.md](dockerfile.md) · `[[dockerfile]]`).

## Operability
- **`HEALTHCHECK`** so orchestrators know readiness (Compose `depends_on:
  condition: service_healthy`). `nexus-api` exposes `GET /v1/health` for this.
- **12-factor config** — all config from the environment; one image runs in every
  environment, differing only by env vars. No secrets, no per-env builds.
- **One process per container** — let the orchestrator manage lifecycle/restart.

## Nexus checklist
- [ ] multi-stage, JRE runtime, non-root `appuser`
- [ ] `.dockerignore` excludes `.git`/`build`/`.env`
- [ ] `JWT_SECRET` + DB creds via env, never in the image
- [ ] healthcheck wired to `/v1/health`

## References
- [docker_orchestrator.md](docker_orchestrator.md) · `[[docker_orchestrator]]`
- [multistage_builds.md](multistage_builds.md) · `[[multistage_builds]]`
