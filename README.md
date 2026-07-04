# Vibe Match

An occasion-aware outing planner for people who already know each other — couples, families, and friend groups — built as a local-first Android app.

## What it does
Vibe Match solves group decision paralysis for small, recurring outings (a date, a weekend hangout, a family day out) by combining private multi-person voting, a fair-compromise scoring algorithm, real venue crowd/vibe data, and age-aware activity filtering for mixed-age groups — all running fully offline on-device.

## Why it's impressive
- **Real algorithm, not a wrapper**: uses a weighted, variance-aware scoring function to find genuine group compromises rather than an LLM prompt or a simple average.
- **Validated gap**: built after researching 10+ existing group-planning apps (SwipeSights, Troupe, Roamly, WePlanify, Partiful) and identifying that none combine existing-relationship history, crowd-awareness, and age-based filtering in one tool.
- **Fully offline, local-first architecture**: no cloud dependency, no backend cost, works entirely on-device — a deliberate constraint-driven design decision, not a limitation glossed over.
- **End-to-end ownership**: data pipeline (Python + local LLM tagging via Ollama) → algorithm prototyping (Colab) → production port (Kotlin/Android) → shipped app.

## Tech Stack
- **App**: Kotlin, Jetpack Compose, Room (local database)
- **Algorithm prototyping**: Python, Google Colab
- **Data tagging**: Ollama (local LLM, offline vibe-tagging of seed venue data)
- **Version control**: Git, GitHub
- **IDE**: VSCode (Python/research), Android Studio (app)

## Folder Structure
VibeMatch/
├── android-app/          # Shipped native Android app (Kotlin)
├── research/             # Python + Colab prototyping (not shipped)
├── docs/phase-readmes/   # Per-phase documentation
├── .gitignore
├── LICENSE
└── README.md

## Phase Progress

| Phase | Name | Covers | Status |
|---|---|---|---|
| 0 | Repo & Environment Setup | Repo init, folder structure, Android/Python/Ollama/Colab environment checks | Completed ✅ |
| 1 | Data Design & Seed Dataset Pipeline | Schema design, raw venue collection, Ollama-based vibe tagging, seed dataset | Pending |
| 2 | Matching Algorithm Prototype | Scoring functions, group aggregation, crowd/history logic, Colab testing | Pending |
| 3 | Android App Skeleton & Local Database | Kotlin data classes, Room DB, DAOs, repository layer, seed data import | Pending |
| 4 | Group & Preference Flow | Create group screen, preference setup screen | Pending |
| 5 | Session & Private Voting Flow | Occasion mode selector, sequential private voting UI | Pending |
| 6 | Matching Engine Integration | Port Python scoring logic to Kotlin, parity tests | Pending |
| 7 | Reveal & History Experience | Ranked results screen, explanation text, chart, history screen | Pending |
| 8 | Polish, Testing & Demo Release | Theming, end-to-end testing, demo video, v1.0 release tag | Pending |

## License
MIT