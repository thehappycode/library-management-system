package com.library.bookservice.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.bookservice.application.port.output.BookRepositoryPort;
import com.library.bookservice.application.port.output.EventPublisherPort;
import com.library.bookservice.domain.event.BookEvent;
import com.library.bookservice.domain.model.Book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Use Case: Delete a book
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteBookUseCase {

    private final BookRepositoryPort bookRepository;
    private final EventPublisherPort eventPublisher;

    @Transactional
    public void execute(Long bookId) {
        log.info("Deleting book: {}", bookId);

        // 1. Find book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        // 2. Check if can be deleted (domain logic)
        if (!book.canBeDeleted()) {
            throw new IllegalStateException(
                    "Cannot delete book.  " + book.getInventory().getBorrowedQuantity() +
                            " copies are still borrowed");
        }

        // 3. Store info for event
        String isbn = book.getIsbn().getValue();

        // 4. Delete book
        bookRepository.delete(book);
        log.info("Book deleted successfully: {}", bookId);

        // 5. Publish event
        BookEvent event = BookEvent.bookDeleted(bookId, isbn);
        eventPublisher.publishAsync(event);
    }
}