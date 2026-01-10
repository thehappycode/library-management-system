package com.library.bookservice.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.bookservice.application.dto.BookResponse;
import com.library.bookservice.application.dto.UpdateBookCommand;
import com.library.bookservice.application.port.output.BookRepositoryPort;
import com.library.bookservice.application.port.output.CategoryRepositoryPort;
import com.library.bookservice.application.port.output.EventPublisherPort;
import com.library.bookservice.domain.event.BookEvent;
import com.library.bookservice.domain.model.Book;
import com.library.bookservice.domain.model.Category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Use Case: Update book information
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateBookUseCase {

    private final BookRepositoryPort bookRepository;
    private final CategoryRepositoryPort categoryRepository;
    private final EventPublisherPort eventPublisher;

    @Transactional
    public BookResponse execute(Long bookId, UpdateBookCommand command) {
        log.info("Updating book:  {}", bookId);

        // 1. Find book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        // 2. Update basic info
        book.updateInfo(
                command.getTitle(),
                command.getDescription(),
                command.getAuthorName());

        // 3. Update category if changed
        if (command.getCategoryName() != null && !command.getCategoryName().isBlank()) {
            Category newCategory = categoryRepository.findByName(command.getCategoryName())
                    .orElseGet(() -> {
                        log.info("Category '{}' not found, creating new one", command.getCategoryName());
                        Category category = Category.create(command.getCategoryName(), null);
                        return categoryRepository.save(category);
                    });

            book.changeCategory(newCategory);
        }

        // 4. Save updated book
        Book updatedBook = bookRepository.save(book);
        log.info("Book updated successfully:  {}", bookId);

        // 5. Publish event
        BookEvent event = BookEvent.bookUpdated(
                updatedBook.getId(),
                updatedBook.getIsbn().getValue(),
                updatedBook.getTitle());
        eventPublisher.publishAsync(event);

        // 6. Return response
        return mapToResponse(updatedBook);
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