package com.example.urlshortener.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage())
    );
    log.warn("Validation error: {}", errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(InvalidInputException.class)
  public ResponseEntity<Map<String, String>> handleInvalidInputException(InvalidInputException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("message", ex.getMessage());
    log.warn("Invalid input error: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(CacheFailureException.class)
  public ResponseEntity<Map<String, String>> handleCacheFailureException(CacheFailureException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("message", "Cache failure: " + ex.getMessage());
    log.error("Cache failure: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(DatabaseConnectionException.class)
  public ResponseEntity<Map<String, String>> handleDatabaseConnectionException(DatabaseConnectionException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("message", "Database connection error: " + ex.getMessage());
    log.error("Database connection error: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
  }

  @ExceptionHandler(EncodingException.class)
  public ResponseEntity<Map<String, String>> handleEncodingException(EncodingException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("message", "Encoding error: " + ex.getMessage());
    log.error("Encoding error: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put("message", "An unexpected error occurred. Please try again later.");
    log.error("Unexpected error: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}