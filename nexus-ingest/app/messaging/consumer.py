"""Kafka consumer — subscribes to document.uploaded and processes each event.

Runs as a background asyncio task started/stopped by the FastAPI lifespan.
Processing is **idempotent** (Kafka is at-least-once): re-handling an event must
be safe. For now it logs; the status update + embedding land in Phase 9.
"""

import asyncio
import json
import logging

from aiokafka import AIOKafkaConsumer

from app.config import settings

log = logging.getLogger("nexus-ingest")


class DocumentConsumer:
    def __init__(self) -> None:
        self._consumer: AIOKafkaConsumer | None = None
        self._task: asyncio.Task[None] | None = None

    async def start(self) -> None:
        consumer = AIOKafkaConsumer(
            settings.kafka_document_topic,
            bootstrap_servers=settings.kafka_bootstrap_servers,
            group_id=settings.kafka_group_id,
            auto_offset_reset="earliest",
            enable_auto_commit=True,
        )
        await consumer.start()
        self._consumer = consumer
        self._task = asyncio.create_task(self._run())
        log.info("Kafka consumer started on '%s'", settings.kafka_document_topic)

    async def _run(self) -> None:
        assert self._consumer is not None
        try:
            async for msg in self._consumer:
                self._handle(msg.value, msg.partition, msg.offset)
        except asyncio.CancelledError:
            pass

    def _handle(self, raw: bytes, partition: int, offset: int) -> None:
        try:
            event = json.loads(raw)
        except json.JSONDecodeError:
            log.warning("skipping non-JSON message at %s@%s", partition, offset)
            return
        # Phase 9: update documents.status → processing → ready + embed.
        log.info(
            "consumed document.uploaded id=%s title=%r (partition %s offset %s)",
            event.get("documentId"),
            event.get("title"),
            partition,
            offset,
        )

    async def stop(self) -> None:
        if self._task is not None:
            self._task.cancel()
        if self._consumer is not None:
            await self._consumer.stop()
        log.info("Kafka consumer stopped")
