package com.example.urlshortener.ratelimiter;

public interface RequestRateLimiter {
  boolean allowRequest(String key);
}