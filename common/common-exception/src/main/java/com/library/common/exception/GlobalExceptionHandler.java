package com.library.common.exception;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.library.common.dto.ErrorResponse;

import jakarta.validation.ConstraintViolationException;

/**
 * Base global exception handler for all services
 * Services should extend this class and add service-specific exception handlers
 */
public abstract class GlobalExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        /**
         * Handle ResourceNotFoundException
         */
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
                        ResourceNotFoundException ex, WebRequest request) {
                String traceId = generateTraceId();
                logger.error("Resource not found - TraceId: {}, Message: {}", traceId, ex.getMessage());

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.NOT_FOUND.value())
                                .error("Resource Not Found")
                                .message(ex.getMessage())
                                .path(getPath(request))
                                .traceId(traceId)
                                .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        /**
         * Handle BusinessException
         */
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ErrorResponse> handleBusinessException(
                        BusinessException ex, WebRequest request) {
                String traceId = generateTraceId();
                logger.error("Business exception - TraceId: {}, Message: {}", traceId, ex.getMessage());

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("Business Rule Violation")
                                .message(ex.getMessage())
                                .path(getPath(request))
                                .traceId(traceId)
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        /**
         * Handle ValidationException
         */
        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        ValidationException ex, WebRequest request) {
                String traceId = generateTraceId();
                logger.error("Validation exception - TraceId: {}, Message: {}", traceId, ex.getMessage());

                List<ErrorResponse.FieldError> fieldErrors = ex.getErrors().entrySet().stream()
                                .map(entry -> ErrorResponse.FieldError.builder()
                                                .field(entry.getKey())
                                                .message(entry.getValue())
                                                .build())
                                .collect(Collectors.toList());

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("Validation Failed")
                                .message(ex.getMessage())
                                .path(getPath(request))
                                .traceId(traceId)
                                .fieldErrors(fieldErrors)
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        /**
         * Handle UnauthorizedException
         */
        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<ErrorResponse> handleUnauthorizedException(
                        UnauthorizedException ex, WebRequest request) {
                String traceId = generateTraceId();
                logger.error("Unauthorized exception - TraceId: {}, Message: {}", traceId, ex.getMessage());

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .error("Unauthorized")
                                .message(ex.getMessage())
                                .path(getPath(request))
                                .traceId(traceId)
                                .build();

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        /**
         * Handle MethodArgumentNotValidException (Bean Validation)
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
                        MethodArgumentNotValidException ex, WebRequest request) {
                String traceId = generateTraceId();
                logger.error("Method argument validation failed - TraceId: {}", traceId);

                List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                                .map(error -> ErrorResponse.FieldError.builder()
                                                .field(error.getField())
                                                .rejectedValue(error.getRejectedValue())
                                                .message(error.getDefaultMessage())
                                                .build())
                                .collect(Collectors.toList());

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("Validation Failed")
                                .message("Input validation failed")
                                .path(getPath(request))
                                .traceId(traceId)
                                .fieldErrors(fieldErrors)
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        /**
         * Handle ConstraintViolationException
         */
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraintViolation(
                        ConstraintViolationException ex, WebRequest request) {
                String traceId = generateTraceId();
                logger.error("Constraint violation - TraceId: {}, Message: {}", traceId, ex.getMessage());

                List<ErrorResponse.FieldError> fieldErrors = ex.getConstraintViolations().stream()
                                .map(violation -> ErrorResponse.FieldError.builder()
                                                .field(violation.getPropertyPath().toString())
                                                .rejectedValue(violation.getInvalidValue())
                                                .message(violation.getMessage())
                                                .build())
                                .collect(Collectors.toList());

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("Constraint Violation")
                                .message("Validation constraint violated")
                                .path(getPath(request))
                                .traceId(traceId)
                                .fieldErrors(fieldErrors)
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        /**
         * Handle all other exceptions
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGlobalException(
                        Exception ex, WebRequest request) {
                String traceId = generateTraceId();
                logger.error("Unexpected error - TraceId: {}, Message: {}", traceId, ex.getMessage(), ex);

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .error("Internal Server Error")
                                .message("An unexpected error occurred. Please contact support with trace ID: "
                                                + traceId)
                                .path(getPath(request))
                                .traceId(traceId)
                                .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        /**
         * Generate a unique trace ID for tracking errors
         */
        protected String generateTraceId() {
                return UUID.randomUUID().toString();
        }

        /**
         * Extract the request path from WebRequest
         */
        protected String getPath(WebRequest request) {
                return request.getDescription(false).replace("uri=", "");
        }
}
