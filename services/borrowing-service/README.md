# Borrowing Service

Microservice responsible for managing book borrowing and return operations.

## Features

- Borrow book transactions
- Return book processing
- Borrowing history tracking
- Overdue detection and fine calculation
- Renewal operations
- Scheduled jobs for overdue checks

## Architecture

Follows **DDD + Clean Architecture** principles with four main layers:
Domain, Application, Infrastructure, and Presentation.

## Technology Stack

- Java 21, Spring Boot 3.2+
- Spring Data JPA (MySQL)
- Spring Kafka (event publishing)
- Spring Cloud (Eureka, Config)
- Flyway (migrations)
- Spring Scheduling

## API Endpoints

- `POST /api/borrowing` - Borrow a book
- `POST /api/borrowing/{id}/return` - Return a book
- `POST /api/borrowing/{id}/renew` - Renew borrowing
- `GET /api/borrowing/user/{userId}` - Get user's borrowing history
- `GET /api/borrowing/overdue` - Get overdue borrowings
- `GET /api/borrowing/{id}` - Get borrowing details

## Configuration

Port: 8083

Environment variables:
- `DB_USERNAME`, `DB_PASSWORD`
- `KAFKA_BOOTSTRAP_SERVERS`
- `EUREKA_URI`

## Building & Running

```bash
mvn clean package -pl services/borrowing-service -am
mvn spring-boot:run -pl services/borrowing-service
```

## Events Published

- `BorrowingCreatedEvent`
- `BorrowingReturnedEvent`
- `BorrowingOverdueEvent`
- `FinePaidEvent`

## API Documentation

Swagger UI: http://localhost:8083/swagger-ui.html
