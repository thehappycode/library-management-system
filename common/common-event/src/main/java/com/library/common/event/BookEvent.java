package com.library.common.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.library.common.dto.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Base class for book-related domain events
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BookEvent.BookCreatedEvent.class, name = "BOOK_CREATED"),
    @JsonSubTypes.Type(value = BookEvent.BookUpdatedEvent.class, name = "BOOK_UPDATED"),
    @JsonSubTypes.Type(value = BookEvent.BookDeletedEvent.class, name = "BOOK_DELETED"),
    @JsonSubTypes.Type(value = BookEvent.BookBorrowedEvent.class, name = "BOOK_BORROWED"),
    @JsonSubTypes.Type(value = BookEvent.BookReturnedEvent.class, name = "BOOK_RETURNED")
})
public abstract class BookEvent implements DomainEvent {
    
    private String eventId;
    private LocalDateTime occurredOn;
    private String aggregateId;
    
    public BookEvent(String aggregateId) {
        this.eventId = DomainEvent.generateEventId();
        this.occurredOn = LocalDateTime.now();
        this.aggregateId = aggregateId;
    }
    
    @Override
    public String getEventId() {
        return eventId;
    }
    
    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String getAggregateId() {
        return aggregateId;
    }
    
    /**
     * Book Created Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookCreatedEvent extends BookEvent {
        private BookDTO book;
        
        public BookCreatedEvent(String aggregateId, BookDTO book) {
            super(aggregateId);
            this.book = book;
        }
        
        @Override
        public String getEventType() {
            return "BOOK_CREATED";
        }
    }
    
    /**
     * Book Updated Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookUpdatedEvent extends BookEvent {
        private BookDTO book;
        
        public BookUpdatedEvent(String aggregateId, BookDTO book) {
            super(aggregateId);
            this.book = book;
        }
        
        @Override
        public String getEventType() {
            return "BOOK_UPDATED";
        }
    }
    
    /**
     * Book Deleted Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookDeletedEvent extends BookEvent {
        private Long bookId;
        
        public BookDeletedEvent(String aggregateId, Long bookId) {
            super(aggregateId);
            this.bookId = bookId;
        }
        
        @Override
        public String getEventType() {
            return "BOOK_DELETED";
        }
    }
    
    /**
     * Book Borrowed Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookBorrowedEvent extends BookEvent {
        private Long bookId;
        private Long userId;
        private LocalDateTime borrowDate;
        private LocalDateTime dueDate;
        
        public BookBorrowedEvent(String aggregateId, Long bookId, Long userId, 
                                LocalDateTime borrowDate, LocalDateTime dueDate) {
            super(aggregateId);
            this.bookId = bookId;
            this.userId = userId;
            this.borrowDate = borrowDate;
            this.dueDate = dueDate;
        }
        
        @Override
        public String getEventType() {
            return "BOOK_BORROWED";
        }
    }
    
    /**
     * Book Returned Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookReturnedEvent extends BookEvent {
        private Long bookId;
        private Long userId;
        private LocalDateTime returnDate;
        private boolean isOverdue;
        
        public BookReturnedEvent(String aggregateId, Long bookId, Long userId, 
                                LocalDateTime returnDate, boolean isOverdue) {
            super(aggregateId);
            this.bookId = bookId;
            this.userId = userId;
            this.returnDate = returnDate;
            this.isOverdue = isOverdue;
        }
        
        @Override
        public String getEventType() {
            return "BOOK_RETURNED";
        }
    }
}
