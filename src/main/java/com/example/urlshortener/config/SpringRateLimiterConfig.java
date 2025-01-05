package com.example.urlshortener.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Mono;

@Configuration
@Profile("spring")
public class SpringRateLimiterConfig {

  @Bean
  public RedisRateLimiter redisRateLimiter() {
    return new RedisRateLimiter(100, 100, 1);
  }

  @Bean
  public KeyResolver keyResolver() {
    return exchange -> Mono.just(
        exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
    );
  }
}