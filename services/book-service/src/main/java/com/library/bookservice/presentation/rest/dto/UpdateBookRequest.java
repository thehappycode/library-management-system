package com.library.bookservice.presentation.rest.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating a book
 * All fields are optional
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookRequest {

    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Size(min = 1, max = 100, message = "Author name must be between 1 and 100 characters")
    private String authorName;

    @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters")
    private String categoryName;
}