# Saga Orchestrator Service

Microservice responsible for managing distributed transactions using the Saga pattern.

## Features

- Orchestrate distributed transactions
- Handle compensation logic
- Track saga execution state
- Retry failed transactions
- Event-driven saga coordination
- Saga timeout management

## Architecture

Follows **DDD + Clean Architecture** principles with four main layers:
Domain, Application, Infrastructure, and Presentation.

Implements **Orchestration-based Saga Pattern** for distributed transactions.

## Technology Stack

- Java 21, Spring Boot 3.2+
- Spring Kafka (event streaming)
- Spring Cloud (Eureka, Config)
- Spring Scheduling

## Saga Types

- **Book Borrowing Saga**: Coordinates book borrowing across services
  1. Reserve book (Book Service)
  2. Create borrowing record (Borrowing Service)
  3. Send notification (Notification Service)

## Configuration

Port: 8085

Environment variables:
- `KAFKA_BOOTSTRAP_SERVERS`
- `EUREKA_URI`

## Building & Running

```bash
mvn clean package -pl services/saga-orchestrator-service -am
mvn spring-boot:run -pl services/saga-orchestrator-service
```

## Saga State Machine

States: STARTED → IN_PROGRESS → COMPLETED | FAILED | COMPENSATING → COMPENSATED

## API Documentation

Swagger UI: http://localhost:8085/swagger-ui.html
