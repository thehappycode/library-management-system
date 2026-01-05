package com.library.bookservice.domain.model;

import lombok.Value;

/**
 * Value Object representing an Author
 * Immutable and encapsulates author-related data
 */

@Value
public class Author {
    String name;

    private Author(String name) {
        this.name = name;
    }

    /**
     * Factory method to create an Author with validation
     * 
     * @param name Author's name
     * @return Author instance
     * @throws IllegalArgumentException if the name is null or empty
     */
    public static Author of(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }

        name = name.trim();

        if (name.length() > 100) {
            throw new IllegalArgumentException("Author name cannot exceed 100 characters");
        }
        return new Author(name);
    }

    /**
     * Get the first name of the author
     * 
     * @return First name
     */
    public String getFirstName() {
        String[] parts = name.split(" ");
        return parts.length > 0 ? parts[0] : "";
    }

    /**
     * Get the last name of the author
     * 
     * @return Last name
     */
    public String getLastName() {
        String[] parts = name.split(" ");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }
}
