package com.library.bookservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to reserve a book
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveBookCommand {
    private Long bookId;
    private Long userId;
}