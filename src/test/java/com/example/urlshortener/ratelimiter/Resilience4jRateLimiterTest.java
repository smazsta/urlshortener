package com.example.urlshortener.ratelimiter;

import com.example.urlshortener.config.Resilience4jConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {Resilience4jConfig.class, Resilience4jRateLimiter.class})
@ActiveProfiles("resilience4j")
public class Resilience4jRateLimiterTest {

  @Autowired
  private RequestRateLimiter rateLimiter;

  @Test
  @DisplayName("Test Resilience4j rate limiter allows requests within limit")
  public void testAllowRequest() {
    for (int i = 0; i < 100; i++) {
      assertTrue(rateLimiter.allowRequest("test-key"));
    }

    assertFalse(rateLimiter.allowRequest("test-key"));
  }
}