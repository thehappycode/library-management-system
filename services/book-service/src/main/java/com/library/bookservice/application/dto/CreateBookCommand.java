package com.library.bookservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new Book
 * Command to create a new book
 * Immutable after construction
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookCommand {
    private String isbn;
    private String title;
    private String authorName;
    private String description;
    private String categoryName;
    private Integer initialQuantity;
}
