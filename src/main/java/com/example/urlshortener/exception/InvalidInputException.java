/*
 * Copyright (C) Smazsta, Inc.
 * All Rights Reserved.
 */
package com.example.urlshortener.exception;

public class InvalidInputException extends RuntimeException {
  public InvalidInputException(String message) {
    super(message);
  }
}