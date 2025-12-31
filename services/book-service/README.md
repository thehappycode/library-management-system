# Book Service

Microservice responsible for managing the book catalog, inventory, and search operations in the Library Management System.

## Features

- **Book CRUD Operations**: Create, read, update, and delete books
- **Category Management**: Organize books by categories
- **Inventory Tracking**: Track available copies and total copies
- **Search & Filtering**: Advanced search with filters
- **Event Publishing**: Publishes domain events for book operations

## Architecture

This service follows **DDD (Domain-Driven Design) + Clean Architecture** principles:

### Layers

1. **Domain Layer** (`domain/`)
   - `model/`: Entities, Value Objects, Aggregates
   - `repository/`: Repository interfaces (Ports)
   - `service/`: Domain services
   - `event/`: Domain events

2. **Application Layer** (`application/`)
   - `usecase/`: Use case implementations
   - `dto/`: Commands, Queries, Responses
   - `port/input/`: Input ports (interfaces for use cases)
   - `port/output/`: Output ports (interfaces for external dependencies)

3. **Infrastructure Layer** (`infrastructure/`)
   - `persistence/`: JPA entities, repositories, mappers
   - `messaging/kafka/`: Kafka producers and consumers
   - `cache/`: Redis cache implementation
   - `http/`: REST clients for other services
   - `config/`: Configuration classes

4. **Presentation Layer** (`presentation/`)
   - `rest/`: REST controllers
   - `exception/`: Exception handlers
   - `interceptor/`: Request/response interceptors
   - `validation/`: Custom validators

## Technology Stack

- **Java 21**
- **Spring Boot 3.2+**
- **Spring Data JPA** (MySQL)
- **Spring Cloud** (Eureka, Config)
- **Spring Kafka**
- **Redis** (Caching)
- **Flyway** (Database migrations)
- **MapStruct** (Entity-DTO mapping)
- **Lombok** (Code generation)
- **SpringDoc OpenAPI** (API documentation)

## API Endpoints

- `GET /api/books` - List all books (paginated)
- `GET /api/books/{id}` - Get book by ID
- `POST /api/books` - Create a new book
- `PUT /api/books/{id}` - Update book
- `DELETE /api/books/{id}` - Delete book
- `GET /api/books/search` - Search books
- `GET /api/books/category/{category}` - Get books by category

## Configuration

Required environment variables:

```bash
DB_USERNAME=root
DB_PASSWORD=password
REDIS_HOST=localhost
REDIS_PORT=6379
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
EUREKA_URI=http://localhost:8761/eureka
```

## Building & Running

### Local Development

```bash
# Build
mvn clean package -pl services/book-service -am

# Run
mvn spring-boot:run -pl services/book-service
```

### Docker

```bash
# Build image
docker build -t book-service:latest -f services/book-service/Dockerfile .

# Run container
docker run -p 8081:8081 book-service:latest
```

## Testing

```bash
# Run tests
mvn test -pl services/book-service

# Run integration tests
mvn verify -pl services/book-service
```

## Database Migrations

Flyway migrations are located in `src/main/resources/db/migration/`

- `V1__create_books_table.sql` - Initial schema
- `V2__add_book_categories.sql` - Add categories

## Events Published

- `BookCreatedEvent` - When a new book is created
- `BookUpdatedEvent` - When book details are updated
- `BookDeletedEvent` - When a book is deleted
- `BookBorrowedEvent` - When a book is borrowed
- `BookReturnedEvent` - When a book is returned

## Dependencies

- Common DTO
- Common Exception
- Common Util
- Common Security
- Common Event

## API Documentation

Swagger UI: `http://localhost:8081/swagger-ui.html`

API Docs: `http://localhost:8081/api-docs`

## Monitoring

Health Check: `http://localhost:8081/actuator/health`

Metrics: `http://localhost:8081/actuator/metrics`
