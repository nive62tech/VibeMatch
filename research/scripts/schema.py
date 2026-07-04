from dataclasses import dataclass, field
from typing import Optional

@dataclass
class Preference:
    person_id: str
    vibe: list[str]          # e.g. ["cozy", "quiet"]
    setting: str              # "indoor" | "outdoor" | "either"
    energy_level: int         # 1-5
    budget_range: str         # "low" | "medium" | "high"
    cuisine_tags: list[str] = field(default_factory=list)
    dislike_tags: list[str] = field(default_factory=list)

@dataclass
class Person:
    id: str
    display_name: str
    age: Optional[int] = None

@dataclass
class Candidate:
    id: str
    name: str
    category: str             # "cafe" | "restaurant" | "park" | "activity" | "game"
    description: str
    vibe_tags: list[str] = field(default_factory=list)
    crowd_level: Optional[str] = None   # "low" | "medium" | "high"
    price_level: Optional[str] = None
    min_age: Optional[int] = None