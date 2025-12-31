package com.library.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for Book entity
 * Used for transferring book data between services
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDTO {
    
    private Long id;
    
    @NotBlank(message = "ISBN is required")
    private String isbn;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Author is required")
    private String author;
    
    private String description;
    
    private String publisher;
    
    private LocalDate publicationDate;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    private List<String> tags;
    
    @NotNull(message = "Total copies is required")
    @Positive(message = "Total copies must be positive")
    private Integer totalCopies;
    
    @NotNull(message = "Available copies is required")
    private Integer availableCopies;
    
    private String language;
    
    private Integer pages;
    
    private String coverImageUrl;
    
    private BookStatus status;
    
    /**
     * Enum representing the status of a book
     */
    public enum BookStatus {
        AVAILABLE,
        BORROWED,
        RESERVED,
        MAINTENANCE,
        LOST,
        DAMAGED
    }
}
