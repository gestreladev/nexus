"""Application entry point — the FastAPI analogue of nexus-api's Application.module().

Wires error handlers, versioned routes, and a Kafka consumer (lifespan-managed),
then exposes `app` for Uvicorn.
"""

import logging
from contextlib import asynccontextmanager
from collections.abc import AsyncIterator

from fastapi import FastAPI

from app.config import settings
from app.errors import install_error_handlers
from app.messaging.consumer import DocumentConsumer
from app.routes.v1.health import router as health_router

logging.basicConfig(level=logging.INFO, format="%(asctime)s %(levelname)s %(name)s: %(message)s")
log = logging.getLogger("nexus-ingest")


@asynccontextmanager
async def lifespan(_: FastAPI) -> AsyncIterator[None]:
    consumer = DocumentConsumer()
    try:
        await consumer.start()          # best-effort: a Kafka outage won't down the API
    except Exception as exc:            # noqa: BLE001
        log.warning("Kafka consumer not started: %s", exc)
    try:
        yield
    finally:
        await consumer.stop()


def create_app() -> FastAPI:
    app = FastAPI(title="nexus-ingest", version=settings.version, lifespan=lifespan)
    install_error_handlers(app)
    app.include_router(health_router, prefix="/v1")
    return app


app = create_app()
