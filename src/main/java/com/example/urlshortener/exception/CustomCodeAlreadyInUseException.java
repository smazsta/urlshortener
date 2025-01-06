package com.example.urlshortener.exception;

public class CustomCodeAlreadyInUseException extends RuntimeException {
  public CustomCodeAlreadyInUseException(String message) {
    super(message);
  }
}