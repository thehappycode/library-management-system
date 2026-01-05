package com.library.bookservice.domain.model;

import lombok.Value;

/**
 * ISBN Value Object
 * 
 * Represents the International Standard Book Number (ISBN) for books.
 * Encapsulates validation and formatting logic for ISBN-10 and ISBN-13.
 * Immutable and self-validating
 */

@Value
public class ISBN {
    String value;

    private ISBN(String value) {
        this.value = value;
    }

    /**
     * Factory method to create an ISBN with validation
     * 
     * @param value Raw ISBN string
     * @return Validated ISBN
     * @throws IllegalArgumentException if the ISBN format is invalid
     */
    public static ISBN of(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }

        // Basic validation logic (can be extended)
        String cleaned = value.replaceAll("-", "").replaceAll(" ", "");

        if (!cleaned.matches("^\\d{10}$") && !cleaned.matches("^\\d{13}$")) {
            throw new IllegalArgumentException("ISBN must be either 10 or 13 digits long");
        }

        // Validate checksum for ISBN-10
        if (cleaned.length() == 10 && !isValidISBN10(cleaned)) {
            throw new IllegalArgumentException("Invalid ISBN-10 checksum");
        }

        // Validate checksum for ISBN-13
        if (cleaned.length() == 13 && !isValidISBN13(cleaned)) {
            throw new IllegalArgumentException("Invalid ISBN-13 checksum");
        }

        return new ISBN(cleaned);
    }

    /**
     * Validate ISBN-10 checksum
     * 
     * @param isbn ISBN-10 string
     * @return true if valid, false otherwise
     */
    private static boolean isValidISBN10(String isbn) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (digit * (10 - i));
        }

        char lastChar = isbn.charAt(9);
        int checksum = (lastChar == 'X') ? 10 : Character.getNumericValue(lastChar);
        sum += checksum;

        return (sum % 11 == 0);
    }

    /**
     * Validate ISBN-13 checksum
     * 
     * @param isbn ISBN-13 string
     * @return true if valid, false otherwise
     */
    private static boolean isValidISBN13(String isbn) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checksum = Character.getNumericValue(isbn.charAt(12));
        int calculatedChecksum = (10 - (sum % 10)) % 10;

        return checksum == calculatedChecksum;
    }

    /**
     * Get formatted ISBN string
     * 
     * @return Formatted ISBN
     */
    public String formatted() {
        if (value.length() == 10) {
            return String.format("%s-%s-%s-%s",
                    value.substring(0, 1),
                    value.substring(1, 5),
                    value.substring(5, 9),
                    value.substring(9));
        } else {
            return String.format("%s-%s-%s-%s-%s",
                    value.substring(0, 3),
                    value.substring(3, 4),
                    value.substring(4, 9),
                    value.substring(9, 12),
                    value.substring(12));
        }
    }
}
