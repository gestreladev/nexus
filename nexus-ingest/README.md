# nexus-ingest

Document ingestion service — parse, chunk, embed. Python / FastAPI twin of
`nexus-api`. Python **3.13**.

## Setup

```bash
cd nexus-ingest
python3.13 -m venv .venv
source .venv/bin/activate
pip install -e ".[dev]"
```

## Run

```bash
uvicorn app.main:app --reload --port 8081
# → GET http://localhost:8081/v1/health
```

## Test

```bash
pytest          # unit tests, no external services
ruff check .    # lint
mypy app        # static type checking
```

## Layout

```
app/
├── main.py            entry point (create_app + Uvicorn `app`)
├── config.py          env-driven Settings (pydantic-settings)
├── errors.py          ErrorResponse + handlers (parity with StatusPages)
└── routes/v1/         versioned routers (health.py)
tests/                 pytest
```
