# WarChest 
### An API that finds MLB's best bargains and worst contracts via $ / WAR.

Try it live: https://warchest-web.onrender.com/api/v1/players/value?season=2026

---

#### Example from the 2026 season, getting the five players in baseball that provide the most bang for their buck:
Curl command:
```bash
curl -s 'https://warchest-web.onrender.com/api/v1/players/value?season=2026' | jq '.[:5]'
```
Output
```json
[
  {
    "mlbPlayerId": 691718,
    "fullName": "Pete Crow-Armstrong",
    "season": 2026,
    "teamAbbrev": "CHC",
    "war": 5.8,
    "salaryUsd": 894000,
    "dollarsPerWar": 154138
  },
  {
    "mlbPlayerId": 805808,
    "fullName": "Kevin McGonigle",
    "season": 2026,
    "teamAbbrev": "DET",
    "war": 4.5,
    "salaryUsd": 780000,
    "dollarsPerWar": 173333
  },
  {
    "mlbPlayerId": 669003,
    "fullName": "Garrett Mitchell",
    "season": 2026,
    "teamAbbrev": "MIL",
    "war": 1.9,
    "salaryUsd": 950000,
    "dollarsPerWar": 500000
  },
  {
    "mlbPlayerId": 691016,
    "fullName": "Tyler Soderstrom",
    "season": 2026,
    "teamAbbrev": "ATH",
    "war": 2.4,
    "salaryUsd": 1285714,
    "dollarsPerWar": 535714
  },
  {
    "mlbPlayerId": 678882,
    "fullName": "Ceddanne Rafaela",
    "season": 2026,
    "teamAbbrev": "BOS",
    "war": 4.1,
    "salaryUsd": 2250000,
    "dollarsPerWar": 548780
  }
]
```
---
#### Ronald Acuña Jr.'s player values from 2021-2026, given his notoriously cheap contract:
Curl command:
```bash
curl -s 'https://warchest-web.onrender.com/api/v1/players/career?name=Ronald%20Acu%C3%B1a%20Jr.' | jq
```
Output
```json
[
  {
    "mlbPlayerId": 660670,
    "fullName": "Ronald Acuña Jr.",
    "season": 2021,
    "teamAbbrev": "ATL",
    "war": 3.5,
    "salaryUsd": 5000000,
    "dollarsPerWar": 1428571
  },
  {
    "mlbPlayerId": 660670,
    "fullName": "Ronald Acuña Jr.",
    "season": 2022,
    "teamAbbrev": "ATL",
    "war": 2.3,
    "salaryUsd": 15000000,
    "dollarsPerWar": 6521739
  },
  {
    "mlbPlayerId": 660670,
    "fullName": "Ronald Acuña Jr.",
    "season": 2023,
    "teamAbbrev": "ATL",
    "war": 8.4,
    "salaryUsd": 17000000,
    "dollarsPerWar": 2023810
  },
  {
    "mlbPlayerId": 660670,
    "fullName": "Ronald Acuña Jr.",
    "season": 2024,
    "teamAbbrev": "ATL",
    "war": 0.0,
    "salaryUsd": 17000000,
    "dollarsPerWar": null
  },
  {
    "mlbPlayerId": 660670,
    "fullName": "Ronald Acuña Jr.",
    "season": 2025,
    "teamAbbrev": "ATL",
    "war": 3.1,
    "salaryUsd": 17000000,
    "dollarsPerWar": 5483871
  },
  {
    "mlbPlayerId": 660670,
    "fullName": "Ronald Acuña Jr.",
    "season": 2026,
    "teamAbbrev": "ATL",
    "war": 0.8,
    "salaryUsd": 17000000,
    "dollarsPerWar": 21250000
  }
]
```
---
#### Five albatrosses for the 2026 season so far:
Curl command:
```bash
curl -s 'https://warchest-web.onrender.com/api/v1/players/albatrosses?season=2026' | jq '.[:5]'
```
Output (lolmets)
```json
[
  {
    "mlbPlayerId": 666182,
    "fullName": "Bo Bichette",
    "season": 2026,
    "teamAbbrev": "NYM",
    "war": 0.0,
    "salaryUsd": 42000000,
    "dollarsPerWar": null
  },
  {
    "mlbPlayerId": 607208,
    "fullName": "Trea Turner",
    "season": 2026,
    "teamAbbrev": "PHI",
    "war": -0.1,
    "salaryUsd": 27272727,
    "dollarsPerWar": null
  },
  {
    "mlbPlayerId": 543760,
    "fullName": "Marcus Semien",
    "season": 2026,
    "teamAbbrev": "NYM",
    "war": -0.6,
    "salaryUsd": 26000000,
    "dollarsPerWar": null
  },
  {
    "mlbPlayerId": 596115,
    "fullName": "Trevor Story",
    "season": 2026,
    "teamAbbrev": "BOS",
    "war": 0.0,
    "salaryUsd": 25000000,
    "dollarsPerWar": null
  },
  {
    "mlbPlayerId": 543807,
    "fullName": "George Springer",
    "season": 2026,
    "teamAbbrev": "TOR",
    "war": -0.1,
    "salaryUsd": 24166667,
    "dollarsPerWar": null
  }
]
```
---
#### The efficiency of total (currently measurable) payroll versus total team war for the Boston Red Sox so far in the 2026 season:
Curl command:
```bash
curl -s 'https://warchest-web.onrender.com/api/v1/teams/BOS/efficiency?season=2026' | jq
```
Output
```json
{
  "teamAbbrev": "BOS",
  "season": 2026,
  "players": 20,
  "totalWar": 19.2,
  "totalPayrollUsd": 83150000,
  "dollarsPerWar": 4330729
}
```
---

