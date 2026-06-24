# syntax=docker/dockerfile:1

# ---- build stage: full JDK + Gradle compile the fat jar ----
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /build
# Copy build files first so the dependency layer caches across source changes.
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle/ gradle/
RUN ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true
COPY src/ src/
RUN ./gradlew --no-daemon buildFatJar

# ---- runtime stage: slim JRE, non-root ----
FROM eclipse-temurin:17-jre-jammy AS final
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/* \
 && useradd -r -u 10001 appuser
USER appuser
WORKDIR /app
COPY --from=build --chown=appuser:appuser /build/build/libs/nexus-api-all.jar app.jar
EXPOSE 8080
# Image-level health so any runtime (plain docker run / Swarm) sees readiness,
# not just compose. --start-period covers JVM boot + Flyway migrations.
HEALTHCHECK --interval=10s --timeout=5s --start-period=60s --retries=5 \
  CMD curl -fsS http://localhost:8080/v1/health || exit 1
# Container-aware heap: size the JVM from the cgroup limit, fail fast on OOM.
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-XX:InitialRAMPercentage=50.0", "-XX:+ExitOnOutOfMemoryError", "-jar", "app.jar"]
