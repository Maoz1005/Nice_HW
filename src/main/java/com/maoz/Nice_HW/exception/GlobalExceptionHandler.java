package com.maoz.Nice_HW.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * Global exception handler that catches validation errors across all controllers.
 *
 * Motivation:
 * Previously, when DTO fields were missing or invalid, the default Spring error response
 * contained a very long message. This class produces a short and informative JSON response.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /***
     * Handles validation errors thrown when request body fails @Valid checks.
     *
     * @param ex the MethodArgumentNotValidException containing all validation errors
     * @return a structured ResponseEntity containing timestamp, HTTP status, and a map of field-specific errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Build response body
        Map<String, Object> body = new HashMap<>();

        // Include current timestamp in ISO-8601 format
        body.put("timestamp", DateTimeFormatter.ISO_INSTANT.format(Instant.now()));

        // Include HTTP status code
        body.put("status", HttpStatus.BAD_REQUEST.value());

        // Extract field-specific validation errors and map them to {field: message}
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),        // field name
                        error -> error.getDefaultMessage(), // validation message
                        (msg1, msg2) -> msg1              // in case of duplicate errors for same field, keep first
                ));

        body.put("errors", errors);

        // Return structured response with HTTP 400
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
