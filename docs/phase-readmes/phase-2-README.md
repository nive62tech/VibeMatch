# Phase 2 — Matching Algorithm Prototype

## What was built
- Implemented per-person scoring functions in `research/scripts/scoring_engine.py`:
  vibe match, setting match, energy match, and budget match, each returning a normalized 0-1 score
- Implemented a hard filter that fully excludes candidates matching a person's dislike tags,
  or exceeding their crowd-level tolerance (e.g. "quiet" preference vs a "high" crowd candidate)
- Implemented group aggregation using a variance-penalty average — rewards candidates that are
  decently liked by everyone over candidates loved by one person and disliked by another
- Implemented history-based down-ranking for recently visited candidates
- Built a test notebook (`research/notebooks/01_scoring_algorithm_prototype.ipynb`) validating
  the algorithm against fake profiles and the real seed dataset
- Explicitly tested and verified edge cases: dislike-tag exclusion, energy mismatch penalty,
  and missing vibe-tag handling
- Tuned scoring weights based on observed ranking behavior, with reasoning documented in the notebook

## Key learnings / notes for future phases
- `setting` and `energy_level` fields were not in the original Phase 1 schema/dataset and had to
  be added manually to `seed_venues.json` — Phase 3's Room schema must include these fields
- Current weights (vibe 0.35, setting 0.2, energy 0.2, budget 0.25) are a starting point tuned
  by observation, not fixed — worth re-tuning once real users test the app in later phases
- The variance-penalty aggregation is the core differentiator of this project and must be ported
  to Kotlin exactly as-is in Phase 6, with parity tests to confirm identical output

## Status
Phase 2 complete and pushed to GitHub.