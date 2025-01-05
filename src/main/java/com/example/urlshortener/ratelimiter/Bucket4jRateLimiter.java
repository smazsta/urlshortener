package com.example.urlshortener.ratelimiter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("bucket4j")
@RequiredArgsConstructor
public class Bucket4jRateLimiter implements RequestRateLimiter {

  private final ProxyManager<String> proxyManager;
  private final BucketConfiguration bucketConfiguration;

  @Override
  public boolean allowRequest(String key) {
    Bucket bucket = proxyManager.builder().build(key, bucketConfiguration);
    return bucket.tryConsume(1);
  }
}