package com.library.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Borrowing entity
 * Used for transferring borrowing transaction data between services
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BorrowingDTO {
    
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Book ID is required")
    private Long bookId;
    
    private String username;
    
    private String bookTitle;
    
    private String bookIsbn;
    
    @NotNull(message = "Borrow date is required")
    private LocalDate borrowDate;
    
    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
    
    private LocalDate returnDate;
    
    private BorrowingStatus status;
    
    private Integer daysOverdue;
    
    private BigDecimal fineAmount;
    
    private Boolean finePaid;
    
    private String notes;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    /**
     * Borrowing transaction status
     */
    public enum BorrowingStatus {
        ACTIVE,
        RETURNED,
        OVERDUE,
        LOST,
        RENEWED,
        CANCELLED
    }
    
    /**
     * Calculate if the borrowing is overdue
     */
    public boolean isOverdue() {
        return status == BorrowingStatus.ACTIVE && 
               LocalDate.now().isAfter(dueDate);
    }
    
    /**
     * Calculate days overdue
     */
    public int calculateDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return (int) java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
}
