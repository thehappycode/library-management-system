# Service Discovery (Eureka Server)

Netflix Eureka Server for service registration and discovery in the Library Management System.

## Purpose

All microservices register with Eureka Server and use it to discover other services dynamically. This enables:
- Dynamic service discovery
- Client-side load balancing
- Health monitoring
- Resilient communication between services

## Features

- Service registration
- Service discovery
- Health checks
- Dashboard UI
- Self-preservation mode

## Technology Stack

- Spring Cloud Netflix Eureka Server

## Dashboard

Access the Eureka dashboard at: http://localhost:8761

The dashboard shows:
- Registered service instances
- Service health status
- Service URLs and metadata

## Configuration

Port: 8761

No external dependencies required.

## Building & Running

```bash
mvn clean package -pl infrastructure/service-discovery -am
mvn spring-boot:run -pl infrastructure/service-discovery
```

## Docker

```bash
docker build -t service-discovery:latest -f infrastructure/service-discovery/Dockerfile .
docker run -p 8761:8761 service-discovery:latest
```

## Service Registration

Services register with Eureka by adding these properties:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
    fetch-registry: true
    register-with-eureka: true
```

## Monitoring

Health: http://localhost:8761/actuator/health
