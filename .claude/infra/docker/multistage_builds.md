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
```dockerfile
# syntax=docker/dockerfile:1

# ---- build stage: full JDK + Gradle ----
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /build
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle/ gradle/
RUN ./gradlew dependencies --no-daemon || true   # cache deps
COPY src/ src/
RUN ./gradlew buildFatJar --no-daemon            # → build/libs/nexus-api-all.jar

# ---- runtime stage: slim JRE, non-root ----
FROM eclipse-temurin:21-jre-jammy AS final
ARG UID=10001
RUN adduser --disabled-password --gecos "" --no-create-home --uid "${UID}" appuser
USER appuser
WORKDIR /app
COPY --from=build /build/build/libs/nexus-api-all.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Why each choice
- **JDK to build, JRE to run** — runtime needs no compiler; the JRE image is far smaller.
- **Deps before source** — layer cache (see [dockerfile.md](dockerfile.md) · `[[dockerfile]]`).
- **`buildFatJar`** — Ktor's plugin bundles app + dependencies into one jar
  (`nexus-api-all.jar`); the runtime stage just runs it.
- **non-root `appuser`** — never run as root (see [best_practices.md](best_practices.md) · `[[best_practices]]`).

## Note
Ktor's plugin also offers `buildImage`/`publishImageToLocalRegistry` tasks. The
explicit Dockerfile above is preferred for control and learning.

## References
- [docker_orchestrator.md](docker_orchestrator.md) · `[[docker_orchestrator]]`
- [compose.md](compose.md) · `[[compose]]`
