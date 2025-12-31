# Common DTOs Module

This module contains shared Data Transfer Objects (DTOs) used across all microservices in the Library Management System.

## Purpose

The `common-dto` module provides:
- Standardized API response formats
- Domain-specific DTOs for inter-service communication
- Validation annotations
- Consistent data structures across services

## Contents

### Generic Response DTOs
- **ApiResponse**: Generic wrapper for all API responses with success/error indication
- **PageResponse**: Paginated response wrapper for list endpoints
- **ErrorResponse**: Standardized error response with field-level validation errors

### Domain DTOs
- **BookDTO**: Data transfer object for book entities
- **UserDTO**: Data transfer object for user entities
- **BorrowingDTO**: Data transfer object for borrowing transactions

## Usage

Add this dependency to your service's `pom.xml`:

```xml
<dependency>
    <groupId>com.library</groupId>
    <artifactId>common-dto</artifactId>
</dependency>
```

### Example: Using ApiResponse

```java
@GetMapping("/books/{id}")
public ApiResponse<BookDTO> getBook(@PathVariable Long id) {
    BookDTO book = bookService.findById(id);
    return ApiResponse.success("Book retrieved successfully", book);
}
```

### Example: Using PageResponse

```java
@GetMapping("/books")
public ApiResponse<PageResponse<BookDTO>> getBooks(Pageable pageable) {
    PageResponse<BookDTO> books = bookService.findAll(pageable);
    return ApiResponse.success(books);
}
```

## Design Principles

- **Immutability**: Use builders and avoid setters where appropriate
- **Validation**: Include Jakarta Bean Validation annotations
- **JSON Serialization**: Exclude null values with `@JsonInclude`
- **Documentation**: All fields are well-documented with JavaDoc

## Dependencies

- Lombok: For reducing boilerplate code
- Jackson: For JSON serialization/deserialization
- Jakarta Validation: For validation annotations
