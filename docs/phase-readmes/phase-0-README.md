# Phase 0 — Repo & Environment Setup

## What was built
- Initialized GitHub repository `VibeMatch` with MIT license
- Created base folder structure: `android-app/`, `research/` (notebooks, scripts, data), `docs/phase-readmes/`
- Added `.gitignore` covering Android, Python, and OS/IDE artifacts
- Created empty Android Studio project (Kotlin + Compose, package `com.vibematch`, min SDK 26) — confirmed it builds and runs on emulator/device
- Set up Python virtual environment in `research/` with `requirements.txt` (pandas, requests, jupyter) — confirmed Jupyter launches
- Verified Ollama installation and resolved a RAM allocation failure on `llama3.2:3b` by switching to a lighter local model suited to the machine's available memory
- Verified Google Colab can clone and access the GitHub repo

## Environment notes for future reference
- Local LLM tagging (Phase 1) will run on a smaller Ollama model due to RAM constraints — documented so future phases don't assume 3B+ model availability
- No cloud/backend used anywhere — app is fully local-first by design

## Status
Phase 0 complete and pushed to GitHub.