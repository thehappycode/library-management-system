# Config Server

Centralized configuration management for all microservices using Spring Cloud Config.

## Purpose

Provides externalized configuration management for all services:
- Central configuration repository
- Environment-specific configurations
- Dynamic configuration updates
- Configuration versioning with Git

## Features

- Git backend support
- Native (filesystem) support for development
- Configuration encryption (TODO)
- Refresh configurations without restart
- Multiple profiles (dev, prod, etc.)

## Technology Stack

- Spring Cloud Config Server
- Git (optional backend)

## Configuration Backends

### Native (Development)
Uses local filesystem: `src/main/resources/config/`

### Git (Production)
Uses Git repository for versioned configurations.

Set these environment variables:
- `CONFIG_GIT_URI`: Git repository URL
- `CONFIG_GIT_USERNAME`: Git username (if private repo)
- `CONFIG_GIT_PASSWORD`: Git password/token

## Configuration Structure

```
config-repo/
├── application.yml          # Shared config for all services
├── auth-service.yml         # Auth service specific config
├── book-service.yml         # Book service specific config
├── auth-service-dev.yml     # Auth service dev profile
└── auth-service-prod.yml    # Auth service prod profile
```

## Configuration Priority

1. `{service-name}-{profile}.yml`
2. `{service-name}.yml`
3. `application-{profile}.yml`
4. `application.yml`

## Configuration

Port: 8888

Environment variables:
- `CONFIG_GIT_URI`, `CONFIG_GIT_USERNAME`, `CONFIG_GIT_PASSWORD`
- `EUREKA_URI`

## Building & Running

```bash
mvn clean package -pl infrastructure/config-server -am
mvn spring-boot:run -pl infrastructure/config-server
```

## Accessing Configurations

- `http://localhost:8888/{service-name}/{profile}`
- `http://localhost:8888/{service-name}/{profile}/{label}`

Example:
- `http://localhost:8888/book-service/default`
- `http://localhost:8888/book-service/prod`

## Refresh Configuration

Services can refresh their configuration by calling:
```
POST /actuator/refresh
```

## TODO

- [ ] Set up Git repository for configurations
- [ ] Enable configuration encryption
- [ ] Add authentication for config server
- [ ] Set up webhook for auto-refresh on Git push
