package com.example.urlshortener.utils;

public class Base62Encoder {
  private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static String encode(long id) {
    StringBuilder shortCode = new StringBuilder();
    while (id > 0) {
      shortCode.insert(0, BASE62_CHARS.charAt((int) (id % 62)));
      id /= 62;
    }
    return shortCode.toString();
  }
}
