import pytest

from app.chunking.character import CharacterChunker


def test_empty_text_yields_no_chunks() -> None:
    assert CharacterChunker(size=100, overlap=10).chunk("   ") == []


def test_short_text_is_one_chunk() -> None:
    assert CharacterChunker(size=100, overlap=10).chunk("hello world") == ["hello world"]


def test_overlap_repeats_tail_in_next_chunk() -> None:
    text = "abcdefghij"  # len 10
    chunks = CharacterChunker(size=6, overlap=2).chunk(text)
    # step = 4 → starts at 0, 4, 8
    assert chunks == ["abcdef", "efghij", "ij"]
    # the 2-char overlap ("ef") appears at the end of chunk 0 and start of chunk 1
    assert chunks[0][-2:] == chunks[1][:2]


def test_covers_entire_text() -> None:
    text = "x" * 1000
    chunks = CharacterChunker(size=300, overlap=50).chunk(text)
    assert "".join(c[:250] for c in chunks).startswith("x")
    assert sum(len(c) for c in chunks) >= len(text)  # overlap means >= original


def test_invalid_overlap_rejected() -> None:
    with pytest.raises(ValueError):
        CharacterChunker(size=100, overlap=100)
    with pytest.raises(ValueError):
        CharacterChunker(size=0, overlap=0)
