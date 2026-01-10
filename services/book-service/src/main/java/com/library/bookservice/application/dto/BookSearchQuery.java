package com.library.bookservice.application.dto;

import com.library.bookservice.domain.model.BookStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for searching books with various criteria
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchQuery {

    private String keyword;
    private String categoryName;
    private String authorName;
    private BookStatus status;
    private Boolean availableOnly;

    // Pagination parameters
    private int page;
    private int size;
    private String sortBy;
    private String sortDirection; // ASC or DESC

}
