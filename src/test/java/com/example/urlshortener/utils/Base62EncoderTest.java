package com.example.urlshortener.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base62EncoderTest {

  @Test
  @DisplayName("Encode - Success")
  void testEncode_Success() {
    assertEquals("a", Base62Encoder.encode(10));
    assertEquals("10", Base62Encoder.encode(62));
    assertEquals("3d7", Base62Encoder.encode(12345));
  }

  @Test
  @DisplayName("Encode - Zero")
  void testEncode_Zero() {
    assertEquals("0", Base62Encoder.encode(0));
  }
}