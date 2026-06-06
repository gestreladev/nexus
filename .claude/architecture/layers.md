# Architecture Layers

## Layer map

```
┌─────────────────────────────────┐
│           pipeline/             │  Application bootstrap — ordered plugin
│         ModulePipeline          │  registration. Nothing else goes here.
├─────────────────────────────────┤
│            plugins/             │  Ktor plugin wiring (Serialization,
│    Routing, Serialization,      │  StatusPages, CallLogging). Delegates
│    StatusPages, CallLogging     │  to routes — no business logic here.
├─────────────────────────────────┤
│           routes/v1/            │  HTTP boundary. Parses request,
│      HealthRoute, etc.          │  validates input, calls domain,
│                                 │  serialises response. No SQL here.
├─────────────────────────────────┤
│            domain/              │  Business logic lives here.
│   user/, document/, …           │  Data classes, Repository interfaces,
│   Table + Repository            │  Exposed table definitions, queries.
├─────────────────────────────────┤
│           database/             │  Infrastructure only.
│       DatabaseFactory           │  HikariCP pool, Flyway runner.
│                                 │  No domain types cross this boundary.
└─────────────────────────────────┘
```

## Rules

| Layer | May import | Must NOT import |
|---|---|---|
| `pipeline` | `plugins/*` | `routes`, `domain`, `database` |
| `plugins` | `routes/*` | `domain`, `database` |
| `routes/v1` | `domain/*` | `database` directly |
| `domain` | stdlib, Exposed, `database` types | `routes`, `plugins`, `pipeline` |
| `database` | HikariCP, Flyway | anything from `domain` upward |

**The key rule:** dependencies only flow downward. A route never touches
`DatabaseFactory`. A repository never touches Ktor.

## Where to put new code

| What you're writing | Layer |
|---|---|
| New HTTP endpoint | `routes/v1/<resource>Route.kt` |
| New query / DB operation | `domain/<resource>/<Resource>Repository.kt` |
| New Exposed table definition | `domain/<resource>/<Resource>Table.kt` |
| New domain model (data class) | `domain/<resource>/<Resource>.kt` |
| New Ktor plugin install | `plugins/<Concern>.kt` + `ModulePipeline` stage |
| DB pool / migration config | `database/DatabaseFactory.kt` |

## Package structure

```
dev.nexus.api/
├── pipeline/       ModulePipeline.kt
├── plugins/        Routing.kt  Serialization.kt  StatusPages.kt
├── routes/
│   └── v1/         HealthRoute.kt  (UserRoute.kt …)
├── domain/
│   ├── user/       UserTable.kt  UserRepository.kt  User.kt
│   └── document/   DocumentTable.kt  DocumentRepository.kt  Document.kt
└── database/       DatabaseFactory.kt  DatabaseConfig.kt
```
