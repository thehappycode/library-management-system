package com.library.bookservice.presentation.rest.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for Book
 * What client receives
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
    private String status;
    private String coverImageUrl;

    // Computed fields
    private Boolean availableForBorrowing;
    private Boolean popular;
    private Double borrowRate;

    // Timestamps
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}