"""Select the embedding strategy from config (the Strategy's context/factory).

Heavy adapters are imported lazily inside each branch so choosing `voyage` never
imports torch, and choosing `local`/`fake` never imports the Voyage SDK.
"""

from app.config import Settings
from app.embeddings.base import EmbeddingStrategy


def build_embedder(settings: Settings) -> EmbeddingStrategy:
    provider = settings.embedding_provider.lower()

    if provider == "local":
        from app.embeddings.local import LocalEmbedder

        return LocalEmbedder(settings.embedding_model_local, settings.embedding_dim)

    if provider == "voyage":
        from app.embeddings.voyage import VoyageEmbedder

        return VoyageEmbedder(
            settings.embedding_model_voyage, settings.embedding_dim, settings.voyage_api_key
        )

    if provider == "fake":
        from app.embeddings.fake import FakeEmbedder

        return FakeEmbedder(settings.embedding_dim)

    raise ValueError(
        f"unknown NEXUS_EMBEDDING_PROVIDER {settings.embedding_provider!r} "
        "(expected: local | voyage | fake)"
    )
