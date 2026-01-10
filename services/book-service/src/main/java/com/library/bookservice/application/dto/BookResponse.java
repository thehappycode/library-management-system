package com.library.bookservice.application.dto;

import java.time.LocalDateTime;

import com.library.bookservice.domain.model.BookStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for Book
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private String isbn;
    private String formattedIsbn;
    private String title;
    private String authorName;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String categoryName;

    // Inventory
    private Integer totalQuantity;
    private Integer availableQuantity;
    private Integer borrowedQuantity;

    // Status
    private BookStatus status;
    private String coverImageUrl;

    // Computed fields
    private boolean availableForBorrowing;
    private boolean popular;
    private double borrowRate;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}