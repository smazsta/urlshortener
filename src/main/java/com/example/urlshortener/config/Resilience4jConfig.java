package com.example.urlshortener.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;

@Configuration
@Profile("resilience4j")
public class Resilience4jConfig {

  @Bean
  public RateLimiterRegistry rateLimiterRegistry() {
    return RateLimiterRegistry.ofDefaults();
  }

  @Bean
  public RateLimiter globalRateLimiter(RateLimiterRegistry rateLimiterRegistry) {
    RateLimiterConfig config = RateLimiterConfig.custom()
        .limitForPeriod(100)
        .limitRefreshPeriod(Duration.ofMinutes(1))
        .timeoutDuration(Duration.ZERO)
        .build();

    return rateLimiterRegistry.rateLimiter("globalRateLimiter", config);
  }
}