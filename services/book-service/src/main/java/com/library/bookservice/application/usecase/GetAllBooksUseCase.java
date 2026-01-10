package com.library.bookservice.application.usecase;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.bookservice.application.dto.BookResponse;
import com.library.bookservice.application.port.output.BookRepositoryPort;
import com.library.bookservice.domain.model.Book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Use Case: Get all books
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllBooksUseCase {

    private final BookRepositoryPort bookRepository;

    @Transactional(readOnly = true)
    public List<BookResponse> execute() {
        log.info("Getting all books");

        List<Book> books = bookRepository.findAll();

        log.info("Found {} books", books.size());

        return books.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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