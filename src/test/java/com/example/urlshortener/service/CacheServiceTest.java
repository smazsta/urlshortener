package com.example.urlshortener.service;

import com.example.urlshortener.config.CacheConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CacheServiceTest {

  @Mock
  private CacheConfig cacheConfig;

  private CacheService cacheService;

  @BeforeEach
  void setUp() {
    lenient().when(cacheConfig.getMaxSize()).thenReturn(1000);
    cacheService = new CacheService(cacheConfig);
  }

  @Test
  @DisplayName("put and get - when key exists - returns value")
  void putAndGet_whenKeyExists_returnsValue() {
    String key = "key1";
    String value = "value1";

    cacheService.put(key, value);
    String result = cacheService.get(key);

    assertThat(result).isEqualTo(value);
  }

  @Test
  @DisplayName("put and get - when cache exceeds max size - evicts oldest entry")
  void putAndGet_whenCacheExceedsMaxSize_evictsOldestEntry() {
    for (int i = 0; i < 1001; i++) {
      cacheService.put("key" + i, "value" + i);
    }

    String firstEntry = cacheService.get("key0");
    String lastEntry = cacheService.get("key1000");

    assertThat(firstEntry).isNull();
    assertThat(lastEntry).isEqualTo("value1000");
  }

  @Test
  @DisplayName("invalidate - when key exists - removes key from cache")
  void invalidate_whenKeyExists_removesKeyFromCache() {
    String key = "key1";
    String value = "value1";
    cacheService.put(key, value);

    cacheService.invalidate(key);
    String result = cacheService.get(key);

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("put and get - with custom cache size - evicts entries correctly")
  void putAndGet_withCustomCacheSize_evictsEntriesCorrectly() {
    when(cacheConfig.getMaxSize()).thenReturn(500);
    CacheService customCacheService = new CacheService(cacheConfig);

    for (int i = 0; i < 501; i++) {
      customCacheService.put("key" + i, "value" + i);
    }

    String firstEntry = customCacheService.get("key0");
    String lastEntry = customCacheService.get("key500");

    assertThat(firstEntry).isNull();
    assertThat(lastEntry).isEqualTo("value500");
  }
}