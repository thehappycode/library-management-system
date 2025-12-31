# DDD + Clean Architecture Implementation Guide

## Overview

This document explains how Domain-Driven Design (DDD) and Clean Architecture principles are implemented in each microservice.

## Clean Architecture Layers

### 1. Domain Layer (`domain/`)

**Purpose**: Core business logic and domain models

**Characteristics**:
- Framework-independent
- Database-independent
- UI-independent
- Contains pure business rules

**Structure**:
```
domain/
├── model/              # Entities, Value Objects, Aggregates
├── repository/         # Repository interfaces (Ports)
├── service/            # Domain services
└── event/              # Domain events
```

**Example - Book Entity**:
```java
@Entity
public class Book {
    private BookId id;
    private ISBN isbn;
    private Title title;
    private Author author;
    private Inventory inventory;
    
    public boolean isAvailableForBorrowing() {
        return inventory.hasAvailableCopies();
    }
    
    public void borrowCopy() {
        if (!isAvailableForBorrowing()) {
            throw new BookNotAvailableException();
        }
        inventory.decrementAvailable();
    }
}
```

### 2. Application Layer (`application/`)

**Purpose**: Orchestrate domain logic and implement use cases

**Characteristics**:
- Coordinates domain objects
- Manages transactions
- Publishes domain events
- Independent of infrastructure details

**Structure**:
```
application/
├── usecase/            # Use case implementations
├── dto/                # Commands, Queries, Responses
└── port/               # Ports (interfaces)
    ├── input/          # Driving ports (API interfaces)
    └── output/         # Driven ports (Repository, Event Publisher)
```

**Example - Borrow Book Use Case**:
```java
@Service
public class BorrowBookUseCase implements BorrowBookInputPort {
    
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowingRepository borrowingRepository;
    private final EventPublisher eventPublisher;
    
    @Transactional
    @Override
    public BorrowingResponse execute(BorrowBookCommand command) {
        // 1. Load aggregates
        Book book = bookRepository.findById(command.getBookId())
            .orElseThrow(() -> new BookNotFoundException());
        
        User user = userRepository.findById(command.getUserId())
            .orElseThrow(() -> new UserNotFoundException());
        
        // 2. Execute domain logic
        book.borrowCopy();
        Borrowing borrowing = Borrowing.create(user, book);
        
        // 3. Persist changes
        bookRepository.save(book);
        borrowingRepository.save(borrowing);
        
        // 4. Publish domain event
        eventPublisher.publish(new BookBorrowedEvent(borrowing));
        
        return BorrowingResponse.from(borrowing);
    }
}
```

### 3. Infrastructure Layer (`infrastructure/`)

**Purpose**: Technical implementations and external dependencies

**Characteristics**:
- Framework-dependent
- Database implementations
- External service clients
- Technical concerns

**Structure**:
```
infrastructure/
├── persistence/        # Database implementation
│   ├── entity/        # JPA entities
│   ├── repository/    # JPA repositories
│   └── mapper/        # Domain ↔ Entity mappers
├── messaging/         # Kafka producers/consumers
├── cache/             # Redis caching
├── http/              # REST clients
└── config/            # Configuration classes
```

**Example - JPA Repository Adapter**:
```java
@Repository
public class BookRepositoryAdapter implements BookRepository {
    
    private final JpaBookRepository jpaRepository;
    private final BookMapper mapper;
    
    @Override
    public Optional<Book> findById(BookId id) {
        return jpaRepository.findById(id.getValue())
            .map(mapper::toDomain);
    }
    
    @Override
    public void save(Book book) {
        BookEntity entity = mapper.toEntity(book);
        jpaRepository.save(entity);
    }
}
```

### 4. Presentation Layer (`presentation/`)

**Purpose**: External interfaces (REST APIs)

**Characteristics**:
- Request/response handling
- Input validation
- Exception handling
- API documentation

**Structure**:
```
presentation/
├── rest/              # REST controllers
├── exception/         # Exception handlers
├── interceptor/       # Interceptors
└── validation/        # Custom validators
```

**Example - REST Controller**:
```java
@RestController
@RequestMapping("/api/books")
public class BookController {
    
    private final BorrowBookInputPort borrowBookUseCase;
    
    @PostMapping("/{bookId}/borrow")
    public ResponseEntity<ApiResponse<BorrowingResponse>> borrowBook(
            @PathVariable Long bookId,
            @Valid @RequestBody BorrowBookRequest request) {
        
        BorrowBookCommand command = BorrowBookCommand.builder()
            .bookId(bookId)
            .userId(request.getUserId())
            .dueDate(request.getDueDate())
            .build();
        
        BorrowingResponse response = borrowBookUseCase.execute(command);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
```

## DDD Building Blocks

### Entities
Objects with identity that persists over time

```java
public class Book extends AggregateRoot<BookId> {
    private final BookId id;
    private Title title;
    private ISBN isbn;
    // ... identity-based equality
}
```

### Value Objects
Immutable objects defined by their attributes

```java
@Value
public class ISBN {
    String value;
    
    public ISBN(String value) {
        if (!isValid(value)) {
            throw new InvalidISBNException();
        }
        this.value = value;
    }
}
```

### Aggregates
Cluster of entities and value objects with a root

```java
public class Book extends AggregateRoot<BookId> {
    private Inventory inventory;  // Part of aggregate
    private List<Review> reviews;  // Part of aggregate
    
    // Enforce invariants
    public void addReview(Review review) {
        if (reviews.size() >= MAX_REVIEWS) {
            throw new TooManyReviewsException();
        }
        reviews.add(review);
    }
}
```

### Repositories
Manage aggregate persistence

```java
public interface BookRepository {
    Optional<Book> findById(BookId id);
    List<Book> findByCategory(Category category);
    void save(Book book);
    void delete(Book book);
}
```

### Domain Services
Operations that don't belong to any entity

```java
@Service
public class OverdueFineCalculator {
    
    public Money calculateFine(Borrowing borrowing) {
        int daysOverdue = borrowing.getDaysOverdue();
        Money dailyFine = Money.of(1.00);
        return dailyFine.multiply(daysOverdue);
    }
}
```

### Domain Events
Represent something that happened in the domain

```java
public class BookBorrowedEvent implements DomainEvent {
    private final BookId bookId;
    private final UserId userId;
    private final LocalDateTime borrowedAt;
    private final LocalDateTime dueDate;
}
```

## Dependency Rule

Dependencies point inward:
```
Presentation → Application → Domain ← Infrastructure
```

- Domain has no dependencies
- Application depends only on Domain
- Infrastructure depends on Domain
- Presentation depends on Application

## Testing Strategy

### Unit Tests
- Test domain logic in isolation
- Mock dependencies
- Fast execution

### Integration Tests
- Test with real database (Testcontainers)
- Test Kafka integration
- Test REST APIs

### Architecture Tests
- Verify layer dependencies
- Enforce Clean Architecture rules

```java
@Test
public void domainLayerShouldNotDependOnInfrastructure() {
    classes()
        .that().resideInAPackage("..domain..")
        .should().onlyDependOnClassesThat()
        .resideInAnyPackage("..domain..", "java..")
        .check(importedClasses);
}
```

## Benefits

1. **Testability**: Easy to test business logic
2. **Maintainability**: Clear separation of concerns
3. **Flexibility**: Easy to change technical details
4. **Team Collaboration**: Clear boundaries
5. **Domain Focus**: Business logic is central
