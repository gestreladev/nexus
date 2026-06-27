# syntax=docker/dockerfile:1

# ---- build stage: install deps into a venv with uv ----
# embed-local bundles sentence-transformers + torch; the model is pre-downloaded
# into HF_HOME so container startup loads from disk instead of the network.
FROM python:3.13-slim AS build
COPY --from=ghcr.io/astral-sh/uv:0.11.23 /uv /bin/uv
ENV HF_HOME=/opt/hf
WORKDIR /app
COPY pyproject.toml ./
COPY app/ app/
RUN uv venv /opt/venv && VIRTUAL_ENV=/opt/venv uv pip install ".[embed-local]"
RUN /opt/venv/bin/python -c "from sentence_transformers import SentenceTransformer; \
SentenceTransformer('mixedbread-ai/mxbai-embed-large-v1')"

# ---- runtime stage: slim Python, non-root, venv + baked model ----
FROM python:3.13-slim AS final
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/* \
 && useradd -r -u 10001 appuser
ENV HF_HOME=/opt/hf
COPY --from=build --chown=appuser:appuser /opt/venv /opt/venv
COPY --from=build --chown=appuser:appuser /opt/hf /opt/hf
WORKDIR /app
COPY --chown=appuser:appuser app/ app/
ENV PATH="/opt/venv/bin:$PATH"
USER appuser
EXPOSE 8081
# Loading the 1024-dim model into RAM takes a bit, so allow a longer warmup.
HEALTHCHECK --interval=10s --timeout=5s --start-period=120s --retries=6 \
  CMD curl -fsS http://localhost:8081/v1/health || exit 1
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8081"]
