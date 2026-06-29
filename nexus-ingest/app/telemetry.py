"""OpenTelemetry init — traces (Session A) + metrics & logs (Session B).

`init_telemetry` MUST run before the asyncpg pool and aiokafka consumer are
built, so the instrumentations patch those libraries before they are
instantiated. The aiokafka instrumentation extracts the W3C `traceparent` from
each message's Kafka headers, so this consumer **continues** the trace that
nexus-api started — the span crosses the Kafka boundary.

All three signals export over OTLP/HTTP to the grafana/otel-lgtm collector
(endpoint from the standard `OTEL_EXPORTER_OTLP_*` env). Logs go through an OTel
`LoggingHandler` so every line carries the active span's `trace_id` → logs
correlate to traces in Grafana. Toggle off for local runs with
`NEXUS_OTEL_ENABLED=false`.
"""

import logging

from fastapi import FastAPI

from app.config import settings

log = logging.getLogger("nexus-ingest")


def init_telemetry(app: FastAPI) -> None:
    if not settings.otel_enabled:
        log.info("OpenTelemetry disabled (NEXUS_OTEL_ENABLED=false)")
        return

    from opentelemetry import metrics, trace
    from opentelemetry.exporter.otlp.proto.http._log_exporter import OTLPLogExporter
    from opentelemetry.exporter.otlp.proto.http.metric_exporter import OTLPMetricExporter
    from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter
    from opentelemetry.instrumentation.aiokafka import AIOKafkaInstrumentor
    from opentelemetry.instrumentation.asyncpg import AsyncPGInstrumentor
    from opentelemetry.instrumentation.fastapi import FastAPIInstrumentor
    from opentelemetry.sdk._logs import LoggerProvider, LoggingHandler
    from opentelemetry.sdk._logs.export import BatchLogRecordProcessor
    from opentelemetry.sdk.metrics import MeterProvider
    from opentelemetry.sdk.metrics.export import PeriodicExportingMetricReader
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

    # Traces
    tp = TracerProvider(resource=resource)
    tp.add_span_processor(BatchSpanProcessor(OTLPSpanExporter()))
    trace.set_tracer_provider(tp)

    # Metrics — periodic push of counters/histograms (incl. FastAPI HTTP metrics)
    reader = PeriodicExportingMetricReader(OTLPMetricExporter())
    metrics.set_meter_provider(MeterProvider(resource=resource, metric_readers=[reader]))

    # Logs — ship records over OTLP with the active trace context attached
    lp = LoggerProvider(resource=resource)
    lp.add_log_record_processor(BatchLogRecordProcessor(OTLPLogExporter()))
    logging.getLogger().addHandler(LoggingHandler(level=logging.INFO, logger_provider=lp))

    FastAPIInstrumentor.instrument_app(app)  # HTTP server spans + metrics
    AIOKafkaInstrumentor().instrument()      # extracts traceparent from headers
    AsyncPGInstrumentor().instrument()  # type: ignore[no-untyped-call]  # spans on pgvector r/w
    log.info("OpenTelemetry traces+metrics+logs initialized for nexus-ingest")
