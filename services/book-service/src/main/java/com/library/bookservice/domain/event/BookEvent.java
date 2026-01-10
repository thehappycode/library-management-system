package com.library.bookservice.domain.event;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Domain event representing actions performed on a Book entity
 */

@Builder
@Getter
@Setter
public class BookEvent {
    private String eventId;
    private String eventType;
    private LocalDateTime occurredOn;
    private Long bookId;
    private String isbn;
    private String title;
    private Map<String, Object> metadata;

    /**
     * Factory method to create a Book Created event
     * 
     * @param bookId
     * @param isbn
     * @param title
     * @return
     */
    public static BookEvent bookCreated(
            Long bookId,
            String isbn,
            String title) {
        return BookEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("BOOK_CREATED")
                .occurredOn(LocalDateTime.now())
                .bookId(bookId)
                .isbn(isbn)
                .title(title)
                .build();
    }

    /**
     * Factory method to create a Book Updated event
     * 
     * @param bookId
     * @param isbn
     * @param title
     * @return
     */
    public static BookEvent bookUpdated(
            Long bookId,
            String isbn,
            String title) {
        return BookEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("BOOK_UPDATED")
                .occurredOn(LocalDateTime.now())
                .bookId(bookId)
                .isbn(isbn)
                .title(title)
                .build();
    }

    /**
     * Factory method to create a Book Deleted event
     * 
     * @param bookId
     * @param isbn
     * @return
     */
    public static BookEvent bookDeleted(
            Long bookId,
            String isbn) {
        return BookEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("BOOK_DELETED")
                .occurredOn(LocalDateTime.now())
                .bookId(bookId)
                .isbn(isbn)
                .build();
    }

    /**
     * Factory method to create a Book Reserved event
     * 
     * @param bookId
     * @param isbn
     * @param title
     * @param userId
     * @return
     */
    public static BookEvent bookReserved(
            Long bookId,
            String isbn,
            String title,
            Long userId) {
        return BookEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("BOOK_RESERVED")
                .occurredOn(LocalDateTime.now())
                .bookId(bookId)
                .isbn(isbn)
                .title(title)
                .metadata(Map.of("userId", userId))
                .build();
    }

    public static BookEvent bookReleased(
            Long bookId,
            String isbn,
            String title) {
        return BookEvent.builder()
                .eventId(java.util.UUID.randomUUID().toString())
                .eventType("BOOK_RETURNED")
                .occurredOn(LocalDateTime.now())
                .bookId(bookId)
                .isbn(isbn)
                .title(title)
                .build();
    }
}
