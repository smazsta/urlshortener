package com.example.urlshortener.ratelimiter;

import com.example.urlshortener.config.Bucket4jConfig;
import com.example.urlshortener.config.WebConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {
    Bucket4jConfig.class,
    Bucket4jRateLimiter.class,
    WebConfig.class
})
@ActiveProfiles({"bucket4j", "local"})
@Disabled
public class Bucket4jRateLimiterTest {

  @Autowired
  private RequestRateLimiter rateLimiter;

  @Test
  @DisplayName("Test Bucket4j rate limiter allows requests within limit")
  public void testAllowRequest() {
    for (int i = 0; i < 100; i++) {
      assertTrue(rateLimiter.allowRequest("test-key"));
    }
  }
}