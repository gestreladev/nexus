---
name: dns
description: DNS resolution, record types, and TTL.
agent:
  role: subject-expert
  tier: nano
  weight: soft
  triggers:
    - resolving how a domain maps to an address
    - choosing a DNS record type
metadata:
  type: reference
---

# DNS

The phonebook of the internet: domain name → IP address.

## Resolution path
```
api.nexus.dev
  → OS check /etc/hosts (miss)
  → recursive resolver (ISP/router)
      → Root nameserver      "ask .dev TLD"
      → .dev TLD nameserver  "ask nexus.dev NS"
      → nexus.dev nameserver "203.0.113.42" (A record)
  → cached for TTL seconds
```

## Record types you'll use
| Type | Maps | Use |
|---|---|---|
| `A` | domain → IPv4 | point a host at a server |
| `AAAA` | domain → IPv6 | same, IPv6 |
| `CNAME` | domain → another domain | alias (e.g. `www` → apex) |
| `TXT` | domain → text | verification, SPF/DKIM |

## TTL
Each record carries a Time-To-Live; resolvers cache until it expires. Low TTL =
faster propagation on change, more lookups; high TTL = fewer lookups, slower
changes. Lower TTL *before* a planned migration.

## Why it matters in Nexus
In Docker Compose, services resolve each other by **service name** (Docker's
internal DNS), not `localhost` — e.g. `nexus-api` reaches Postgres at host
`postgres`, not `127.0.0.1`. (Phase 8.)

## References
- [networking_orchestrator.md](networking_orchestrator.md) · `[[networking_orchestrator]]`
- [how_the_internet_works.md](how_the_internet_works.md) · `[[how_the_internet_works]]`
