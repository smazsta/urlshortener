package com.example.urlshortener;

import com.example.urlshortener.utils.Base62Encoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base62EncoderTest {

  @Test
  public void testEncode() {
    assertEquals("a", Base62Encoder.encode(10));
    assertEquals("10", Base62Encoder.encode(62));
    assertEquals("3d7", Base62Encoder.encode(12345));
  }
}