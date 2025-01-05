package com.example.urlshortener.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CacheServiceTest {

  @Test
  @DisplayName("Cache Put and Get - Success")
  void testCachePutAndGet() {
    CacheService cacheService = new CacheService();
    String key = "key1";
    String value = "value1";

    cacheService.put(key, value);
    String result = cacheService.get(key);

    assertEquals(value, result);
  }

  @Test
  @DisplayName("Cache Eviction - Success")
  void testCacheEviction() {
    CacheService cacheService = new CacheService();
    for (int i = 0; i < 1001; i++) {
      cacheService.put("key" + i, "value" + i);
    }

    String firstEntry = cacheService.get("key0");
    String lastEntry = cacheService.get("key1000");

    assertNull(firstEntry);
    assertEquals("value1000", lastEntry);
  }

  @Test
  @DisplayName("Cache Invalidation - Success")
  void testCacheInvalidation() {
    CacheService cacheService = new CacheService();
    String key = "key1";
    String value = "value1";
    cacheService.put(key, value);

    cacheService.invalidate(key);
    String result = cacheService.get(key);

    assertNull(result);
  }
}