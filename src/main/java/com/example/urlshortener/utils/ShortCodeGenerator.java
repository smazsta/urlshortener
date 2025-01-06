/*
 * Copyright (C) Smazsta, Inc.
 * All Rights Reserved.
 */
package com.example.urlshortener.utils;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ShortCodeGenerator {

  public String generate() {
    UUID uuid = UUID.randomUUID();
    long hash = uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits();
    return Base62Encoder.encode(Math.abs(hash));
  }
}