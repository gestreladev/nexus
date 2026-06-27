"""Environment-driven settings — parity with nexus-api's application.yaml + env."""

from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    # NEXUS_ENVIRONMENT / NEXUS_PORT override these in any environment.
    model_config = SettingsConfigDict(env_prefix="NEXUS_")

    version: str = "0.9.0"
    environment: str = "development"
    port: int = 8081

    # Messaging — NEXUS_KAFKA_* override these.
    kafka_bootstrap_servers: str = "localhost:9092"
    kafka_document_topic: str = "document.uploaded"
    kafka_group_id: str = "nexus-ingest"

    # Database — claim-check read of documents.content + write/search of chunks.
    db_dsn: str = "postgresql://nexus:nexus@localhost:5433/nexus"

    # Embeddings — NEXUS_EMBEDDING_* override these.
    # provider: local (sentence-transformers) | voyage (API) | fake (tests/offline).
    embedding_provider: str = "local"
    embedding_dim: int = 1024  # must equal document_chunks.embedding's vector(N)
    embedding_model_local: str = "mixedbread-ai/mxbai-embed-large-v1"
    embedding_model_voyage: str = "voyage-3-large"
    voyage_api_key: str = ""

    # Chunking — NEXUS_CHUNK_* override these.
    chunk_size: int = 800
    chunk_overlap: int = 120


settings = Settings()
