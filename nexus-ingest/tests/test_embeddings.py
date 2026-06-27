import math

import pytest

from app.config import Settings
from app.embeddings.base import EmbeddingStrategy
from app.embeddings.factory import build_embedder
from app.embeddings.fake import FakeEmbedder


def test_fake_is_deterministic_and_normalized() -> None:
    emb = FakeEmbedder(dim=64)
    a = emb.embed_query("docker compose up")
    b = emb.embed_query("docker compose up")
    assert a == b  # same text → same vector
    assert len(a) == 64
    assert math.isclose(math.sqrt(sum(x * x for x in a)), 1.0, rel_tol=1e-9)


def test_fake_distinguishes_texts() -> None:
    emb = FakeEmbedder(dim=64)
    assert emb.embed_query("alpha") != emb.embed_query("beta")


def test_fake_documents_match_query_for_same_text() -> None:
    emb = FakeEmbedder(dim=32)
    [doc] = emb.embed_documents(["needle"])
    assert doc == emb.embed_query("needle")  # cosine == 1 → search finds it


def test_fake_satisfies_protocol() -> None:
    assert isinstance(FakeEmbedder(dim=8), EmbeddingStrategy)


def test_factory_builds_fake() -> None:
    emb = build_embedder(Settings(embedding_provider="fake", embedding_dim=16))
    assert emb.dimension == 16
    assert isinstance(emb, FakeEmbedder)


def test_factory_rejects_unknown_provider() -> None:
    with pytest.raises(ValueError, match="unknown"):
        build_embedder(Settings(embedding_provider="bogus"))
