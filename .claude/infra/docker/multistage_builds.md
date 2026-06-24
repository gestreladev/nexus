---
name: multistage_builds
description: Multi-stage Docker build — JDK build stage to slim JRE runtime for the Ktor fat jar.
agent:
  role: infra-specialist
  tier: standard
  weight: medium
  triggers:
    - containerizing nexus-api
    - shrinking a JVM image / separating build from runtime
metadata:
  type: reference
---

# Multi-stage Builds

A multi-stage build uses several `FROM` stages: a heavy **build** stage compiles
the app, then a slim **runtime** stage copies only the artifact. The JDK,
Gradle, and source never ship — the final image is small and has less attack
surface. (Pattern verified against docs.docker.com via Context7.)

## nexus-api Dockerfile (Ktor fat jar)
Mirrors the shipped `nexus-api/Dockerfile` (Temurin **17**, the project baseline).
```dockerfile
# syntax=docker/dockerfile:1

# ---- build stage: full JDK + Gradle ----
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /build
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle/ gradle/
RUN ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true   # cache deps
COPY src/ src/
RUN ./gradlew --no-daemon buildFatJar            # → build/libs/nexus-api-all.jar

# ---- runtime stage: slim JRE, non-root ----
FROM eclipse-temurin:17-jre-jammy AS final
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/* \
 && useradd -r -u 10001 appuser   # curl is for the HEALTHCHECK below
USER appuser
WORKDIR /app
COPY --from=build --chown=appuser:appuser /build/build/libs/nexus-api-all.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=5s --start-period=60s --retries=5 \
  CMD curl -fsS http://localhost:8080/v1/health || exit 1
# Container-aware heap: size from the cgroup limit, fail fast on OOM.
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-XX:+ExitOnOutOfMemoryError", "-jar", "app.jar"]
```

## Why each choice
- **JDK to build, JRE to run** — runtime needs no compiler; the JRE image is far smaller.
- **Deps before source** — layer cache (see [dockerfile.md](dockerfile.md) · `[[dockerfile]]`).
- **`buildFatJar`** — Ktor's plugin bundles app + dependencies into one jar
  (`nexus-api-all.jar`); the runtime stage just runs it.
- **non-root `appuser`** (`useradd -r -u 10001`) — never run as root (see
  [best_practices.md](best_practices.md) · `[[best_practices]]`).
- **`curl` + `HEALTHCHECK`** — image-level readiness so any runtime sees health,
  not only compose; `--start-period` covers JVM boot + Flyway migrations.
- **`-XX:MaxRAMPercentage`** — JVM sizes its heap from the container memory limit,
  not the host's total RAM (pair with a compose `mem_limit`).

## Note
Ktor's plugin also offers `buildImage`/`publishImageToLocalRegistry` tasks. The
explicit Dockerfile above is preferred for control and learning.

## References
- [docker_orchestrator.md](docker_orchestrator.md) · `[[docker_orchestrator]]`
- [compose.md](compose.md) · `[[compose]]`