## Architecture

```
                        ┌────────────────────────────────────────┐
                        │            Spring Boot app             │
 MLB Stats API ───────▶ │  MlbStatsClient (RestClient)           │
 (teams, live data)     │       │                                │
                        │       ▼                                │
 Baseball-Reference ──▶ │  CsvImportService ──▶ JPA Repository ──┼──▶ PostgreSQL
 war_daily_bat.txt      │  (filter, stint     (PlayerSeason)     │    (player_season)
 (WAR + salary,         │   aggregation)                         │
  manual download)      │       ▼                                │
                        │  ValueService ──▶ REST Controllers     │
                        │  ($/WAR calc)     (/api/v1/...)        │
                        └────────────────────────────────────────┘
```

One row per player per season lives in `player_season`. The importer streams b-ref's daily war file, filters to a given season's hitters, collapses multi-team hitter seasons into a singular season summing war and listing the last team the player was on, and then upserts atomically. `ValueService` calculates dollars per war on read, and applies a 0.5 war threshold as lower values currently mess with the ratio of value. Negative war contracts currently get their own endpoint. Flyway owns schema and hibernate validates it.

## Data sources & limitations
- **WAR + salary:** [Baseball-Reference's war_daily files](https://www.baseball-reference.com/data/)
  (downloaded manually for now by design).
- **Salary coverage varies by season:** e.g. 2024 is around 81% populated. Current
  season is a bit sparse (bref isn't great with active contracts). Players with war but no
  salary return `"dollarsPerWar": null` instead of being dropped.
- **Name search is exact:** data stores proper
  diacritics (e.g. `Ronald Acuña Jr.`), search that way, or use the
  ID endpoints.
- **Admin import endpoint is unauthenticated** — acceptable for now, but will be changed later on.

## Quickstart

Prereqs: Docker, JDK 21, Maven. One manual download: grab
[war_daily_bat.txt](https://www.baseball-reference.com/data/war_daily_bat.txt)
in your browser and drop it in `data/`.

```bash
docker compose up -d                                                      # Postgres
mvn spring-boot:run                                                       # boot + run Flyway migration
curl -X POST 'localhost:8080/api/v1/admin/import?season=2026'             # load season
curl -s 'localhost:8080/api/v1/players/value?season=2026' | jq '.[:5]'    # test it out (use jq for the love of God)
```

## API reference

| Method | Endpoint | Returns                                              |
|---|---|------------------------------------------------------|
| GET | `/api/v1/players/value?season=` | Players ranked by $/WAR, best value first (≥0.5 WAR) |
| GET | `/api/v1/players/albatrosses?season=` | Negative WAR (sunk) contracts, most expensive first  |
| GET | `/api/v1/players/search?name=&season=` | One player's line for a given season                 |
| GET | `/api/v1/players/career?name=` | All imported seasons for a given name                |
| GET | `/api/v1/players/{mlbPlayerId}` | All imported seasons by MLB id                       |
| GET | `/api/v1/teams/{abbrev}/efficiency?season=` | Team payroll vs. total WAR                           |
| GET | `/api/v1/teams?season=` | Live team list from the MLB Stats API                |
| GET | `/api/v1/teams/{abbrev}/ranks?season=` | Team player value ranking list for a given season          |
| POST | `/api/v1/admin/import?season=` | (Re)import a season from the data file               |

## Roadmap

- **Pitchers** - import `war_daily_pitch.txt` (just throw the ball man)
- **Accent-insensitive search** - normalized name column
- **Cot's Contracts enrichment** - fill salary gaps which means name based fuzzy
  id matching (which is why it's missing in v1)
- **Tests + CI** - Testcontainers for Postgres test suite, GitHub Actions
- **Admin auth** -  shared secret header on admin endpoints
- **Frontend** - view da stuff, but from web
- **Trade value and info** - twitter nightmare (mock trades)
