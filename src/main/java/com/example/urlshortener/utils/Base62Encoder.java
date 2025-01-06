/*
 * Copyright (C) Smazsta, Inc.
 * All Rights Reserved.
 */
package com.example.urlshortener.utils;

public class Base62Encoder {
  private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static String encode(long value) {
    if (value == 0) {
      return String.valueOf(BASE62_CHARS.charAt(0));
    }

    StringBuilder shortCode = new StringBuilder();
    while (value > 0) {
      shortCode.insert(0, BASE62_CHARS.charAt((int) (value % 62)));
      value /= 62;
    }
    return shortCode.toString();
  }
}