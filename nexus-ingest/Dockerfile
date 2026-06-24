# syntax=docker/dockerfile:1

# ---- build stage: install deps into a venv with uv ----
FROM python:3.13-slim AS build
COPY --from=ghcr.io/astral-sh/uv:0.11.23 /uv /bin/uv
WORKDIR /app
COPY pyproject.toml ./
COPY app/ app/
RUN uv venv /opt/venv && VIRTUAL_ENV=/opt/venv uv pip install .

# ---- runtime stage: slim Python, non-root, venv only ----
FROM python:3.13-slim AS final
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/* \
 && useradd -r -u 10001 appuser
COPY --from=build --chown=appuser:appuser /opt/venv /opt/venv
WORKDIR /app
COPY --chown=appuser:appuser app/ app/
ENV PATH="/opt/venv/bin:$PATH"
USER appuser
EXPOSE 8081
HEALTHCHECK --interval=10s --timeout=5s --start-period=20s --retries=5 \
  CMD curl -fsS http://localhost:8081/v1/health || exit 1
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8081"]
