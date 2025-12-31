# API Gateway

Central entry point for all client requests to microservices in the Library Management System.

## Features

- **Routing**: Routes requests to appropriate microservices
- **Load Balancing**: Distributes load across service instances
- **Authentication**: Validates JWT tokens
- **Rate Limiting**: Prevents API abuse
- **Circuit Breaker**: Resilience4j integration for fault tolerance
- **CORS**: Cross-Origin Resource Sharing configuration
- **Request/Response Transformation**: Modifies requests and responses

## Technology Stack

- Spring Cloud Gateway
- Spring Cloud Netflix Eureka (Service Discovery)
- Redis (Rate Limiting)
- Resilience4j (Circuit Breaker, Retry)

## Routes

- `/api/auth/**` → Auth Service (8080)
- `/api/books/**` → Book Service (8081)
- `/api/users/**` → User Service (8082)
- `/api/borrowing/**` → Borrowing Service (8083)
- `/api/notifications/**` → Notification Service (8084)

## Configuration

Port: 8000

Environment variables:
- `REDIS_HOST`, `REDIS_PORT`
- `EUREKA_URI`

## Building & Running

```bash
mvn clean package -pl infrastructure/api-gateway -am
mvn spring-boot:run -pl infrastructure/api-gateway
```

## Gateway Filters

- **CircuitBreaker**: Prevents cascading failures
- **Retry**: Retries failed requests
- **RateLimiter**: Limits requests per user (TODO)
- **Authentication**: JWT validation (TODO)

## Monitoring

Health: http://localhost:8000/actuator/health
Gateway Routes: http://localhost:8000/actuator/gateway/routes
