package com.library.bookservice.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.bookservice.application.dto.BookResponse;
import com.library.bookservice.application.dto.CreateBookCommand;
import com.library.bookservice.application.port.output.BookRepositoryPort;
import com.library.bookservice.application.port.output.CategoryRepositoryPort;
import com.library.bookservice.application.port.output.EventPublisherPort;
import com.library.bookservice.domain.event.BookEvent;
import com.library.bookservice.domain.model.Author;
import com.library.bookservice.domain.model.Book;
import com.library.bookservice.domain.model.Category;
import com.library.bookservice.domain.model.ISBN;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Use Case: Create a new book
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateBookUseCase {

    private final BookRepositoryPort bookRepository;
    private final CategoryRepositoryPort categoryRepository;
    private final EventPublisherPort eventPublisher;

    @Transactional
    public BookResponse execute(CreateBookCommand command) {

        log.info("Creating book with ISBN: {}", command.getIsbn());

        // 1. Validate business rules
        validateCommand(command);

        // 2. Check if book already exists
        if (bookRepository.existsByIsbn(command.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + command.getIsbn() + " already exists");
        }

        // 3. Get or create category
        var category = categoryRepository.findByName(command.getCategoryName())
                .orElseGet(() -> {
                    log.info("Catelogy '{}' not found, creating new one", command.getCategoryName());

                    Category newCategory = Category.create(
                            command.getCategoryName(),
                            "Auto-created category");

                    return categoryRepository.save(newCategory);
                });

        // 4. Create book using domain factory method
        Book book = Book.create(
                command.getTitle(),
                command.getDescription(),
                Author.of(command.getAuthorName()),
                ISBN.of(command.getIsbn()),
                category,
                command.getInitialQuantity());

        // 5. Save book to repository
        Book savedBook = bookRepository.save(book);
        log.info("Book with ID: {} created successfully", savedBook.getId());

        // 6. Publish domain event
        BookEvent event = BookEvent.bookCreated(
                savedBook.getId(),
                savedBook.getIsbn().getValue(),
                savedBook.getTitle());

        eventPublisher.publish(event);
        log.info("Published BookCreated event for book ID: {}", savedBook.getId());

        // 7. Return response DTO
        return mapToResponse(savedBook);
    }

    /**
     * Map Book entity to BookResponse DTO
     * 
     * @param command
     */
    private void validateCommand(CreateBookCommand command) {

        if (command.getIsbn() == null || command.getIsbn().isBlank()) {
            throw new IllegalArgumentException("ISBN is required");
        }

        if (command.getTitle() == null || command.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (command.getAuthorName() == null || command.getAuthorName().isBlank()) {
            throw new IllegalArgumentException("Author name is required");
        }

        if (command.getCategoryName() == null || command.getCategoryName().isBlank()) {
            throw new IllegalArgumentException("Category name is required");
        }
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
