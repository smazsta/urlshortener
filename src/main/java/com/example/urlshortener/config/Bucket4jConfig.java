package com.example.urlshortener.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.jcache.JCacheProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Configuration
@Profile("bucket4j")
@RequiredArgsConstructor
public class Bucket4jConfig {

  @Bean
  public ProxyManager<String> proxyManager() {
    CachingProvider cachingProvider = Caching.getCachingProvider();
    CacheManager cacheManager = cachingProvider.getCacheManager();

    javax.cache.configuration.Configuration<String, BucketConfiguration> config =
        new MutableConfiguration<String, BucketConfiguration>()
            .setTypes(String.class, BucketConfiguration.class);

    cacheManager.createCache("rate-limit-buckets", config);

    return new JCacheProxyManager<>(cacheManager.getCache("rate-limit-buckets"));
  }

  @Bean
  public BucketConfiguration bucketConfiguration() {
    Bandwidth limit = Bandwidth.classic(
        100,
        Refill.intervally(
            100, Duration.of(1, ChronoUnit.MINUTES)
        )
    );
    return BucketConfiguration.builder()
        .addLimit(limit)
        .build();
  }
}