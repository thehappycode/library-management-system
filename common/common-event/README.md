# Common Events Module

This module contains shared domain event interfaces and implementations for event-driven architecture across all microservices.

## Purpose

The `common-event` module provides:
- Base domain event interface
- Domain-specific event classes for books, users, borrowing, and notifications
- Event serialization support with Jackson
- Event versioning support

## Contents

### Event Interfaces
- **DomainEvent**: Base interface for all domain events with common properties

### Domain Events
- **BookEvent**: Events related to book operations (created, updated, deleted, borrowed, returned)
- **UserEvent**: Events related to user operations (registered, updated, deleted, activated, deactivated)
- **BorrowingEvent**: Events related to borrowing operations (created, returned, overdue, renewed, fine paid)
- **NotificationEvent**: Events for notifications (email, SMS, push)

## Usage

### 1. Add Dependency

```xml
<dependency>
    <groupId>com.library</groupId>
    <artifactId>common-event</artifactId>
</dependency>
```

### 2. Publish Events

```java
// Create and publish a book created event
BookEvent.BookCreatedEvent event = new BookEvent.BookCreatedEvent(
    book.getId().toString(),
    bookDTO
);

kafkaTemplate.send("book-events", event.getEventId(), event);
```

### 3. Consume Events

```java
@KafkaListener(topics = "book-events", groupId = "book-consumer")
public void handleBookEvent(BookEvent event) {
    switch (event.getEventType()) {
        case "BOOK_CREATED":
            handleBookCreated((BookEvent.BookCreatedEvent) event);
            break;
        case "BOOK_BORROWED":
            handleBookBorrowed((BookEvent.BookBorrowedEvent) event);
            break;
        // Handle other event types
    }
}
```

### 4. Event Types

#### Book Events
- `BookCreatedEvent`: Book was created
- `BookUpdatedEvent`: Book details were updated
- `BookDeletedEvent`: Book was deleted
- `BookBorrowedEvent`: Book was borrowed
- `BookReturnedEvent`: Book was returned

#### User Events
- `UserRegisteredEvent`: New user registered
- `UserUpdatedEvent`: User profile updated
- `UserDeletedEvent`: User was deleted
- `UserActivatedEvent`: User account activated
- `UserDeactivatedEvent`: User account deactivated

#### Borrowing Events
- `BorrowingCreatedEvent`: New borrowing transaction created
- `BorrowingReturnedEvent`: Book was returned
- `BorrowingOverdueEvent`: Borrowing became overdue
- `BorrowingRenewedEvent`: Borrowing was renewed
- `FinePaidEvent`: Fine was paid

#### Notification Events
- `EmailNotificationEvent`: Email notification to be sent
- `SmsNotificationEvent`: SMS notification to be sent
- `PushNotificationEvent`: Push notification to be sent

## Event Structure

All domain events include:
- `eventId`: Unique identifier for the event
- `eventType`: Type/name of the event
- `occurredOn`: Timestamp when the event occurred
- `aggregateId`: ID of the aggregate root that generated the event
- `version`: Event schema version (default: 1)

## Features

- **Type-safe Events**: Strongly typed event classes
- **JSON Serialization**: Jackson annotations for Kafka serialization
- **Event Versioning**: Support for schema evolution
- **Polymorphic Deserialization**: `@JsonTypeInfo` for proper event type handling
- **Immutability**: Events are immutable value objects

## Best Practices

1. **Event Naming**: Use past tense (e.g., `BookCreated`, `UserRegistered`)
2. **Event Granularity**: One event per business action
3. **Event Immutability**: Never modify events after creation
4. **Event Versioning**: Increment version when changing event structure
5. **Backward Compatibility**: Maintain compatibility when evolving events

## Kafka Topic Naming Convention

```
{domain}-events
```

Examples:
- `book-events`
- `user-events`
- `borrowing-events`
- `notification-events`

## Event Sourcing

These events can be used for:
- Event-driven communication between services
- Event sourcing and CQRS patterns
- Audit logging
- Real-time notifications
- Data synchronization
