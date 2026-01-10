package com.library.bookservice.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.bookservice.application.dto.BookResponse;
import com.library.bookservice.application.port.output.BookRepositoryPort;
import com.library.bookservice.domain.model.Book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Use Case: Get book by ID
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class GetBookByIdUseCase {

    private final BookRepositoryPort bookRepository;

    @Transactional(readOnly = true)
    public BookResponse execute(Long bookId) {
        log.info("Getting book by ID: {}", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        return mapToResponse(book);
    }

    /**
     * Map Book entity to BookResponse DTO
     * 
     * @param book
     * @return
     */
    private BookResponse mapToResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn().getValue())
                .formattedIsbn(book.getFormattedIsbn())
                .title(book.getTitle())
                .authorName(book.getAuthor().getName())
                .authorFirstName(book.getAuthorFirstName())
                .authorLastName(book.getAuthorLastName())
                .description(book.getDescription())
                .categoryName(book.getCategory().getName())
                .totalQuantity(book.getInventory().getTotalQuantity())
                .availableQuantity(book.getInventory().getAvailableQuantity())
                .borrowedQuantity(book.getInventory().getBorrowedQuantity())
                .status(book.getStatus())
                .coverImageUrl(book.getCoverImageUrl())
                .availableForBorrowing(book.isAvailableForBorrowing())
                .popular(book.isPopular())
                .borrowRate(book.getInventory().getBorrowRate())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}