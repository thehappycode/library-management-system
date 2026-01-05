package com.library.bookservice.domain.model;

/**
 * Enum representing the status of a Book
 */
public enum BookStatus {
    AVAILABLE("Available for borrowing"),
    OUT_OF_STOCK("All copies are borrowed"),
    DISCONTINUED("No longer available");

    private final String description;

    BookStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return this == AVAILABLE;
    }
}
