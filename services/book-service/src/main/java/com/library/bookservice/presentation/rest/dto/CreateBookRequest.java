package com.library.bookservice.presentation.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a book
 * Contains validation annotations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^\\d{10}(\\d{3})?$", message = "ISBN must be 10 or 13 digits")
    private String isbn;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Author name is required")
    @Size(min = 1, max = 100, message = "Author name must be between 1 and 100 characters")
    private String authorName;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotBlank(message = "Category name is required")
    @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters")
    private String categoryName;

    @NotNull(message = "Initial quantity is required")
    @Min(value = 1, message = "Initial quantity must be at least 1")
    @Max(value = 1000, message = "Initial quantity cannot exceed 1000")
    private Integer initialQuantity;
}