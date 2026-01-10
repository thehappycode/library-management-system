package com.library.bookservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating an existing Book
 * Command to update book details
 * Immutable after construction
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookCommand {
    private String title;
    private String description;
    private String authorName;
    private String categoryName;
}
