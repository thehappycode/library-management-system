package com.library.bookservice.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.bookservice.application.dto.ReserveBookCommand;
import com.library.bookservice.application.port.output.BookRepositoryPort;
import com.library.bookservice.application.port.output.EventPublisherPort;
import com.library.bookservice.domain.event.BookEvent;
import com.library.bookservice.domain.model.Book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Use Case: Reserve a book for borrowing
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReserveBookUseCase {

    private final BookRepositoryPort bookRepository;
    private final EventPublisherPort eventPublisher;

    @Transactional
    public void execute(ReserveBookCommand command) {
        log.info("Reserving book {} for user {}", command.getBookId(), command.getUserId());

        // 1. Find book
        Book book = bookRepository.findById(command.getBookId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Book not found with ID: " + command.getBookId()));

        // 2. Reserve book (domain logic handles validation)
        try {
            book.reserve();
        } catch (IllegalStateException e) {
            log.error("Failed to reserve book: {}", e.getMessage());
            throw new IllegalArgumentException("Cannot reserve book:  " + e.getMessage());
        }

        // 3. Save updated book
        bookRepository.save(book);
        log.info("Book reserved successfully: {}", command.getBookId());

        // 4. Publish event
        BookEvent event = BookEvent.bookReserved(
                book.getId(),
                book.getIsbn().getValue(),
                book.getTitle(),
                command.getUserId());
        eventPublisher.publishAsync(event);
        log.info("Published BookReserved event for book {} and user {}",
                command.getBookId(), command.getUserId());
    }
}