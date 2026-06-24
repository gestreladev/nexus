---
name: volumes_and_networks
description: Docker volumes (persistence), bind mounts, networks, and service-name DNS.
agent:
  role: infra-specialist
  tier: standard
  weight: soft
  triggers:
    - persisting container data
    - connecting services across containers
metadata:
  type: reference
---

# Volumes & Networks

## Volumes — persistence
A container's writable layer is **ephemeral** — destroyed with the container.
To keep data, mount a volume.

| Type | Use |
|---|---|
| **Named volume** | Docker-managed storage; survives `down`/recreate (DB data) |
| **Bind mount** | host path → container path; great for dev (live source) |
| **tmpfs** | in-memory; never persisted |

```yaml
volumes: ["nexus-postgres-data:/var/lib/postgresql/data"]  # named → DB survives restarts
```
`docker compose down` keeps named volumes; `down -v` deletes them (data loss).

## Networks — how containers talk
Compose puts services on a shared network and gives each a DNS name = its
**service name**. So `nexus-api` reaches Postgres at host `postgres`, **not**
`localhost`:

```
jdbc:postgresql://postgres:5432/nexus     # inside the compose network
jdbc:postgresql://localhost:5433/nexus    # from the host (published port)
```

Two address spaces to keep straight:
- **Inside the network** → service name + container port (`postgres:5432`)
- **From the host** → `localhost` + published port (`localhost:5433`)

That published `5433:5432` mapping is why local `nexus-api` (run on the host)
uses 5433, while a containerized `nexus-api` would use `postgres:5432`.

## Dual-listener brokers (Kafka)
Most services accept a connection on a port and that's it. A broker is different:
it **advertises** an address back to the client to reconnect on. One advertised
address can't serve both the host and in-network containers, so the broker needs
two listeners:

```yaml
KAFKA_LISTENERS: EXTERNAL://0.0.0.0:9092,INTERNAL://0.0.0.0:29092,CONTROLLER://0.0.0.0:9093
KAFKA_ADVERTISED_LISTENERS: EXTERNAL://localhost:9092,INTERNAL://kafka:29092
KAFKA_INTER_BROKER_LISTENER_NAME: INTERNAL   # brokers talk to each other in-network
```

- Host clients bootstrap on `localhost:9092` → broker advertises `localhost:9092` ✓
- Containers (`nexus-api`, `nexus-ingest`) bootstrap on `kafka:29092` → advertised
  `kafka:29092` ✓

Give the broker only the single-listener `localhost:9092` and containers connect
to bootstrap but then get told to reconnect to `localhost` — which inside a
container is *itself*, not Kafka. Two listeners fix it.

## Custom networks
Define networks to isolate groups of services (e.g. a private DB network the
public proxy can't reach) — arrives with the full system in Phase 8.

## References
- [docker_orchestrator.md](docker_orchestrator.md) · `[[docker_orchestrator]]`
- [compose.md](compose.md) · `[[compose]]`
- [dns.md](../../fundamentals/networking/dns.md) · `[[dns]]`
