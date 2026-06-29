"""Application entry point — the FastAPI analogue of nexus-api's Application.module().

Wires error handlers, versioned routes, and a Kafka consumer (lifespan-managed),
then exposes `app` for Uvicorn.
"""

import logging
from contextlib import asynccontextmanager
from collections.abc import AsyncIterator

from fastapi import FastAPI

from app.chunking.character import CharacterChunker
from app.config import settings
from app.db import Database
from app.embeddings.factory import build_embedder
from app.errors import install_error_handlers
from app.ingest import Ingestor
from app.messaging.consumer import DocumentConsumer
from app.routes.v1.health import router as health_router
from app.routes.v1.search import router as search_router
from app.telemetry import init_telemetry

logging.basicConfig(level=logging.INFO, format="%(asctime)s %(levelname)s %(name)s: %(message)s")
log = logging.getLogger("nexus-ingest")


@asynccontextmanager
async def lifespan(app: FastAPI) -> AsyncIterator[None]:
    init_telemetry(app)   # FIRST: must patch asyncpg/aiokafka before they're built
    db = Database(settings.db_dsn)
    await db.connect()
    embedder = build_embedder(settings)   # local | voyage | fake — loads the model
    chunker = CharacterChunker(settings.chunk_size, settings.chunk_overlap)
    # /v1/search reads these off app.state (read path).
    app.state.db = db
    app.state.embedder = embedder

    consumer = DocumentConsumer(Ingestor(db, embedder, chunker))   # write path
    try:
        await consumer.start()          # best-effort: a Kafka outage won't down the API
    except Exception as exc:            # noqa: BLE001
        log.warning("Kafka consumer not started: %s", exc)
    try:
        yield
    finally:
        await consumer.stop()
        await db.close()


def create_app() -> FastAPI:
    app = FastAPI(title="nexus-ingest", version=settings.version, lifespan=lifespan)
    install_error_handlers(app)
    app.include_router(health_router, prefix="/v1")
    app.include_router(search_router, prefix="/v1")
    return app


app = create_app()
