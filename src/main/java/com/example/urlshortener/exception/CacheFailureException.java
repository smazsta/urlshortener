package com.example.urlshortener.exception;

public class CacheFailureException extends RuntimeException {
  public CacheFailureException(String message) {
    super(message);
  }
}