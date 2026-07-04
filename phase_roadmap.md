Phase-Wise Roadmap
Phase 0: Repo & Environment Setup

Initialize GitHub repo with the folder structure above
Set up .gitignore (Android + Python + IDE files)
Create Android Studio project skeleton (empty, buildable)
Set up Python virtual environment for research/
Verify Ollama runs locally with a test model pull
Confirm Google Colab can pull the repo (clone or GitHub-mount)
Files: README.md, .gitignore, android-app/ (empty Gradle project), research/requirements.txt

Phase 1: Data Design & Seed Dataset Pipeline

Define informal schema for Person, Preference, Candidate, Session, Vote, History as Python dataclasses
Collect a raw list of 15-20 real local venues/activities into a CSV
Write a script using Ollama locally to auto-generate vibe tags/descriptions from venue descriptions
Produce a finalized seed_venues.json for later Room DB pre-population
Files: research/scripts/ollama_tagger.py, research/data/raw_venues.csv, research/data/seed_venues.json, research/notebooks/02_vibe_tagging_with_ollama.ipynb

Phase 2: Matching Algorithm Prototype

Implement scoring functions (vibe, setting, energy, budget match) in Python
Implement group aggregation with variance penalty (fair-compromise logic)
Implement crowd-level hard filter and history down-ranking logic
Test against sample fake profiles/candidates in a Colab notebook
Files: research/scripts/scoring_engine.py, research/notebooks/01_scoring_algorithm_prototype.ipynb

Phase 3: Android App Skeleton & Local Database

Set up Kotlin data classes for all entities
Set up Room database, DAOs, and entities
Set up Repository layer wrapping Room access
Pre-populate Room DB from seed_venues.json on first launch
Files: data/local/*.kt, data/model/*.kt, data/repository/*.kt

Phase 4: Group & Preference Flow

Build "Create Group" screen (add people locally with name/age)
Build per-person preference setup screen (vibe, setting, energy, budget, dislikes)
Wire screens to Repository/Room
Files: ui/group/*.kt, ui/preferences/*.kt

Phase 5: Session & Private Voting Flow

Build "New Session" screen (occasion mode selector, triggers candidate list load)
Build sequential private voting screens (lock previous answers, "hand phone to next person" transition)
Store votes to Room DB
Files: ui/session/*.kt, ui/voting/*.kt

Phase 6: Matching Engine Integration

Port scoring_engine.py logic into Kotlin (ScoringEngine.kt, Aggregator.kt)
Implement crowd filter, history down-rank, and family age-based filter in Kotlin
Write unit tests comparing Kotlin output to Python prototype output for parity
Files: domain/matching/ScoringEngine.kt, domain/matching/Aggregator.kt, test files

Phase 7: Reveal & History Experience

Build Reveal screen: ranked results + generated explanation text + simple chart
Build History screen: past outings list, "mark as done" writes to history table
Files: ui/reveal/*.kt, ui/history/*.kt

Phase 8: Polish, Testing & Demo Release

Add app icon, theming, empty/error states
End-to-end test on emulator/device
Record demo video for LinkedIn
Tag v1.0 release on GitHub
Files: README.md (final polish), CHANGELOG.md, app icon/theme assets