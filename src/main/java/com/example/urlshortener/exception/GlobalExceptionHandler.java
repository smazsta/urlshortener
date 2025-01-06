package com.example.urlshortener.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

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

  @ExceptionHandler(CustomCodeAlreadyInUseException.class)
  public ResponseEntity<Map<String, String>> handleCustomCodeAlreadyInUseException(CustomCodeAlreadyInUseException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("message", ex.getMessage());
    log.warn("Custom code already in use: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }
}