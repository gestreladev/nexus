# Branching Strategy

## Model

Nexus uses a simplified **GitHub Flow**:
- `main` is always deployable and protected
- All work happens on short-lived branches
- Every branch merges via PR — no direct pushes to `main`

```
main ──────────────●──────────────●──────────
                  ↑               ↑
           feat/phase-2     fix/health-timeout
```

## Branch naming

```
{type}/{short-description}
```

| Type | When |
|---|---|
| `feat/` | New feature or phase deliverable |
| `fix/` | Bug fix |
| `chore/` | Dependency bump, tooling, config |
| `docs/` | Documentation only |
| `test/` | Adding or fixing tests only |
| `refactor/` | Code improvement, no behaviour change |

Examples:
```
feat/phase-2-database
feat/user-create-endpoint
fix/health-endpoint-500
chore/bump-ktor-3.2
refactor/pipeline-error-handling
```

Keep descriptions lowercase and hyphenated. No ticket numbers in branch
names — milestones and labels on GitHub carry that context.

## PR rules

1. **One concern per PR.** A PR that adds auth AND fixes a bug is two PRs.
2. **Title follows Conventional Commits** — same format as commits:
   `feat(nexus-api): add user creation endpoint`
3. **Every PR must have** a milestone and at least one `type:*` label.
4. **Squash merge** into `main` — keeps history linear and readable.

## Commit convention

```
{type}({scope}): {description}

{optional body}

Co-Authored-By: …
```

Types: `feat` `fix` `refactor` `chore` `docs` `test`  
Scope: service or area (`nexus-api`, `nexus-ingest`, `infra`, `claude`, `deps`)

```
feat(nexus-api): add POST /v1/users endpoint
fix(nexus-api): handle duplicate email as 409 not 500
chore(deps): bump exposed to 0.62.0
docs(claude): add api/conventions.md
```

## Protection rules on `main`

- Force pushes: **blocked**
- Deletions: **blocked**
- PRs require at least one review (self-review counts for solo learning project)

## Release tags

Created on `main` after milestone PRs are merged.  
See `git/versioning.md` for full release process.
