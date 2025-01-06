/*
 * Copyright (C) Smazsta, Inc.
 * All Rights Reserved.
 */
package com.example.urlshortener.utils;

public class StringSanitizer {

  public static String sanitize(String input) {
    if (input == null) {
      return null;
    }

    StringBuilder sanitized = new StringBuilder();
    for (char c : input.toCharArray()) {
      switch (c) {
        case '<':
          sanitized.append("&lt;");
          break;
        case '>':
          sanitized.append("&gt;");
          break;
        case '&':
          sanitized.append("&amp;");
          break;
        case '"':
          sanitized.append("&quot;");
          break;
        case '\'':
          sanitized.append("&#39;");
          break;
        default:
          sanitized.append(c);
      }
    }
    return sanitized.toString();
  }
}