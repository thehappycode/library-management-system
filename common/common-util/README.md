# Common Utilities Module

This module contains shared utility classes for common operations across all microservices.

## Purpose

The `common-util` module provides:
- Date and time manipulation utilities
- String processing and validation utilities
- General validation helpers
- JSON serialization/deserialization utilities

## Contents

### Utility Classes
- **DateUtils**: Date and time operations, formatting, parsing
- **StringUtils**: String manipulation, validation, masking
- **ValidationUtils**: Object and collection validation
- **JsonUtils**: JSON serialization and deserialization

## Usage

### 1. Add Dependency

```xml
<dependency>
    <groupId>com.library</groupId>
    <artifactId>common-util</artifactId>
</dependency>
```

### 2. Use Utilities

#### DateUtils Example

```java
// Current date operations
LocalDate today = DateUtils.now();
LocalDateTime now = DateUtils.nowDateTime();

// Date formatting
String formatted = DateUtils.formatDate(LocalDate.now());

// Date calculations
long days = DateUtils.daysBetween(startDate, endDate);
LocalDate futureDate = DateUtils.addDays(today, 14);
```

#### StringUtils Example

```java
// String validation
boolean isEmpty = StringUtils.isEmpty(str);
boolean isValid = StringUtils.isValidEmail("user@example.com");

// String manipulation
String capitalized = StringUtils.capitalize("hello");
String masked = StringUtils.maskEmail("user@example.com"); // "us***@example.com"
```

#### ValidationUtils Example

```java
// Object validation
ValidationUtils.requireNonNull(user, "User cannot be null");
ValidationUtils.isTrue(age > 0, "Age must be positive");

// Collection validation
if (ValidationUtils.isNotEmpty(list)) {
    // Process list
}
```

#### JsonUtils Example

```java
// Object to JSON
String json = JsonUtils.toJson(book);
String prettyJson = JsonUtils.toPrettyJson(book);

// JSON to Object
BookDTO book = JsonUtils.fromJson(json, BookDTO.class);
List<BookDTO> books = JsonUtils.fromJson(json, new TypeReference<List<BookDTO>>() {});
```

## Features

- **Thread-safe**: All utility methods are stateless and thread-safe
- **Null-safe**: Proper null handling in all methods
- **Logging**: Error logging for debugging
- **Performance**: Optimized for common use cases

## Best Practices

1. **Static Import**: Import utility methods statically for cleaner code
   ```java
   import static com.library.common.util.StringUtils.isEmpty;
   ```

2. **Validation**: Use ValidationUtils for input validation
3. **Date Handling**: Use DateUtils for consistent date operations
4. **JSON**: Use JsonUtils for all JSON operations to ensure consistency
