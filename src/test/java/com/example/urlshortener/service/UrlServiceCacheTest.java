package com.example.urlshortener.service;

import com.example.urlshortener.model.UrlEntity;
import com.example.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UrlServiceCacheTest {

  @MockitoBean
  private UrlRepository urlRepository;

  @Autowired
  private UrlService urlService;

  @Autowired
  private CacheManager cacheManager;

  private Cache urlCache;

  @BeforeEach
  void setUp() {
    urlCache = cacheManager.getCache("urlCache");
    assertNotNull(urlCache, "Cache 'urlCache' should not be null");
    urlCache.clear();
  }

  @Test
  @DisplayName("Should cache the result of getOriginalUrl")
  void shouldCacheGetOriginalUrlTest() {
    String originalUrl = "www.google.com";
    String shortUrl = "d61ac103";

    when(urlRepository.findByShortUrl(shortUrl))
        .thenReturn(Optional.of(new UrlEntity(originalUrl, shortUrl)));

    Optional<String> firstCall = urlService.getOriginalUrl(shortUrl);
    assertTrue(firstCall.isPresent());
    assertEquals(originalUrl, firstCall.get());

    Optional<String> secondCall = urlService.getOriginalUrl(shortUrl);
    assertTrue(secondCall.isPresent());
    assertEquals(originalUrl, secondCall.get());

    verify(urlRepository, times(1)).findByShortUrl(shortUrl);

    assertNotNull(urlCache.get(shortUrl));
    assertEquals(originalUrl, urlCache.get(shortUrl).get());
  }

  @Test
  @DisplayName("Should update the cache with the result of createShortUrl")
  void shouldCachePutCreateShortUrlTest() {
    String originalUrl = "www.google.com";
    String shortUrl = "d61ac103";

    when(urlRepository.save(any(UrlEntity.class)))
        .thenReturn(new UrlEntity(originalUrl, shortUrl));

    String createdShortUrl = urlService.createShortUrl(originalUrl);
    assertEquals(shortUrl, createdShortUrl);

    verify(urlRepository, times(1)).save(any(UrlEntity.class));

    assertNotNull(urlCache.get(shortUrl));
    assertEquals(shortUrl, urlCache.get(shortUrl).get());
  }
}