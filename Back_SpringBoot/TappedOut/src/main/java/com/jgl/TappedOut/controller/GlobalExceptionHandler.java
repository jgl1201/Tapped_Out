package com.jgl.TappedOut.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handle validation exceptions
     * ? @NotNull, @NotBlank, @Email, @Size ...
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation Error", errors);
        return buildResponseEntity(apiError);
    }

    /**
     * Handle ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation Error", errors);
        return buildResponseEntity(apiError);
    }

    /**
     * Handle EntityNotFoundException
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), null);
        return buildResponseEntity(apiError);
    }

    /**
     * Handle IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        return buildResponseEntity(apiError);
    }

    /**
     * Handle IllegalStateException
     */
    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        return buildResponseEntity(apiError);
    }

    /**
     * Handle DataIntegrityViolationException
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String errorMessage = "Data Integrity Violation: " + ex.getMessage();

        if(ex.getRootCause() != null)
            errorMessage = ex.getRootCause().getMessage();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage, null);
        return buildResponseEntity(apiError);
    }

    /**
     * Hanlde not captured general exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


    /**
     * Internal class to represent the error response
     */
    public static class ApiError {
        private HttpStatus status;
        private String message;
        private Map<String, String> errors;
        private long timestamp;

        public ApiError(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public ApiError(HttpStatus status, String message, Map<String, String> errors) {
            this(status, message);
            this.errors = errors;
        }

        // Getters
        public HttpStatus getStatus() { return status; }
        public String getMessage() { return message; }
        public Map<String, String> getErrors() { return errors; }
        public long getTimestamp() { return timestamp; }
    }
}