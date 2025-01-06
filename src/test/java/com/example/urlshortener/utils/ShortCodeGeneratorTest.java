package com.example.urlshortener.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ShortCodeGeneratorTest {

  @Autowired
  private ShortCodeGenerator shortCodeGenerator;

  @Test
  @DisplayName("Generate short code - Success")
  void testGenerateShortCode_Success() {
    String shortCode = shortCodeGenerator.generate();
    assertNotNull(shortCode);
    assertFalse(shortCode.isEmpty());
  }

  @Test
  @DisplayName("Generate multiple short codes - Ensure uniqueness")
  void testGenerateShortCode_Uniqueness() {
    String shortCode1 = shortCodeGenerator.generate();
    String shortCode2 = shortCodeGenerator.generate();
    assertNotEquals(shortCode1, shortCode2);
  }
}
