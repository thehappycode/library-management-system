# Notification Service

Microservice responsible for sending notifications to users.

## Features

- Email notifications
- SMS notifications (optional)
- Push notifications
- Notification templates
- Event-driven notification processing
- Async notification sending

## Architecture

Follows **DDD + Clean Architecture** principles with four main layers:
Domain, Application, Infrastructure, and Presentation.

## Technology Stack

- Java 21, Spring Boot 3.2+
- Spring Kafka (event consumption)
- Spring Mail (email)
- Spring Cloud (Eureka, Config)
- Spring Async

## Notification Types

- **Due Date Reminders**: 3 days before due date
- **Overdue Notifications**: When book becomes overdue
- **Registration Welcome**: New user registration
- **Password Reset**: Password reset requests
- **Fine Notifications**: Fine payment reminders

## Configuration

Port: 8084

Environment variables:
- `KAFKA_BOOTSTRAP_SERVERS`
- `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`
- `EUREKA_URI`

## Building & Running

```bash
mvn clean package -pl services/notification-service -am
mvn spring-boot:run -pl services/notification-service
```

## Events Consumed

Listens to multiple Kafka topics for triggering notifications

## API Documentation

Swagger UI: http://localhost:8084/swagger-ui.html
