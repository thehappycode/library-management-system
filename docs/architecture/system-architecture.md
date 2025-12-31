# System Architecture

## Overview

The Library Management System is built using a microservices architecture with Domain-Driven Design (DDD) and Clean Architecture principles. The system is deployed as a monorepo containing all services, common libraries, and infrastructure components.

## Architectural Principles

### 1. Microservices Architecture

Each service is independently deployable and focuses on a specific business domain:
- **Auth Service**: Authentication and authorization
- **Book Service**: Book catalog and inventory management
- **User Service**: User profile and management
- **Borrowing Service**: Borrowing transactions and operations
- **Notification Service**: Multi-channel notifications
- **Saga Orchestrator**: Distributed transaction coordination

### 2. Domain-Driven Design (DDD)

Each service follows DDD principles:
- **Bounded Contexts**: Clear service boundaries
- **Aggregates**: Consistency boundaries within domains
- **Domain Events**: Event-driven communication
- **Ubiquitous Language**: Consistent terminology

### 3. Clean Architecture

Services are structured in layers:
```
Presentation → Application → Domain ← Infrastructure
```

- **Domain Layer**: Pure business logic, no framework dependencies
- **Application Layer**: Use cases, orchestration logic
- **Infrastructure Layer**: Technical implementations (DB, messaging, HTTP)
- **Presentation Layer**: REST APIs, controllers

### 4. Event-Driven Architecture

- **Asynchronous Communication**: Via Kafka
- **Event Sourcing**: Track all state changes
- **CQRS**: Separate read and write models where appropriate
- **Saga Pattern**: Manage distributed transactions

## Component Architecture

### Core Components

#### API Gateway
- Single entry point for all clients
- Request routing and load balancing
- Authentication and authorization
- Rate limiting
- Circuit breaker pattern

#### Service Discovery (Eureka)
- Dynamic service registration
- Service health monitoring
- Client-side load balancing

#### Config Server
- Centralized configuration management
- Environment-specific configurations
- Dynamic configuration updates

### Data Architecture

#### Databases
- **MySQL**: Primary database for transactional data (Auth, Book, User, Borrowing)
- **MongoDB**: Document storage for notifications and saga state
- **Redis**: Caching, sessions, rate limiting
- **Elasticsearch**: Full-text search engine

#### Data Consistency
- **Single Service DB**: Each service owns its data
- **Saga Pattern**: For distributed transactions
- **Event Sourcing**: Track state changes
- **Eventual Consistency**: Accept temporary inconsistencies

### Communication Patterns

#### Synchronous Communication
- **REST/HTTP**: Service-to-service communication
- **Circuit Breaker**: Resilience4j for fault tolerance
- **Retry Mechanism**: Automatic retries for transient failures

#### Asynchronous Communication
- **Apache Kafka**: Event streaming platform
- **Domain Events**: Publish business events
- **Event Consumers**: Process events asynchronously

### Security Architecture

#### Authentication & Authorization
- **JWT Tokens**: Stateless authentication
- **Spring Security**: Framework-level security
- **Role-Based Access Control**: User permissions

#### API Security
- **HTTPS**: Encrypted communication
- **API Rate Limiting**: Prevent abuse
- **Input Validation**: Prevent injection attacks
- **CORS**: Cross-origin resource sharing

### Observability

#### Monitoring
- **Prometheus**: Metrics collection
- **Grafana**: Metrics visualization
- **Spring Boot Actuator**: Health checks and metrics

#### Logging
- **SLF4J**: Logging framework
- **Logback**: Log implementation
- **Centralized Logging**: Aggregated logs

#### Tracing
- **Jaeger**: Distributed tracing
- **OpenTelemetry**: Observability framework

## Deployment Architecture

### Development
- Local development with Docker Compose
- Individual service execution with Maven
- Integrated testing with Testcontainers

### Production (TODO)
- Kubernetes deployment
- Helm charts for configuration
- Horizontal Pod Autoscaling
- Service Mesh (Istio/Linkerd)

## Scalability

### Horizontal Scaling
- Stateless services
- Load balancing via Eureka/K8s
- Database read replicas
- Kafka partitioning

### Vertical Scaling
- Resource limits per service
- JVM tuning
- Database optimization

## Resilience Patterns

- **Circuit Breaker**: Prevent cascading failures
- **Retry**: Automatic retry for transient failures
- **Timeout**: Prevent hanging requests
- **Bulkhead**: Isolate critical resources
- **Rate Limiting**: Protect against overload

## Future Enhancements

- [ ] Service Mesh (Istio)
- [ ] GraphQL Gateway
- [ ] gRPC for inter-service communication
- [ ] Real-time features with WebSocket
- [ ] AI/ML integration for recommendations
- [ ] Multi-region deployment
