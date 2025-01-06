/*
 * Copyright (C) Smazsta, Inc.
 * All Rights Reserved.
 */
package com.example.urlshortener.exception;

public class DatabaseConnectionException extends RuntimeException {
  public DatabaseConnectionException(String message) {
    super(message);
  }
}