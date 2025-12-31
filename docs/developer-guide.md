# Developer Guide

## Getting Started

This guide will help you set up your development environment and start contributing to the Library Management System.

## Prerequisites

### Required Software

- **Java 21**: [Download](https://adoptium.net/)
- **Maven 3.9+**: [Download](https://maven.apache.org/download.cgi)
- **Docker & Docker Compose**: [Download](https://www.docker.com/products/docker-desktop)
- **Git**: [Download](https://git-scm.com/downloads)

### Optional Software

- **IntelliJ IDEA** or **VS Code** for IDE
- **Postman** or **curl** for API testing
- **kubectl** for Kubernetes (future)

## Environment Setup

### 1. Clone the Repository

```bash
git clone https://github.com/thehappycode/library-management-system.git
cd library-management-system
```

### 2. Start Infrastructure Services

```bash
# Start all infrastructure (databases, Kafka, Redis, etc.)
docker-compose up -d

# Verify all services are running
docker-compose ps
```

### 3. Build the Project

```bash
# Build all modules
./scripts/build-all.sh

# Or manually with Maven
mvn clean install
```

### 4. Run Services

```bash
# Start all services
./scripts/start-all.sh

# Or manually start specific service
mvn spring-boot:run -pl services/book-service
```

## Project Structure

```
library-management-system/
├── common/              # Shared libraries
├── services/            # Microservices
├── infrastructure/      # Infrastructure services
├── docs/               # Documentation
└── scripts/            # Utility scripts
```

## Development Workflow

### Working on a Feature

1. **Create a Feature Branch**
   ```bash
   git checkout -b feature/add-book-search
   ```

2. **Make Changes**
   - Edit code in the relevant service
   - Follow DDD + Clean Architecture principles
   - Write tests

3. **Build and Test**
   ```bash
   # Build specific service
   mvn clean package -pl services/book-service -am
   
   # Run tests
   mvn test -pl services/book-service
   ```

4. **Run Service Locally**
   ```bash
   mvn spring-boot:run -pl services/book-service
   ```

5. **Test Your Changes**
   ```bash
   curl http://localhost:8081/api/books
   ```

6. **Commit Changes**
   ```bash
   git add .
   git commit -m "feat(book-service): add book search endpoint"
   ```

7. **Push and Create PR**
   ```bash
   git push origin feature/add-book-search
   ```

## Building and Testing

### Build Commands

```bash
# Build everything
mvn clean install

# Build specific service
mvn clean package -pl services/book-service -am

# Build without tests
mvn clean package -DskipTests

# Build only changed modules
mvn clean install -amd
```

### Test Commands

```bash
# Test all
./scripts/test-all.sh

# Test specific service
mvn test -pl services/book-service

# Integration tests
mvn verify -pl services/book-service

# Test with coverage
mvn test jacoco:report
```

### Docker Build

```bash
# Build all Docker images
./scripts/docker-build-all.sh

# Build specific service image
docker build -t library/book-service:latest -f services/book-service/Dockerfile .
```

## Code Style Guidelines

### Java Code Style

- Use Java 21 features
- Follow Google Java Style Guide
- Use Lombok to reduce boilerplate
- Write meaningful variable names
- Keep methods small and focused

### Architecture Guidelines

**Follow DDD + Clean Architecture**:

```
service/
├── domain/           # Pure business logic
├── application/      # Use cases
├── infrastructure/   # Technical implementations
└── presentation/     # REST API
```

**Dependency Rule**: Dependencies point inward
```
Presentation → Application → Domain ← Infrastructure
```

### Naming Conventions

- **Classes**: `PascalCase` (e.g., `BookService`)
- **Methods**: `camelCase` (e.g., `findBookById`)
- **Constants**: `UPPER_SNAKE_CASE` (e.g., `MAX_BOOKS`)
- **Packages**: `lowercase` (e.g., `com.library.book`)

## Testing Guidelines

### Unit Tests

- Test business logic in domain layer
- Mock dependencies
- Use JUnit 5 and Mockito

```java
@Test
void shouldBorrowBookWhenAvailable() {
    // Arrange
    Book book = createAvailableBook();
    
    // Act
    book.borrowCopy();
    
    // Assert
    assertEquals(9, book.getAvailableCopies());
}
```

### Integration Tests

- Use Testcontainers for databases
- Test with real dependencies
- Test REST endpoints

```java
@SpringBootTest
@Testcontainers
class BookControllerIntegrationTest {
    
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");
    
    @Test
    void shouldCreateBook() {
        // Test implementation
    }
}
```

## Common Tasks

### Add New Endpoint

1. Define use case in application layer
2. Implement use case
3. Add REST endpoint in presentation layer
4. Write tests
5. Document in OpenAPI

### Add New Service

1. Create service directory in `services/`
2. Create `pom.xml` with dependencies
3. Create DDD structure
4. Add application class
5. Configure `application.yml`
6. Add to parent POM modules
7. Create Dockerfile
8. Create README

### Update Common Library

1. Make changes in `common/` module
2. Build common module: `mvn install -pl common/common-dto`
3. Rebuild dependent services
4. Test all affected services

### Add New Dependency

1. Add to parent POM `dependencyManagement`
2. Add to service POM `dependencies`
3. Use the dependency in code

## Debugging

### Debug Service in IDE

**IntelliJ IDEA**:
1. Open Run Configuration
2. Select Spring Boot application
3. Set module to specific service
4. Add breakpoints
5. Run in Debug mode

**VS Code**:
1. Create launch.json configuration
2. Set main class
3. Add breakpoints
4. Press F5 to debug

### Debug with Logs

```bash
# View service logs
tail -f logs/book-service.log

# View Docker logs
docker-compose logs -f mysql-book

# Enable debug logging
# Edit application-dev.yml
logging:
  level:
    com.library: DEBUG
```

### Debug REST APIs

```bash
# Using curl
curl -v http://localhost:8081/api/books

# Using Postman
# Import OpenAPI spec from http://localhost:8081/api-docs
```

## Useful Commands

```bash
# Check service health
curl http://localhost:8081/actuator/health

# View metrics
curl http://localhost:8081/actuator/metrics

# Restart service
./scripts/stop-all.sh
./scripts/start-all.sh

# Clean and rebuild
mvn clean install -DskipTests

# View running services
docker-compose ps
ps aux | grep java
```

## IDE Setup

### IntelliJ IDEA

1. Import project as Maven project
2. Enable annotation processing (for Lombok)
3. Install Lombok plugin
4. Set Java SDK to 21
5. Import code style (Google Java Style)

### VS Code

1. Install Java Extension Pack
2. Install Spring Boot Extension Pack
3. Open project folder
4. Maven will automatically configure

## Troubleshooting

### Build Failures

```bash
# Clear Maven cache
rm -rf ~/.m2/repository

# Rebuild
mvn clean install
```

### Port Already in Use

```bash
# Find process using port
lsof -i :8081

# Kill process
kill -9 <PID>
```

### Docker Issues

```bash
# Restart Docker
docker-compose down
docker-compose up -d

# Clean Docker
docker system prune -a
```

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Maven Documentation](https://maven.apache.org/guides/)
- [DDD Reference](https://www.domainlanguage.com/ddd/)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## Getting Help

- Check service README in `services/*/README.md`
- Review documentation in `docs/`
- Ask team members
- Create GitHub issue for bugs
