# 📚 Library Management System - Microservices Architecture

> Enterprise-grade Book Management System built with Spring Boot 3, Java 21, and Cloud-Native technologies

[![Java](https://img.shields.io/badge/Java-21-orange. svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3. 2-brightgreen. svg)](https://spring.io/projects/spring-boot)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-Ready-blue.svg)](https://kubernetes.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📖 Table of Contents

- [Overview](#-overview)
- [Monorepo Structure](#-monorepo-structure)
- [Architecture](#-architecture)
- [Technology Stack](#-technology-stack)
- [Microservices](#-microservices)
- [Getting Started](#-getting-started)
- [Project Checklist](#-project-checklist)
- [Documentation](#-documentation)
- [Contributing](#-contributing)

---

## 🎯 Overview

Library Management System là một hệ thống quản lý thư viện đầy đủ tính năng, được xây dựng với kiến trúc microservices hiện đại. Hệ thống áp dụng các best practices trong thiết kế phần mềm bao gồm Domain-Driven Design (DDD), Clean Architecture, và Cloud-Native patterns.

### ✨ Key Features

- 📖 **Book Management**: CRUD operations, search, categorization
- 👤 **User Management**: Registration, authentication, profile management
- 📋 **Borrowing System**:  Borrow/return books, due date tracking, fines
- 🔔 **Notifications**: Email/SMS notifications for due dates, overdue books
- 🔍 **Advanced Search**: Full-text search with Elasticsearch
- 📊 **Analytics & Reporting**: Borrowing statistics, popular books
- 🔐 **Security**: JWT authentication, role-based access control
- 🌐 **Multi-tenant**: Support multiple library branches

---

## 📦 Monorepo Structure

This project uses a **monorepo architecture** with all microservices, common libraries, and infrastructure components in a single repository.

### Project Structure

```
library-management-system/
├── common/                          # Shared libraries
│   ├── common-dto/                  # Data Transfer Objects
│   ├── common-exception/            # Exception handling
│   ├── common-util/                 # Utility classes
│   ├── common-security/             # Security components (JWT, etc.)
│   └── common-event/                # Domain events
├── services/                        # Microservices
│   ├── auth-service/               # Authentication & Authorization
│   ├── book-service/               # Book management
│   ├── user-service/               # User management
│   ├── borrowing-service/          # Borrowing operations
│   ├── notification-service/       # Notifications
│   └── saga-orchestrator-service/  # Distributed transactions
├── infrastructure/                  # Infrastructure services
│   ├── api-gateway/                # Spring Cloud Gateway
│   ├── service-discovery/          # Eureka Server
│   └── config-server/              # Spring Cloud Config
├── k8s/                            # Kubernetes manifests
├── helm/                           # Helm charts
├── terraform/                      # Infrastructure as Code
├── observability/                  # Monitoring configs
├── docs/                           # Documentation
│   ├── architecture/
│   ├── api/
│   ├── adr/
│   └── runbooks/
├── scripts/                        # Utility scripts
│   ├── build-all.sh
│   ├── start-all.sh
│   ├── stop-all.sh
│   ├── test-all.sh
│   └── docker-build-all.sh
├── docker-compose.yml              # Local development
└── pom.xml                         # Parent POM
```

### Benefits of Monorepo

- ✅ **Simplified Dependency Management**: All services use the same versions
- ✅ **Atomic Changes**: Cross-service changes in single commit
- ✅ **Code Reuse**: Shared libraries easily accessible
- ✅ **Consistent Tooling**: Same build, test, and deployment process
- ✅ **Better Collaboration**: Easier code reviews across services

---

## 🏗️ Architecture

### High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                        EXTERNAL CLIENTS                              │
│              (Web App, Mobile App, Third-party APIs)                 │
└───────────────────────────────┬─────────────────────────────────────┘
                                │
                                │ HTTPS
                                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     KUBERNETES CLUSTER                               │
│                                                                       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │               SERVICE MESH (Istio)                           │   │
│  │        mTLS, Traffic Management, Observability               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │            API GATEWAY (Spring Cloud Gateway)                │   │
│  │  Rate Limiting | Authentication | Routing | Load Balancing  │   │
│  └───────────────────────────┬─────────────────────────────────┘   │
│                               │                                      │
│              ┌────────────────┼───────────────┬─────────────┐       │
│              ▼                ▼               ▼             ▼       │
│      ┌─────────────┐  ┌─────────────┐  ┌──────────┐  ┌──────────┐ │
│      │    Auth     │  │    Book     │  │   User   │  │Borrowing │ │
│      │   Service   │  │  Service    │  │ Service  │  │ Service  │ │
│      └─────────────┘  └─────────────┘  └──────────┘  └──────────┘ │
│                                                                       │
│      ┌─────────────┐  ┌─────────────────────────────────────────┐  │
│      │Notification │  │     Saga Orchestrator Service           │  │
│      │  Service    │  │  (Distributed Transaction Management)   │  │
│      └─────────────┘  └─────────────────────────────────────────┘  │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                              │
│                                                                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐              │
│  │   Service    │  │    Config    │  │   Circuit    │              │
│  │  Discovery   │  │    Server    │  │   Breaker    │              │
│  │   (Eureka)   │  │ (Spring Cloud)│  │(Resilience4j)│              │
│  └──────────────┘  └──────────────┘  └──────────────┘              │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                    MESSAGE & EVENT BUS                               │
│                                                                       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │               APACHE KAFKA (Event Streaming)                 │   │
│  │  • book-events  • user-events  • borrowing-events            │   │
│  │  • notification-events  • saga-events                        │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                         DATA LAYER                                   │
│                                                                       │
│  ┌───────────┐  ┌───────────┐  ┌───────────┐  ┌──────────────┐    │
│  │   MySQL   │  │  MongoDB  │  │   Redis   │  │Elasticsearch │    │
│  │ (Primary) │  │(Documents)│  │  (Cache)  │  │ (Search)     │    │
│  └───────────┘  └───────────┘  └───────────┘  └──────────────┘    │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                  OBSERVABILITY & MONITORING                          │
│                                                                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐              │
│  │   Logging    │  │   Metrics    │  │   Tracing    │              │
│  │     (ELK)    │  │(Prometheus)  │  │   (Jaeger)   │              │
│  │   Kibana     │  │   Grafana    │  │              │              │
│  └──────────────┘  └──────────────┘  └──────────────┘              │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
```

### DDD + Clean Architecture (Per Service)

```
service/
├── domain/                 # Core Business Logic
│   ├── model/             # Entities, Value Objects, Aggregates
│   ├── repository/        # Repository Interfaces (Ports)
│   ├── service/           # Domain Services
│   └── event/             # Domain Events
│
├── application/           # Use Cases & Application Logic
│   ├── usecase/          # Use Case Implementations
│   ├── dto/              # Data Transfer Objects
│   └── port/             # Input/Output Ports
│
├── infrastructure/        # External Concerns & Adapters
│   ├── persistence/      # Database Implementation
│   ├── messaging/        # Kafka, Event Publishing
│   ├── cache/            # Redis Implementation
│   ├── search/           # Elasticsearch
│   ├── http/             # HTTP Clients
│   └── config/           # Configuration
│
└── presentation/          # API Layer
    ├── rest/             # REST Controllers
    ├── exception/        # Exception Handlers
    ├── interceptor/      # Interceptors/Middleware
    └── validation/       # Request Validation
```

---

## 🛠️ Technology Stack

### Core Technologies

| Category | Technology | Version | Purpose |
|----------|-----------|---------|---------|
| **Language** | Java | 21 | Programming Language |
| **Framework** | Spring Boot | 3.2+ | Application Framework |
| **Build Tool** | Maven | 3.9+ | Dependency Management |

### Spring Ecosystem

| Technology | Purpose |
|-----------|---------|
| Spring Cloud Gateway | API Gateway |
| Spring Cloud Config | Centralized Configuration |
| Spring Cloud Netflix Eureka | Service Discovery |
| Spring Data JPA | Database Access (MySQL) |
| Spring Data MongoDB | NoSQL Database Access |
| Spring Data Redis | Caching |
| Spring Kafka | Event Streaming |
| Spring Security | Authentication & Authorization |
| Spring WebFlux | Reactive Programming |

### Databases & Storage

| Technology | Purpose |
|-----------|---------|
| MySQL | Primary Relational Database |
| MongoDB | Document Storage (Audit logs, Metadata) |
| Redis | Caching, Session Management, Rate Limiting |
| Elasticsearch | Full-text Search Engine |
| MinIO / S3 | Object Storage (Book covers, Documents) |

### Messaging & Events

| Technology | Purpose |
|-----------|---------|
| Apache Kafka | Event Streaming, Async Communication |
| Kafka Connect | Data Integration |
| Schema Registry | Schema Management |

### Observability

| Technology | Purpose |
|-----------|---------|
| Elasticsearch | Log Storage |
| Logstash | Log Processing |
| Kibana | Log Visualization |
| Prometheus | Metrics Collection |
| Grafana | Metrics Visualization |
| Jaeger | Distributed Tracing |
| Zipkin | Alternative Tracing |

### Infrastructure

| Technology | Purpose |
|-----------|---------|
| Docker | Containerization |
| Kubernetes (K8s) | Container Orchestration |
| Helm | Kubernetes Package Manager |
| Istio | Service Mesh |
| Terraform | Infrastructure as Code |

### DevOps & CI/CD

| Technology | Purpose |
|-----------|---------|
| GitHub Actions | CI/CD Pipelines |
| SonarQube | Code Quality |
| OWASP Dependency Check | Security Scanning |
| Trivy | Container Security |
| ArgoCD | GitOps Deployment |

### Resilience & Patterns

| Technology | Purpose |
|-----------|---------|
| Resilience4j | Circuit Breaker, Retry, Rate Limiter |
| Saga Pattern | Distributed Transaction Management |
| CQRS | Command Query Responsibility Segregation |
| Event Sourcing | Event-driven Architecture |

### Testing

| Technology | Purpose |
|-----------|---------|
| JUnit 5 | Unit Testing |
| Mockito | Mocking Framework |
| Testcontainers | Integration Testing |
| Rest Assured | API Testing |
| K6 | Load Testing |
| Cucumber | BDD Testing |

### Others

| Technology | Purpose |
|-----------|---------|
| OpenAPI/Swagger | API Documentation |
| MapStruct | Object Mapping |
| Lombok | Boilerplate Code Reduction |
| Flyway/Liquibase | Database Migration |
| JavaMail/SendGrid | Email Service |
| Twilio | SMS Service (Optional) |

---

## 🎯 Microservices

### Service Portfolio

| Service | Port | Path | Status | Description |
|---------|------|------|--------|-------------|
| **Auth Service** | 8080 | `services/auth-service` | ✅ Structure | Authentication & Authorization (JWT, OAuth2) |
| **Book Service** | 8081 | `services/book-service` | ✅ Structure | Book CRUD, Category, Inventory, Search |
| **User Service** | 8082 | `services/user-service` | ✅ Structure | User Management, Profile, Roles |
| **Borrowing Service** | 8083 | `services/borrowing-service` | ✅ Structure | Borrow/Return, History, Fines |
| **Notification Service** | 8084 | `services/notification-service` | ✅ Structure | Email/SMS Notifications |
| **Saga Orchestrator** | 8085 | `services/saga-orchestrator-service` | ✅ Structure | Distributed Transaction Management |
| **API Gateway** | 8000 | `infrastructure/api-gateway` | ✅ Structure | Gateway, Routing, Rate Limiting |
| **Service Discovery** | 8761 | `infrastructure/service-discovery` | ✅ Structure | Eureka Server |
| **Config Server** | 8888 | `infrastructure/config-server` | ✅ Structure | Centralized Configuration |

### Common Libraries

| Library | Path | Description |
|---------|------|-------------|
| **common-dto** | `common/common-dto` | Shared DTOs and response models |
| **common-exception** | `common/common-exception` | Exception classes and handlers |
| **common-util** | `common/common-util` | Utility classes |
| **common-security** | `common/common-security` | JWT utilities and security configs |
| **common-event** | `common/common-event` | Domain events for event-driven architecture |

**Status Legend:**
- ✅ Structure - Project structure and configuration complete
- 🚧 In Progress - Implementation in progress
- ✅ Completed - Feature complete and tested
- 🧪 Testing - Under testing
- 🚀 Deployed - Deployed to production

---

## 🚀 Getting Started

### Prerequisites

```bash
# Required
- Java 21
- Maven 3.9+
- Docker & Docker Compose

# Optional (for local development)
- kubectl
- Helm 3+
- Minikube or Kind (local K8s)
```

### Quick Start (Local Development)

#### 1. Clone the Repository

```bash
git clone https://github.com/thehappycode/library-management-system.git
cd library-management-system
```

#### 2. Start Infrastructure Services

```bash
# Start databases, message brokers, and monitoring
docker-compose up -d

# Verify all containers are running
docker-compose ps
```

#### 3. Build All Services

```bash
# Build common libraries and all microservices
./scripts/build-all.sh
```

#### 4. Start All Services

```bash
# Start services in correct order
./scripts/start-all.sh

# Logs are available in logs/ directory
tail -f logs/book-service.log
```

#### 5. Access Services

- **API Gateway**: http://localhost:8000
- **Eureka Dashboard**: http://localhost:8761
- **Service-specific Swagger UIs**: 
  - Book Service: http://localhost:8081/swagger-ui.html
  - Auth Service: http://localhost:8080/swagger-ui.html
- **Monitoring**:
  - Prometheus: http://localhost:9090
  - Grafana: http://localhost:3000 (admin/admin)
  - Jaeger: http://localhost:16686

### Stop Services

```bash
# Stop all services
./scripts/stop-all.sh

# Stop Docker containers
docker-compose down
```

### Build Specific Service

```bash
# Build only book-service and its dependencies
mvn clean package -pl services/book-service -am

# Run specific service
mvn spring-boot:run -pl services/book-service
```

### Run Tests

```bash
# Test all services
./scripts/test-all.sh

# Test specific service
mvn test -pl services/book-service
```

### Build Docker Images

```bash
# Build all Docker images
./scripts/docker-build-all.sh

# Build specific service image
docker build -t library/book-service:latest -f services/book-service/Dockerfile .
```

---

## ✅ Project Checklist

### Phase 1: Foundation (Weeks 1-2)

#### Architecture & Setup
- [ x ] Create GitHub Organization
- [ x ] Setup all repositories
- [ ] Define API contracts (OpenAPI)
- [ ] Setup common libraries
- [ ] Create parent POM
- [ ] Setup development environment

#### Infrastructure
- [ ] Docker Compose setup
- [ ] MySQL setup
- [ ] MongoDB setup
- [ ] Redis setup
- [ ] Kafka setup
- [ ] Elasticsearch setup

---

### Phase 2: Core Services (Weeks 3-6)

#### Auth Service
- [ ] User registration
- [ ] Login/Logout
- [ ] JWT token generation
- [ ] Token refresh
- [ ] OAuth2 integration (optional)
- [ ] Role-based access control
- [ ] Password reset

#### Book Service
- [ ] Book CRUD operations
- [ ] Category management
- [ ] Author management
- [ ] ISBN validation
- [ ] Inventory tracking
- [ ] Book search (Elasticsearch)
- [ ] Book cover upload (MinIO/S3)
- [ ] Advanced filtering

#### User Service
- [ ] User profile management
- [ ] Role assignment
- [ ] User status management
- [ ] Borrowing limits
- [ ] User history
- [ ] User statistics

#### Borrowing Service
- [ ] Borrow book
- [ ] Return book
- [ ] Extend borrowing
- [ ] Calculate fines
- [ ] Borrowing history
- [ ] Overdue tracking
- [ ] Reservation system

---

### Phase 3: Supporting Services (Weeks 7-8)

#### Notification Service
- [ ] Email notifications
- [ ] SMS notifications (optional)
- [ ] Push notifications (optional)
- [ ] Notification templates
- [ ] Notification queue (Kafka)
- [ ] Notification history

#### Saga Orchestrator
- [ ] Borrow book saga
- [ ] Return book saga
- [ ] Compensation logic
- [ ] Saga state machine
- [ ] Saga monitoring

---

### Phase 4: Infrastructure Services (Weeks 9-10)

#### API Gateway
- [ ] Request routing
- [ ] Authentication filter
- [ ] Rate limiting
- [ ] CORS configuration
- [ ] Request/Response logging
- [ ] Circuit breaker integration

#### Service Discovery
- [ ] Eureka server setup
- [ ] Service registration
- [ ] Health checks
- [ ] Load balancing

#### Config Server
- [ ] Centralized configuration
- [ ] Environment-specific configs
- [ ] Config refresh
- [ ] Encryption support

---

### Phase 5: Cross-Cutting Concerns (Weeks 11-12)

#### Logging
- [ ] Structured logging (JSON)
- [ ] Correlation ID tracking
- [ ] ELK Stack setup
- [ ] Log aggregation
- [ ] Log retention policy

#### Monitoring
- [ ] Prometheus setup
- [ ] Service metrics
- [ ] Business metrics
- [ ] Grafana dashboards
- [ ] Alerting rules

#### Tracing
- [ ] Jaeger setup
- [ ] Distributed tracing
- [ ] Trace sampling
- [ ] Performance analysis

#### Security
- [ ] JWT validation
- [ ] API key management
- [ ] SQL injection prevention
- [ ] XSS prevention
- [ ] CSRF protection
- [ ] Secrets management (Vault)
- [ ] Security headers

#### Exception Handling
- [ ] Global exception handler
- [ ] Custom exceptions
- [ ] Error response format
- [ ] Error logging
- [ ] User-friendly messages

#### Interceptors/Middleware
- [ ] Logging interceptor
- [ ] Authentication interceptor
- [ ] Rate limit interceptor
- [ ] Request validation
- [ ] Response transformation

---

### Phase 6: Data Management (Week 13)

#### Caching
- [ ] Redis configuration
- [ ] Cache strategies
- [ ] Cache invalidation
- [ ] Multi-level caching

#### Database Migration
- [ ] Flyway setup
- [ ] Migration scripts
- [ ] Rollback strategies
- [ ] Data seeding

#### Backup & Recovery
- [ ] Database backup strategy
- [ ] Automated backups
- [ ] Disaster recovery plan
- [ ] Data restoration procedures

---

### Phase 7: Testing (Weeks 14-15)

#### Unit Tests
- [ ] Service layer tests
- [ ] Domain logic tests
- [ ] Test coverage > 80%

#### Integration Tests
- [ ] API tests (Rest Assured)
- [ ] Database tests (Testcontainers)
- [ ] Kafka tests
- [ ] Cache tests

#### E2E Tests
- [ ] User flows
- [ ] Saga flows
- [ ] Error scenarios

#### Performance Tests
- [ ] Load testing (K6)
- [ ] Stress testing
- [ ] Spike testing
- [ ] Soak testing

---

### Phase 8: DevOps & CI/CD (Week 16)

#### Docker
- [ ] Dockerfile for each service
- [ ] Multi-stage builds
- [ ] Image optimization
- [ ] Docker Compose setup

#### Kubernetes
- [ ] Deployment manifests
- [ ] Service manifests
- [ ] ConfigMap & Secrets
- [ ] Ingress setup
- [ ] Horizontal Pod Autoscaling
- [ ] Resource limits
- [ ] Liveness/Readiness probes

#### Helm Charts
- [ ] Chart structure
- [ ] Values configuration
- [ ] Chart dependencies
- [ ] Chart testing

#### CI/CD Pipeline
- [ ] Build pipeline
- [ ] Test pipeline
- [ ] Code quality checks (SonarQube)
- [ ] Security scanning
- [ ] Docker image build & push
- [ ] Kubernetes deployment
- [ ] Smoke tests
- [ ] Rollback strategy

---

### Phase 9: Service Mesh (Week 17)

#### Istio Setup
- [ ] Istio installation
- [ ] Sidecar injection
- [ ] Virtual Services
- [ ] Destination Rules
- [ ] Gateway configuration
- [ ] mTLS setup
- [ ] Traffic management
- [ ] Fault injection
- [ ] Observability integration

---

### Phase 10: Advanced Features (Weeks 18-20)

#### Search Optimization
- [ ] Full-text search
- [ ] Faceted search
- [ ] Auto-complete
- [ ] Search suggestions
- [ ] Relevance tuning

#### Analytics
- [ ] Borrowing statistics
- [ ] Popular books
- [ ] User engagement metrics
- [ ] Revenue reports (fines)

#### Recommendations
- [ ] Collaborative filtering
- [ ] Content-based filtering
- [ ] Trending books

#### Multi-tenancy
- [ ] Tenant isolation
- [ ] Tenant configuration
- [ ] Tenant-specific data

---

### Phase 11: Documentation (Week 21)

#### Technical Documentation
- [ ] Architecture Decision Records (ADR)
- [ ] System design docs
- [ ] API documentation (Swagger)
- [ ] Database schema docs
- [ ] Event schemas

#### Operational Documentation
- [ ] Runbooks
- [ ] Deployment guides
- [ ] Troubleshooting guides
- [ ] Monitoring guides
- [ ] Incident response procedures

#### Developer Documentation
- [ ] Setup guides
- [ ] Coding standards
- [ ] Git workflow
- [ ] PR guidelines
- [ ] Testing guidelines

---

### Phase 12: Production Readiness (Week 22)

#### Performance
- [ ] Performance optimization
- [ ] Database indexing
- [ ] Query optimization
- [ ] Caching optimization

#### Scalability
- [ ] Load testing results
- [ ] Horizontal scaling verified
- [ ] Database sharding (if needed)
- [ ] CDN setup (optional)

#### Reliability
- [ ] Chaos engineering tests
- [ ] Circuit breaker verified
- [ ] Retry mechanism tested
- [ ] Graceful degradation

#### Security Audit
- [ ] Penetration testing
- [ ] Dependency vulnerability scan
- [ ] Security best practices review
- [ ] Compliance check (GDPR, etc.)

---

## 📚 Documentation

Detailed documentation is available in the following repositories:

- [**Architecture Documentation**](https://github.com/library-management-system/documentation/blob/main/architecture/README.md)
- [**API Documentation**](https://github.com/library-management-system/documentation/blob/main/api/README. md)
- [**Deployment Guide**](https://github.com/library-management-system/documentation/blob/main/deployment/README. md)
- [**Developer Guide**](https://github.com/library-management-system/documentation/blob/main/developer/README.md)
- [**Runbooks**](https://github.com/library-management-system/documentation/blob/main/runbooks/README.md)

---

## 🤝 Contributing

We welcome contributions!  Please see our [Contributing Guide](CONTRIBUTING.md) for details.

### Development Workflow

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Standards

- Follow Clean Code principles
- Write unit tests (coverage > 80%)
- Update documentation
- Follow commit message conventions

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Team

- **Project Lead**: [@thehappycode](https://github.com/thehappycode)
- **Contributors**: See [CONTRIBUTORS.md](CONTRIBUTORS.md)

---

## 🙏 Acknowledgments

- Spring Boot Team
- Cloud Native Computing Foundation
- Open Source Community

---

## 📞 Contact

- **GitHub Organization**: [library-management-system](https://github.com/library-management-system)
- **Issues**: [Create an Issue](https://github.com/library-management-system/library-management-system/issues)
- **Discussions**: [GitHub Discussions](https://github.com/library-management-system/library-management-system/discussions)

---

<p align="center">Made with ❤️ by the Library Management System Team</p>