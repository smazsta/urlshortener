package com.example.urlshortener.ratelimiter;

import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("resilience4j")
@RequiredArgsConstructor
public class Resilience4jRateLimiter implements RequestRateLimiter {

  private final RateLimiter rateLimiter;

  @Override
  public boolean allowRequest(String key) {
    boolean allowed = rateLimiter.acquirePermission();
    log.info("Request from key: {}, Allowed: {}", key, allowed);
    return allowed;
  }
}