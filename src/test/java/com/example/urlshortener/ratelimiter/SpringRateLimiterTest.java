package com.example.urlshortener.ratelimiter;

import com.example.urlshortener.config.SpringRateLimiterConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {SpringRateLimiterConfig.class, SpringRateLimiter.class})
@ActiveProfiles("spring")
@Disabled
public class SpringRateLimiterTest {

  @Autowired
  private RequestRateLimiter rateLimiter;

  @Test
  @DisplayName("Test Spring Boot rate limiter allows requests within limit")
  public void testAllowRequest() {
    for (int i = 0; i < 100; i++) {
      assertTrue(rateLimiter.allowRequest("test-key"));
    }
  }
}