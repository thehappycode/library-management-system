# Common Exceptions Module

This module contains shared exception classes and a base global exception handler for all microservices.

## Purpose

The `common-exception` module provides:
- Standard exception types for common error scenarios
- Centralized exception handling with consistent error responses
- Automatic logging and trace ID generation
- Field-level validation error handling

## Contents

### Exception Classes
- **ResourceNotFoundException**: Thrown when a requested resource is not found
- **BusinessException**: Thrown when a business rule is violated
- **ValidationException**: Thrown when input validation fails
- **UnauthorizedException**: Thrown when authentication/authorization fails

### Exception Handler
- **GlobalExceptionHandler**: Base class for handling exceptions across all services

## Usage

### 1. Add Dependency

```xml
<dependency>
    <groupId>com.library</groupId>
    <artifactId>common-exception</artifactId>
</dependency>
```

### 2. Extend GlobalExceptionHandler

Create a service-specific exception handler:

```java
@RestControllerAdvice
public class ServiceExceptionHandler extends GlobalExceptionHandler {
    // Add service-specific exception handlers here if needed
}
```

### 3. Throw Exceptions

```java
// Resource not found
throw new ResourceNotFoundException("Book", "id", bookId);

// Business rule violation
throw new BusinessException("Book is already borrowed");

// Validation error
throw new ValidationException("email", "Invalid email format");

// Unauthorized access
throw new UnauthorizedException("Invalid credentials");
```

## Features

- **Automatic Trace IDs**: Every error response includes a unique trace ID for debugging
- **Structured Error Responses**: Consistent error format using `ErrorResponse` DTO
- **Field-Level Validation**: Detailed validation errors for form submissions
- **Logging**: All exceptions are automatically logged with context

## Error Response Format

```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Book not found with id: '123'",
  "path": "/api/books/123",
  "traceId": "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6"
}
```

## Best Practices

1. **Use Specific Exceptions**: Choose the most appropriate exception type
2. **Include Context**: Provide meaningful error messages
3. **Don't Expose Internals**: Avoid leaking sensitive information in errors
4. **Log Appropriately**: Errors are logged automatically with trace IDs
