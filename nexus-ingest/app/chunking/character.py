"""Fixed-size character chunker with overlap.

Why overlap: a chunk boundary can fall mid-idea; repeating the last `overlap`
characters at the start of the next chunk keeps a concept's context intact in at
least one chunk, so retrieval doesn't miss it. A simple, deterministic default —
swap in a sentence/recursive splitter via the ChunkingStrategy contract later.
"""


class CharacterChunker:
    def __init__(self, size: int, overlap: int) -> None:
        if size <= 0:
            raise ValueError("chunk size must be positive")
        if overlap < 0 or overlap >= size:
            raise ValueError("overlap must satisfy 0 <= overlap < size")
        self._size = size
        self._overlap = overlap

    def chunk(self, text: str) -> list[str]:
        text = text.strip()
        if not text:
            return []
        step = self._size - self._overlap
        return [text[i : i + self._size] for i in range(0, len(text), step)]
