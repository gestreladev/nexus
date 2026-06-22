---
name: dockerfile
description: Dockerfile instructions, image layers, build cache, and .dockerignore.
agent:
  role: infra-specialist
  tier: standard
  weight: soft
  triggers:
    - writing or reviewing a Dockerfile
    - optimizing build cache
metadata:
  type: reference
---

# Dockerfile

A Dockerfile is a recipe: each instruction creates a cached **layer**. An image
is a stack of read-only layers; a container adds a thin writable layer on top.

## Core instructions
| Instruction | Purpose |
|---|---|
| `FROM` | base image (start of a stage) |
| `WORKDIR` | set/create the working directory |
| `COPY` | copy files from build context into the image |
| `RUN` | execute a command at **build** time → new layer |
| `ENV` | environment variable baked into the image |
| `EXPOSE` | document the listening port (metadata only) |
| `ENTRYPOINT` / `CMD` | the process run at **container start** |

## Layer caching — order matters
Docker reuses a layer's cache until an input changes; everything after a changed
layer rebuilds. So copy **rarely-changing** things first:

```dockerfile
# 1. deps change rarely → copy build files + resolve deps first (cached)
COPY build.gradle.kts settings.gradle.kts gradle/ ./
RUN ./gradlew dependencies --no-daemon
# 2. source changes often → copy last
COPY src/ src/
RUN ./gradlew buildFatJar --no-daemon
```
Reorder these and every source edit re-downloads all dependencies.

## .dockerignore
Excludes paths from the build context (smaller, faster, safer):
```
.git
build
.gradle
.env
**/*.md
```
Never ship `.env`, `.git`, or local build output into an image.

## In Nexus
The real `nexus-api` image uses a multi-stage build — see
[multistage_builds.md](multistage_builds.md) · `[[multistage_builds]]`.

## References
- [docker_orchestrator.md](docker_orchestrator.md) · `[[docker_orchestrator]]`
- [best_practices.md](best_practices.md) · `[[best_practices]]`
