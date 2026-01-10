package com.library.bookservice.presentation.rest.mapper;

import org.springframework.stereotype.Component;

import com.library.bookservice.application.dto.BookResponse;
import com.library.bookservice.application.dto.CreateBookCommand;
import com.library.bookservice.application.dto.ReserveBookCommand;
import com.library.bookservice.application.dto.UpdateBookCommand;
import com.library.bookservice.presentation.rest.dto.CreateBookRequest;
import com.library.bookservice.presentation.rest.dto.ReserveBookRequest;
import com.library.bookservice.presentation.rest.dto.UpdateBookRequest;

/**
 * Mapper between Presentation DTOs and Application DTOs
 */
@Component
public class BookDtoMapper {

    /**
     * Map CreateBookRequest (Presentation) → CreateBookCommand (Application)
     */
    public CreateBookCommand toCommand(CreateBookRequest request) {
        return CreateBookCommand.builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .authorName(request.getAuthorName())
                .description(request.getDescription())
                .categoryName(request.getCategoryName())
                .initialQuantity(request.getInitialQuantity())
                .build();
    }

    /**
     * Map UpdateBookRequest (Presentation) → UpdateBookCommand (Application)
     */
    public UpdateBookCommand toCommand(UpdateBookRequest request) {
        return UpdateBookCommand.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .authorName(request.getAuthorName())
                .categoryName(request.getCategoryName())
                .build();
    }

    /**
     * Map ReserveBookRequest (Presentation) → ReserveBookCommand (Application)
     */
    public ReserveBookCommand toCommand(Long bookId, ReserveBookRequest request) {
        return ReserveBookCommand.builder()
                .bookId(bookId)
                .userId(request.getUserId())
                .build();
    }

    /**
     * Map BookResponse (Application) → BookResponse (Presentation)
     */
    public com.library.bookservice.presentation.rest.dto.BookResponse toDto(BookResponse response) {
        return com.library.bookservice.presentation.rest.dto.BookResponse.builder()
                .id(response.getId())
                .isbn(response.getIsbn())
                .formattedIsbn(response.getFormattedIsbn())
                .title(response.getTitle())
                .authorName(response.getAuthorName())
                .authorFirstName(response.getAuthorFirstName())
                .authorLastName(response.getAuthorLastName())
                .description(response.getDescription())
                .categoryName(response.getCategoryName())
                .totalQuantity(response.getTotalQuantity())
                .availableQuantity(response.getAvailableQuantity())
                .borrowedQuantity(response.getBorrowedQuantity())
                .status(response.getStatus().name())
                .coverImageUrl(response.getCoverImageUrl())
                .availableForBorrowing(response.isAvailableForBorrowing())
                .popular(response.isPopular())
                .borrowRate(response.getBorrowRate())
                .createdAt(response.getCreatedAt())
                .updatedAt(response.getUpdatedAt())
                .build();
    }
}