# Vibe Match — Technical Specification

An occasion-aware outing planner for people who already know each other (couples, families, friend groups). Solves group/pair decision paralysis using private preference input, history-aware suggestions, crowd-data filtering, and age-aware activity logic.

---

## 1. Tech Stack

| Layer | Choice | Why |
|---|---|---|
| Frontend + Backend | **Next.js 14 (App Router)** | Single codebase, deploys natively on Vercel, API routes double as backend |
| Hosting | **Vercel** | Free tier, auto-deploy from GitHub, instant shareable URL |
| Database | **Vercel Postgres** (or Supabase free tier as backup) | Relational data fits this well (users, groups, votes, history) |
| ORM | **Prisma** | Type-safe queries, easy schema migrations, works cleanly with Postgres on Vercel |
| Venue/crowd data | **Google Places API** (Place Details + Popular Times via third-party wrapper, since Google doesn't expose Popular Times officially — use `populartimes` scraping library or a paid wrapper API) | Real crowd-level signal, not just category matching |
| Auth | **No full auth — magic-link/shareable-code sessions** | Keeps weekend scope realistic; each "group" gets a unique code, each person gets a session tied to that code + a name, no password/email flow needed |
| Styling | **Tailwind CSS** | Fast to build, works well in Vercel/Next.js default setup |
| Charts/visuals (optional, for the "why this won" reveal) | **Recharts** | Simple satisfaction-score bar/radar chart |

---

## 2. Core Concept Recap

- Two or more people link into a shared "Group" via a code.
- Each person has a **Preference Profile** (reusable across sessions).
- Each planning session has an **Occasion Mode**: Date / Weekend Hangout / Family Outing / One-Day Trip — this changes the weighting logic.
- Each person answers privately; a **matching algorithm** scores candidate venues/activities against both profiles.
- The app **remembers past picks** for that specific group and down-weights repeats.
- Family mode adds an **age-based activity filter**.
- Output is a **ranked shortlist with a "why this fits" explanation**, not just one silent answer.

---

## 3. Data Model (Prisma schema outline)

```prisma
model Group {
  id            String   @id @default(cuid())
  code          String   @unique   // shareable join code, e.g. "sunset-otter-42"
  name          String?            // optional group nickname
  occasionType  String             // "date" | "weekend" | "family" | "trip" — default mode, can be overridden per session
  createdAt     DateTime @default(now())
  members       Person[]
  sessions      PlanningSession[]
  history       OutingHistory[]
}

model Person {
  id           String   @id @default(cuid())
  groupId      String
  group        Group    @relation(fields: [groupId], references: [id])
  displayName  String
  age          Int?               // used for family-mode activity filtering
  preferences  Preference?
  votes        Vote[]
}

model Preference {
  id            String  @id @default(cuid())
  personId      String  @unique
  person        Person  @relation(fields: [personId], references: [id])
  vibe          String  // "cozy" | "lively" | "party" | "quiet" — could be multi-select stored as JSON array
  setting       String  // "indoor" | "outdoor" | "either"
  energyLevel   Int     // 1-5 scale, relaxed to adventurous
  budgetRange   String  // "low" | "medium" | "high"
  cuisineTags   String[] // optional, for date/hangout food preference
  dislikeTags   String[] // hard filters, e.g. "no-loud-music"
}

model PlanningSession {
  id            String   @id @default(cuid())
  groupId       String
  group         Group    @relation(fields: [groupId], references: [id])
  occasionMode  String   // can override group default per session
  status        String   // "collecting" | "revealed" | "closed"
  createdAt     DateTime @default(now())
  candidates    Candidate[]
  votes         Vote[]
}

model Candidate {
  id            String   @id @default(cuid())
  sessionId     String
  session       PlanningSession @relation(fields: [sessionId], references: [id])
  placeId       String?         // Google Place ID, if venue-based
  name          String
  category      String          // "cafe" | "restaurant" | "park" | "activity" | "game" etc.
  vibeTags      String[]        // tagged at ingestion time
  crowdLevel    String?         // "low" | "medium" | "high" — pulled from Popular Times or manually tagged
  minAge        Int?            // for family/activity filtering
  votes         Vote[]
}

model Vote {
  id            String   @id @default(cuid())
  sessionId     String
  session       PlanningSession @relation(fields: [sessionId], references: [id])
  personId      String
  person        Person   @relation(fields: [personId], references: [id])
  candidateId   String
  candidate     Candidate @relation(fields: [candidateId], references: [id])
  score         Int       // 1-5 private rating, or like/dislike as 1/0
  createdAt     DateTime @default(now())
}

model OutingHistory {
  id            String   @id @default(cuid())
  groupId       String
  group         Group    @relation(fields: [groupId], references: [id])
  candidateName String
  visitedAt     DateTime @default(now())
}
```

---

## 4. The Matching Algorithm (the actual "engineering" part)

This is the piece that makes it more than a wrapper — a weighted multi-user scoring function, not an LLM call.

**Step 1 — Candidate generation**
Pull venues from Google Places (Nearby Search) filtered by category relevant to the occasion mode. Tag each with vibe attributes (either from Google's own type/attribute fields — e.g. "cozy," "good for groups" — or a manual tagging pass you do for your demo dataset, since fine-grained vibe tags aren't reliably available from the API).

**Step 2 — Per-person scoring**
For each candidate, compute a score against each person's Preference profile:

```
score(person, candidate) =
    w1 * vibeMatch(person.vibe, candidate.vibeTags) +
    w2 * settingMatch(person.setting, candidate.setting) +
    w3 * energyMatch(person.energyLevel, candidate.energyLevel) +
    w4 * budgetMatch(person.budgetRange, candidate.priceLevel) -
    penalty if candidate.tags intersects person.dislikeTags (hard filter, sets score to 0)
```

Weights (w1-w4) can start as equal (0.25 each) — a nice resume-line is "tuned weighting through iteration," even if you just hand-tune them during testing.

**Step 3 — Group aggregation (the "fair" part)**
Instead of averaging (which lets one enthusiastic vote drown out a mild dislike), use a **min-satisfaction-aware aggregation**:

```
groupScore(candidate) = average(all person scores) - variance_penalty * stdev(all person scores)
```

This rewards candidates that are *decently liked by everyone* over ones that are *loved by one person, disliked by another* — same principle as the earlier "fair division" idea, just applied pairwise/small-group instead of full Borda count (simpler to implement for 2-6 people).

**Step 4 — Crowd-level filter**
If any person's profile has a hard "quiet/cozy" preference, filter out candidates flagged `crowdLevel: "high"` before scoring, rather than just down-weighting — this is your "checks if a place will be crowded" feature.

**Step 5 — History down-ranking**
```
finalScore(candidate) = groupScore(candidate) * (candidate visited in last N outings ? 0.5 : 1.0)
```

**Step 6 — Age-based filter (family mode only)**
If `occasionMode === "family"`, filter candidates by `minAge`/`maxAge` fit against the ages present in the group — e.g. a board-game candidate tagged for ages 8+ gets excluded if a 5-year-old is in the group; suggest age-bracket-appropriate games/activities from a small curated list you seed yourself (board games, park activities, etc., tagged by age range).

**Output:** Top 3 ranked candidates + a short generated explanation string built from the score breakdown (not an LLM — just template text like: `"${name} scored high because it matched ${personA}'s love of quiet spaces and ${personB}'s budget range, and you haven't been in 2 months."`).

---

## 5. App Sections / Pages

1. **Landing page** — explain the concept, "Create a Group" / "Join a Group" (enter code).
2. **Group creation** — set group name, occasion type default, generates shareable code/link.
3. **Join flow** — enter code, enter display name (and age, if family mode).
4. **Preference setup** (first time per person) — short tag-based form: vibe, setting, energy, budget, dislikes. Stored against that person permanently in the group.
5. **New session screen** — organizer picks occasion mode for this round (can differ from group default), triggers candidate generation.
6. **Private voting screen** — each person, on their own device/link, rates the candidate list (1-5 or like/skip), cannot see others' votes.
7. **Waiting screen** — "waiting for 2 of 3 people to finish voting" (simple polling or websocket-lite via periodic fetch).
8. **Reveal screen** — top 3 ranked results with the explanation text, satisfaction chart (Recharts bar showing each person's score per top candidate), and a "mark as done / add to history" button.
9. **History page** — list of past outings for this group, so members can see what's already been done (also transparently shows why suggestions were down-ranked).
10. **Profile edit page** — update your own preference tags anytime.

---

## 6. API Routes (Next.js App Router `app/api/...`)

- `POST /api/groups` — create group, returns code
- `POST /api/groups/[code]/join` — add a person to a group
- `POST /api/people/[id]/preferences` — save/update preference profile
- `POST /api/groups/[code]/sessions` — start new planning session, triggers candidate fetch from Google Places
- `GET /api/sessions/[id]/candidates` — fetch candidate list for voting
- `POST /api/sessions/[id]/vote` — submit a person's private vote
- `GET /api/sessions/[id]/status` — poll whether all members have voted
- `GET /api/sessions/[id]/reveal` — run the scoring algorithm server-side, return ranked results + explanation
- `POST /api/groups/[code]/history` — log a completed outing

---

## 7. Crowd-Data Integration Notes

- Google's official Places API does **not** cleanly expose "Popular Times" data through documented endpoints — you'll need either: (a) a manually-tagged demo dataset (fastest, fine for a weekend demo), or (b) a third-party wrapper library that scrapes/exposes this signal (slower, riskier for a weekend, treat as a stretch goal).
- For the actual weekend build, **manually tagging 15-20 real local venues** with a `crowdLevel` and `vibeTags` field yourself is the realistic path — it's honest to describe in a demo as "seeded with real local venue data" rather than faking a live API integration you don't have time to build properly.

---

## 8. Weekend Build Plan (day-by-day)

**Day 1 — Foundation**
- Next.js project scaffold, Tailwind setup, Prisma schema + Vercel Postgres connection
- Group creation + join flow + preference setup form
- Deploy early (even half-broken) to Vercel so you're testing the real shareable-link flow from hour one

**Day 2 — Core logic**
- Candidate seeding (manual dataset of local venues/activities, tagged with vibe/crowd/age fields)
- Voting screen + vote storage
- Scoring algorithm (Steps 1-6 above) as a server-side function, callable from the reveal API route

**Day 3 — Polish + demo**
- Reveal screen with ranked results, explanation text, satisfaction chart
- History page + down-ranking logic wired in
- Family mode age-filtering tested with a fake multi-age group
- Record a demo: create a group, two people vote privately on their phones, reveal screen shows the pick and why — this is your LinkedIn video

---

## 9. Resume-Ready Description (for later)

> "Designed and built a preference-matching outing planner that aggregates private multi-user input using a variance-aware scoring algorithm (rewarding group-wide fit over single-user enthusiasm), integrates real venue data with crowd-level and vibe filtering, and applies age-aware activity logic for mixed-age groups. Validated the product gap through competitive research across 10+ existing group-planning apps before building. Built with Next.js, Prisma, Postgres, deployed on Vercel."

---

## 10. Realistic Cuts if Time Runs Short

If Day 3 is tight, cut in this order (least to most damaging to the demo):
1. Age-based family filtering (keep it for date/hangout mode only)
2. History down-ranking (can hardcode "you haven't been here before" for demo)
3. Satisfaction chart visualization (explanation text alone still lands)
4. Multi-mode occasion switching (ship with just "Date" and "Hangout" modes, skip Family/Trip for v1)

Never cut: the private voting + reveal mechanism — that's the actual differentiator.
