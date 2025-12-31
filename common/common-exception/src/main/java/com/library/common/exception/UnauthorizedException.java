package com.library.common.exception;

import lombok.Getter;

/**
 * Exception thrown when authentication or authorization fails
 */
@Getter
public class UnauthorizedException extends RuntimeException {
    
    private final String reason;
    
    public UnauthorizedException(String message) {
        super(message);
        this.reason = null;
    }
    
    public UnauthorizedException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
