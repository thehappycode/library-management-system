package com.library.common.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.library.common.dto.BorrowingDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Base class for borrowing-related domain events
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BorrowingEvent.BorrowingCreatedEvent.class, name = "BORROWING_CREATED"),
    @JsonSubTypes.Type(value = BorrowingEvent.BorrowingReturnedEvent.class, name = "BORROWING_RETURNED"),
    @JsonSubTypes.Type(value = BorrowingEvent.BorrowingOverdueEvent.class, name = "BORROWING_OVERDUE"),
    @JsonSubTypes.Type(value = BorrowingEvent.BorrowingRenewedEvent.class, name = "BORROWING_RENEWED"),
    @JsonSubTypes.Type(value = BorrowingEvent.FinePaidEvent.class, name = "FINE_PAID")
})
public abstract class BorrowingEvent implements DomainEvent {
    
    private String eventId;
    private LocalDateTime occurredOn;
    private String aggregateId;
    
    public BorrowingEvent(String aggregateId) {
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
     * Borrowing Created Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BorrowingCreatedEvent extends BorrowingEvent {
        private BorrowingDTO borrowing;
        
        public BorrowingCreatedEvent(String aggregateId, BorrowingDTO borrowing) {
            super(aggregateId);
            this.borrowing = borrowing;
        }
        
        @Override
        public String getEventType() {
            return "BORROWING_CREATED";
        }
    }
    
    /**
     * Borrowing Returned Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BorrowingReturnedEvent extends BorrowingEvent {
        private Long borrowingId;
        private Long bookId;
        private Long userId;
        private LocalDateTime returnDate;
        private boolean isOverdue;
        private BigDecimal fineAmount;
        
        public BorrowingReturnedEvent(String aggregateId, Long borrowingId, Long bookId, 
                                     Long userId, LocalDateTime returnDate, 
                                     boolean isOverdue, BigDecimal fineAmount) {
            super(aggregateId);
            this.borrowingId = borrowingId;
            this.bookId = bookId;
            this.userId = userId;
            this.returnDate = returnDate;
            this.isOverdue = isOverdue;
            this.fineAmount = fineAmount;
        }
        
        @Override
        public String getEventType() {
            return "BORROWING_RETURNED";
        }
    }
    
    /**
     * Borrowing Overdue Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BorrowingOverdueEvent extends BorrowingEvent {
        private Long borrowingId;
        private Long userId;
        private Long bookId;
        private int daysOverdue;
        private BigDecimal fineAmount;
        
        public BorrowingOverdueEvent(String aggregateId, Long borrowingId, Long userId, 
                                    Long bookId, int daysOverdue, BigDecimal fineAmount) {
            super(aggregateId);
            this.borrowingId = borrowingId;
            this.userId = userId;
            this.bookId = bookId;
            this.daysOverdue = daysOverdue;
            this.fineAmount = fineAmount;
        }
        
        @Override
        public String getEventType() {
            return "BORROWING_OVERDUE";
        }
    }
    
    /**
     * Borrowing Renewed Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BorrowingRenewedEvent extends BorrowingEvent {
        private Long borrowingId;
        private Long userId;
        private Long bookId;
        private LocalDateTime newDueDate;
        
        public BorrowingRenewedEvent(String aggregateId, Long borrowingId, Long userId, 
                                    Long bookId, LocalDateTime newDueDate) {
            super(aggregateId);
            this.borrowingId = borrowingId;
            this.userId = userId;
            this.bookId = bookId;
            this.newDueDate = newDueDate;
        }
        
        @Override
        public String getEventType() {
            return "BORROWING_RENEWED";
        }
    }
    
    /**
     * Fine Paid Event
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinePaidEvent extends BorrowingEvent {
        private Long borrowingId;
        private Long userId;
        private BigDecimal amount;
        private LocalDateTime paidAt;
        
        public FinePaidEvent(String aggregateId, Long borrowingId, Long userId, 
                           BigDecimal amount, LocalDateTime paidAt) {
            super(aggregateId);
            this.borrowingId = borrowingId;
            this.userId = userId;
            this.amount = amount;
            this.paidAt = paidAt;
        }
        
        @Override
        public String getEventType() {
            return "FINE_PAID";
        }
    }
}
