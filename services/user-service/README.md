# User Service

Microservice responsible for user profile management and user-related operations.

## Features

- User registration and profile management
- Role and permission management
- User search and listing
- Profile image upload
- User status management (active, suspended, blocked)

## Architecture

Follows **DDD + Clean Architecture** principles with four main layers:
Domain, Application, Infrastructure, and Presentation.

## Technology Stack

- Java 21, Spring Boot 3.2+
- Spring Data JPA (MySQL)
- Redis (caching)
- Spring Cloud (Eureka, Config)
- Flyway (migrations)

## API Endpoints

- `GET /api/users` - List users (paginated)
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users/search` - Search users

## Configuration

Port: 8082

Environment variables:
- `DB_USERNAME`, `DB_PASSWORD`
- `REDIS_HOST`, `REDIS_PORT`
- `EUREKA_URI`

## Building & Running

```bash
mvn clean package -pl services/user-service -am
mvn spring-boot:run -pl services/user-service
```

## API Documentation

Swagger UI: http://localhost:8082/swagger-ui.html
