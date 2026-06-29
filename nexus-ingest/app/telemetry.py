"""OpenTelemetry init — traces (Session A of Phase 10).

`init_telemetry` MUST run before the asyncpg pool and aiokafka consumer are
built, so the instrumentations patch those libraries before they are
instantiated. The aiokafka instrumentation then extracts the W3C `traceparent`
from each message's Kafka headers, so this consumer **continues** the trace that
nexus-api started — the span crosses the Kafka boundary automatically.

Exporter endpoint/protocol come from the standard `OTEL_EXPORTER_OTLP_*` env
(set in docker-compose to the grafana/otel-lgtm collector). Toggle the whole
thing off for local non-docker runs with `NEXUS_OTEL_ENABLED=false`.
"""

import logging

from fastapi import FastAPI

from app.config import settings

log = logging.getLogger("nexus-ingest")


def init_telemetry(app: FastAPI) -> None:
    if not settings.otel_enabled:
        log.info("OpenTelemetry disabled (NEXUS_OTEL_ENABLED=false)")
        return

    from opentelemetry import trace
    from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter
    from opentelemetry.instrumentation.aiokafka import AIOKafkaInstrumentor
    from opentelemetry.instrumentation.asyncpg import AsyncPGInstrumentor
    from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor
    from opentelemetry.sdk.resources import Resource
    from opentelemetry.sdk.trace import TracerProvider
    from opentelemetry.sdk.trace.export import BatchSpanProcessor

    resource = Resource.create(
        {
            "service.name": "nexus-ingest",
            "service.namespace": "nexus",
            "service.version": settings.version,
            "deployment.environment.name": settings.environment,
        }
    )
    provider = TracerProvider(resource=resource)
    provider.add_span_processor(BatchSpanProcessor(OTLPSpanExporter()))  # endpoint from OTEL_* env
    trace.set_tracer_provider(provider)

    FastAPIInstrumentor.instrument_app(app)  # HTTP server spans (/v1/search, /v1/health)
    AIOKafkaInstrumentor().instrument()      # extracts traceparent from message headers
    AsyncPGInstrumentor().instrument()  # type: ignore[no-untyped-call]  # spans on pgvector r/w
    log.info("OpenTelemetry tracing initialized for nexus-ingest")
