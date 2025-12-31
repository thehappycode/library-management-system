# Monorepo Structure

## Overview

This project uses a **monorepo** architecture where all microservices, common libraries, and infrastructure components reside in a single Git repository.

## Why Monorepo?

### Advantages

1. **Atomic Changes**
   - Make cross-service changes in a single commit
   - No need for coordinating multiple PRs
   - Easy to maintain consistency

2. **Simplified Dependency Management**
   - All services use the same versions
   - Centralized dependency updates
   - No version conflicts

3. **Code Reuse**
   - Common libraries easily accessible
   - Share code without publishing artifacts
   - Consistent coding standards

4. **Improved Developer Experience**
   - Single repository to clone
   - Easier onboarding
   - Consistent tooling

5. **Better CI/CD**
   - Build only changed services
   - Run tests for affected services
   - Simplified deployment pipeline

6. **Enhanced Collaboration**
   - Cross-team visibility
   - Easier code reviews
   - Knowledge sharing

### Challenges & Solutions

| Challenge | Solution |
|-----------|----------|
| **Build Time** | Incremental builds, build caching |
| **Repository Size** | Git LFS for large files, shallow clones |
| **Access Control** | CODEOWNERS file, branch protection |
| **CI/CD Complexity** | Matrix builds, conditional execution |

## Directory Structure

```
library-management-system/
├── common/                          # Shared libraries
│   ├── common-dto/
│   ├── common-exception/
│   ├── common-util/
│   ├── common-security/
│   └── common-event/
├── services/                        # Microservices
│   ├── auth-service/
│   ├── book-service/
│   ├── user-service/
│   ├── borrowing-service/
│   ├── notification-service/
│   └── saga-orchestrator-service/
├── infrastructure/                  # Infrastructure services
│   ├── api-gateway/
│   ├── service-discovery/
│   └── config-server/
├── k8s/                            # Kubernetes manifests
├── helm/                           # Helm charts
├── terraform/                      # Infrastructure as Code
├── observability/                  # Monitoring configurations
├── docs/                           # Documentation
├── scripts/                        # Utility scripts
├── .github/workflows/              # CI/CD pipelines
├── docker-compose.yml              # Local development
└── pom.xml                         # Parent POM
```

## Module Organization

### Common Libraries (`common/`)

Shared code used across multiple services:
- **DTO**: Data transfer objects
- **Exception**: Common exceptions
- **Util**: Utility classes
- **Security**: JWT, authentication
- **Event**: Domain events

**Versioning**: Common libraries version together with the parent

### Services (`services/`)

Independent microservices following DDD + Clean Architecture:
- Each service has its own `pom.xml`
- Each service can be built independently
- Services depend on common libraries

### Infrastructure (`infrastructure/`)

Infrastructure services:
- API Gateway
- Service Discovery (Eureka)
- Config Server

### DevOps (`k8s/`, `helm/`, `terraform/`)

Infrastructure as Code and deployment configs:
- Kubernetes manifests
- Helm charts
- Terraform modules

## Build System

### Parent POM

The root `pom.xml` defines:
- Java version (21)
- Spring Boot version (3.2+)
- Spring Cloud version (2023.0.0)
- Common dependencies
- Plugin configurations
- Modules

### Module Dependencies

```
services/book-service
  └── common/common-dto
  └── common/common-exception
  └── common/common-util
  └── common/common-security
  └── common/common-event
```

### Building

```bash
# Build everything
mvn clean install

# Build specific service and dependencies
mvn clean package -pl services/book-service -am

# Build without tests
mvn clean package -DskipTests

# Build only changed modules (TODO: implement)
mvn clean install -amd
```

## Git Workflow

### Branch Strategy

- `main`: Production-ready code
- `develop`: Integration branch
- `feature/*`: Feature branches
- `hotfix/*`: Hotfix branches

### Commit Conventions

Format: `<type>(<scope>): <subject>`

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation
- `style`: Formatting
- `refactor`: Code restructuring
- `test`: Tests
- `chore`: Maintenance

Examples:
```
feat(book-service): add book search endpoint
fix(auth-service): resolve JWT expiration issue
docs: update README with monorepo structure
```

### Pull Request Process

1. Create feature branch
2. Make changes
3. Run tests locally
4. Create PR to `develop`
5. Automated checks run
6. Code review
7. Merge to `develop`

## CI/CD Pipeline

### Build Workflow

Triggers: PR to main/develop, push to main/develop

Steps:
1. Checkout code
2. Set up JDK 21
3. Build common libraries
4. Build services
5. Build infrastructure
6. Upload artifacts

### Test Workflow

Runs in parallel for:
- Common libraries
- Each service
- Infrastructure services

### Docker Build Workflow

Triggers: Push to main, version tags

Builds and pushes Docker images for all services

## Local Development

### Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose

### Setup

```bash
# Clone repository
git clone <repo-url>
cd library-management-system

# Start infrastructure
docker-compose up -d

# Build all services
./scripts/build-all.sh

# Start all services
./scripts/start-all.sh
```

### Development Workflow

1. Make changes in specific service
2. Rebuild that service: `mvn clean package -pl services/<service> -am`
3. Restart service
4. Test changes
5. Run tests: `mvn test -pl services/<service>`

## Tooling

### Scripts

- `build-all.sh`: Build all modules
- `start-all.sh`: Start all services
- `stop-all.sh`: Stop all services
- `test-all.sh`: Run all tests
- `docker-build-all.sh`: Build Docker images

### IDE Support

- **IntelliJ IDEA**: Import as Maven project
- **VS Code**: Use Java Extension Pack
- **Eclipse**: Import as existing Maven project

## Scalability

As the monorepo grows:

1. **Build Optimization**
   - Use Maven build cache
   - Parallel builds: `mvn -T 4 clean install`
   - Incremental builds

2. **Repository Management**
   - Git LFS for large files
   - Shallow clones: `git clone --depth 1`
   - Sparse checkout for specific services

3. **CI/CD Optimization**
   - Build only affected services
   - Cache dependencies
   - Matrix builds for parallel execution

## Best Practices

1. **Keep common libraries stable**
2. **Version everything together**
3. **Run full test suite before merging**
4. **Document cross-service changes**
5. **Use feature flags for large changes**
6. **Regular dependency updates**
7. **Monitor build times**

## Migration from Multi-Repo

When services were separate repos:
1. Services imported as submodules
2. History preserved
3. Common code extracted to `common/`
4. Build system unified
5. CI/CD pipelines consolidated

## References

- [Monorepo Tools](https://monorepo.tools/)
- [Google's Monorepo Approach](https://cacm.acm.org/magazines/2016/7/204032-why-google-stores-billions-of-lines-of-code-in-a-single-repository/)
- [Monorepo Best Practices](https://www.atlassian.com/git/tutorials/monorepos)
