package com.library.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Standardized error response for all services
 * Provides detailed information about errors
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    /**
     * Timestamp when the error occurred
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * HTTP status code
     */
    private int status;
    
    /**
     * Error type or category
     */
    private String error;
    
    /**
     * Detailed error message
     */
    private String message;
    
    /**
     * The path where the error occurred
     */
    private String path;
    
    /**
     * Unique trace ID for tracking the error
     */
    private String traceId;
    
    /**
     * List of validation errors (for validation failures)
     */
    private List<FieldError> fieldErrors;
    
    /**
     * Additional error details
     */
    private Map<String, Object> details;
    
    /**
     * Represents a field-level validation error
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        /**
         * Name of the field that failed validation
         */
        private String field;
        
        /**
         * The rejected value
         */
        private Object rejectedValue;
        
        /**
         * Error message
         */
        private String message;
        
        /**
         * Validation constraint that was violated
         */
        private String constraint;
    }
}
