package com.example.urlshortener.ratelimiter;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("spring")
@RequiredArgsConstructor
public class SpringRateLimiter implements RequestRateLimiter {

  private final RedisRateLimiter rateLimiter;

  @Override
  public boolean allowRequest(String key) {
    return rateLimiter.isAllowed(key, "1").block().isAllowed();
  }
}