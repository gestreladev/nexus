"""Application entry point — the FastAPI analogue of nexus-api's Application.module().

Wires error handlers and versioned routes, then exposes `app` for Uvicorn.
"""

from fastapi import FastAPI

from app.config import settings
from app.errors import install_error_handlers
from app.routes.v1.health import router as health_router


def create_app() -> FastAPI:
    app = FastAPI(title="nexus-ingest", version=settings.version)
    install_error_handlers(app)
    app.include_router(health_router, prefix="/v1")   # all routes under /v1
    return app


app = create_app()
