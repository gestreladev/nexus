"""Environment-driven settings — parity with nexus-api's application.yaml + env."""

from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    # NEXUS_ENVIRONMENT / NEXUS_PORT override these in any environment.
    model_config = SettingsConfigDict(env_prefix="NEXUS_")

    version: str = "0.6.0"
    environment: str = "development"
    port: int = 8081

    # Messaging — NEXUS_KAFKA_* override these.
    kafka_bootstrap_servers: str = "localhost:9092"
    kafka_document_topic: str = "document.uploaded"
    kafka_group_id: str = "nexus-ingest"


settings = Settings()
