VibeMatch/
├── android-app/                        # Shipped native Android app (Kotlin)
│   └── app/
│       └── src/main/java/com/vibematch/
│           ├── data/
│           │   ├── local/              # Room entities, DAOs, Database class
│           │   ├── model/              # Data classes: Person, Preference, Candidate, GroupSession, Vote, HistoryEntry
│           │   └── repository/         # Repository layer abstracting Room access
│           ├── domain/
│           │   └── matching/           # ScoringEngine.kt, Aggregator.kt (ported from Python prototype)
│           ├── ui/
│           │   ├── group/              # Create/manage group members
│           │   ├── preferences/        # Per-person preference setup
│           │   ├── session/            # New session + occasion mode selection
│           │   ├── voting/             # Sequential private voting (pass-the-phone flow)
│           │   ├── reveal/             # Ranked results + explanation + chart
│           │   └── history/            # Past outings list
│           └── VibeMatchApp.kt
├── research/                           # Python + Colab prototyping (not shipped in app)
│   ├── notebooks/
│   │   ├── 01_scoring_algorithm_prototype.ipynb
│   │   └── 02_vibe_tagging_with_ollama.ipynb
│   ├── scripts/
│   │   ├── scoring_engine.py           # Reference implementation, later ported to Kotlin
│   │   └── ollama_tagger.py            # Local Ollama calls to auto-tag venue vibes
│   ├── data/
│   │   ├── raw_venues.csv
│   │   └── seed_venues.json            # Final tagged dataset, imported into Room DB
│   └── requirements.txt
├── docs/
│   └── phase-readmes/
│       ├── phase-0-README.md
│       ├── phase-1-README.md
│       └── ... (one per completed phase)
├── .gitignore
├── LICENSE
└── README.md