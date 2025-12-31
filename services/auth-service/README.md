# Auth Service

Microservice responsible for authentication and authorization in the Library Management System.

## Features

- User authentication (login/logout)
- JWT token generation and validation
- Password management (reset, change)
- OAuth2 integration support
- Session management

## Architecture

Follows **DDD + Clean Architecture** principles with four main layers:
Domain, Application, Infrastructure, and Presentation.

## Technology Stack

- Java 21, Spring Boot 3.2+
- Spring Security, JWT
- MySQL, Flyway
- Spring Cloud (Eureka, Config)

## API Endpoints

- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `POST /api/auth/refresh` - Refresh access token
- `POST /api/auth/register` - User registration
- `POST /api/auth/forgot-password` - Request password reset
- `POST /api/auth/reset-password` - Reset password

## Configuration

Port: 8080

Environment variables:
- `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET`, `JWT_EXPIRATION`
- `EUREKA_URI`

## Building & Running

```bash
mvn clean package -pl services/auth-service -am
mvn spring-boot:run -pl services/auth-service
```

## API Documentation

Swagger UI: http://localhost:8080/swagger-ui.html
