package com.example.urlshortener.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StringSanitizerTest {

  @Test
  @DisplayName("Sanitize Input - Null Input")
  void testSanitize_NullInput() {
    assertNull(StringSanitizer.sanitize(null));
  }

  @Test
  @DisplayName("Sanitize Input - No Special Characters")
  void testSanitize_NoSpecialCharacters() {
    assertEquals("Hello, World!", StringSanitizer.sanitize("Hello, World!"));
  }

  @Test
  @DisplayName("Sanitize Input - With Special Characters")
  void testSanitize_WithSpecialCharacters() {
    String input = "<script>alert('XSS')</script>";

    assertEquals("&lt;script&gt;alert(&#39;XSS&#39;)&lt;/script&gt;",
        StringSanitizer.sanitize(input));
  }

  @Test
  @DisplayName("Sanitize Input - Mixed Content")
  void testSanitize_MixedContent() {
    String input = "Click <a href='https://example.com'>here</a>!";

    assertEquals("Click &lt;a href=&#39;https://example.com&#39;&gt;here&lt;/a&gt;!",
        StringSanitizer.sanitize(input));
  }
}