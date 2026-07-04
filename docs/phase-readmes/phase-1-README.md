# Phase 1 — Data Design & Seed Dataset Pipeline

## What was built
- Defined core data schema as Python dataclasses: Person, Preference, Candidate, PlanningSession, Vote, OutingHistory (`research/scripts/schema.py`)
- Collected a raw dataset of real local venues/activities into `research/data/raw_venues.csv`, covering a mix of cafes, restaurants, parks, and activity spots with varied vibes
- Built `research/scripts/ollama_tagger.py` — uses a local Ollama model to auto-generate vibe tags and crowd-level estimates from each venue's description
- Debugged and hardened the tagging script against malformed/missing API responses (added defensive error handling around the Ollama API call)
- Manually reviewed and corrected the LLM-generated tags for accuracy
- Produced the finalized `research/data/seed_venues.json` — the dataset that will seed the Android Room database in Phase 3

## Key learnings / notes for future phases
- Ollama's local model responses aren't always clean JSON — defensive parsing (checking for the `response` key, catching `JSONDecodeError`) is necessary and should carry over to any future local-LLM calls in this project
- Schema field names in `schema.py` are the source of truth — Phase 3's Kotlin data classes should mirror these exactly to avoid mapping bugs later
- Manual review of LLM tags was necessary — small local models occasionally produce inconsistent or off-topic tags, so this dataset is LLM-assisted but human-verified

## Status
Phase 1 complete and pushed to GitHub.