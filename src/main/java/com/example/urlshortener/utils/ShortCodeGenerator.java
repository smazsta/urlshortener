package com.example.urlshortener.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShortCodeGenerator {

  public String generate() {
    UUID uuid = UUID.randomUUID();
    long hash = uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits();
    return Base62Encoder.encode(Math.abs(hash));
  }
}