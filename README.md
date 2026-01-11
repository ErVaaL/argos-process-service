# Argos Process Service

Process microservice for report jobs. Exposes a REST API, publishes report requests to RabbitMQ,
queries resource data over gRPC, and stores jobs in MongoDB.
Created in hexagonal architecture style (ports and adapters).

## Modules

- `process-core`: domain models and ports
- `process-application`: application services
- `process-adapters:mongo`: MongoDB persistence adapter
- `process-adapters:rabbitmq`: RabbitMQ publisher/consumer adapters
- `process-adapters:grpc`: gRPC resource query adapter
- `process-adapters:excel`: Excel exporter adapter
- `process-bootstrap`: Spring Boot application and REST controllers

## Requirements

- Java 21
- Docker (for local Mongo + Keycloak + RabbitMQ via compose)
- Gradle (or use `./gradlew`)

## Configuration

The service reads configuration from environment variables (see `process-bootstrap/src/main/resources/application.yml`):

- `SPRING_MONGODB_URI` (required)
- `SPRING_RABBITMQ_HOST` (required)
- `SPRING_RABBITMQ_PORT` (required)
- `SPRING_RABBITMQ_USERNAME` (required)
- `SPRING_RABBITMQ_PASSWORD` (required)
- `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI` (required)
- `ARGOS_RESOURCE_GRPC_HOST` (required)
- `ARGOS_RESOURCE_GRPC_PORT` (required)

Default HTTP port is `8082`.

## Run locally (Docker Compose)

```bash
export GH_USER=...          # if building image from source in compose
export GH_TOKEN=...

docker compose up --build
```

> Note:
>
> Environment variables can also be set in .env file for Docker Compose. (Don't commit sensitive info!)

Services:
- HTTP: http://localhost:8082
- Mongo: localhost:27017 (container)
- Keycloak: http://localhost:8080
- RabbitMQ: localhost:5672 (container)
- RabbitMQ Management: http://localhost:15672

## Run locally (Gradle)

```bash
export SPRING_MONGODB_URI=mongodb://root:root@localhost:27017/argos_process?authSource=admin
export SPRING_RABBITMQ_HOST=localhost
export SPRING_RABBITMQ_PORT=5672
export SPRING_RABBITMQ_USERNAME=guest
export SPRING_RABBITMQ_PASSWORD=guest
export SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://localhost:8080/realms/master
export ARGOS_RESOURCE_GRPC_HOST=localhost
export ARGOS_RESOURCE_GRPC_PORT=9091

./gradlew :process-bootstrap:bootRun
```

## API

Base path is `/` when running the service directly. When behind the gateway, use `/api/v1/process`.

Endpoints:
- `POST /jobs/report`
- `GET /jobs/list`
- `GET /jobs/export?filter=...` (downloads `process-jobs.xlsx`)

## gRPC dependency

The service calls the Resource gRPC server defined in `argos-contracts`.
Make sure `ARGOS_RESOURCE_GRPC_HOST` and `ARGOS_RESOURCE_GRPC_PORT` point to a reachable Resource service.

## Tests

```bash
./gradlew test
```

Note: Mongo adapter tests use Testcontainers.
