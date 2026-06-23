"""Structured errors — the single error shape, mirroring nexus-api's StatusPages.

Every error returns `{ "error": "CODE", "message": "..." }` and never leaks
internals. Routes raise AppError; the catch-all maps anything else to 500.
"""

import logging

from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
from pydantic import BaseModel

log = logging.getLogger("nexus-ingest")


class ErrorResponse(BaseModel):
    error: str
    message: str


class AppError(Exception):
    """Raise for an expected, client-facing failure."""

    def __init__(self, status_code: int, error: str, message: str) -> None:
        self.status_code = status_code
        self.error = error
        self.message = message


def install_error_handlers(app: FastAPI) -> None:
    @app.exception_handler(AppError)
    async def _app_error(_: Request, exc: AppError) -> JSONResponse:
        return JSONResponse(
            status_code=exc.status_code,
            content=ErrorResponse(error=exc.error, message=exc.message).model_dump(),
        )

    @app.exception_handler(Exception)
    async def _unexpected(_: Request, exc: Exception) -> JSONResponse:
        log.error("Unhandled exception", exc_info=exc)
        return JSONResponse(
            status_code=500,
            content=ErrorResponse(
                error="INTERNAL_ERROR", message="An unexpected error occurred"
            ).model_dump(),
        )
