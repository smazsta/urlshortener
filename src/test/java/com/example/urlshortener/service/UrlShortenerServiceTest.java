package com.example.urlshortener.service;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.utils.StringSanitizer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlShortenerServiceTest {

  @Mock
  private UrlRepository urlRepository;

  @Mock
  private CacheService cacheService;

  @InjectMocks
  private UrlShortenerService urlShortenerService;

  @Nested
  @DisplayName("shortenUrl")
  class ShortenUrlTests {
    @Test
    @DisplayName("Success")
    void testShortenUrl_Success() {
      String longUrl = "https://example.com";
      when(urlRepository.getNextId()).thenReturn(1L);
      when(urlRepository.existsByShortCode(anyString())).thenReturn(false);

      String shortCode = urlShortenerService.shortenUrl(longUrl);

      assertNotNull(shortCode);
      verify(urlRepository, times(1)).save(any(UrlMapping.class));
    }

    @Test
    @DisplayName("Sanitizes Input URL")
    void testShortenUrl_SanitizesInput() {
      String longUrl = "https://example.com/<script>alert('XSS')</script>";
      String sanitizedUrl = StringSanitizer.sanitize(longUrl);
      when(urlRepository.getNextId()).thenReturn(1L);
      when(urlRepository.existsByShortCode(anyString())).thenReturn(false);

      String shortCode = urlShortenerService.shortenUrl(longUrl);

      assertNotNull(shortCode);
      verify(urlRepository, times(1)).save(argThat(urlMapping ->
          urlMapping.getLongUrl().equals(sanitizedUrl)
      ));
    }
  }

  @Nested
  @DisplayName("getLongUrl")
  class GetLongUrlTests {
    @Test
    @DisplayName("Found in Cache")
    void testGetLongUrl_FoundInCache() {
      String shortCode = "abc123";
      String longUrl = "https://example.com";
      when(cacheService.get("url:" + shortCode)).thenReturn(longUrl);

      String result = urlShortenerService.getLongUrl(shortCode);

      assertEquals(longUrl, result);
      verify(cacheService, times(1)).get("url:" + shortCode);
      verify(urlRepository, never()).findByShortCode(shortCode);
    }

    @Test
    @DisplayName("Found in Repository")
    void testGetLongUrl_FoundInRepository() {
      String shortCode = "abc123";
      String longUrl = "https://example.com";
      when(cacheService.get("url:" + shortCode)).thenReturn(null);
      when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(new UrlMapping(shortCode, longUrl)));

      String result = urlShortenerService.getLongUrl(shortCode);

      assertEquals(longUrl, result);
      verify(cacheService, times(1)).get("url:" + shortCode);
      verify(urlRepository, times(1)).findByShortCode(shortCode);
      verify(cacheService, times(1)).put("url:" + shortCode, longUrl);
    }

    @Test
    @DisplayName("Not Found")
    void testGetLongUrl_NotFound() {
      String shortCode = "abc123";
      when(cacheService.get("url:" + shortCode)).thenReturn(null);
      when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());

      String result = urlShortenerService.getLongUrl(shortCode);

      assertNull(result);
      verify(cacheService, times(1)).get("url:" + shortCode);
      verify(urlRepository, times(1)).findByShortCode(shortCode);
      verify(cacheService, never()).put(anyString(), anyString());
    }
  }

  @Nested
  @DisplayName("deleteUrl")
  class DeleteUrlTests {
    @Test
    @DisplayName("Success")
    void testDeleteUrl_Success() {
      String shortCode = "abc123";
      when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(new UrlMapping(shortCode, "https://example.com")));

      boolean result = urlShortenerService.deleteUrl(shortCode);

      assertTrue(result);
      verify(urlRepository, times(1)).deleteByShortCode(shortCode);
      verify(cacheService, times(1)).invalidate("url:" + shortCode);
    }

    @Test
    @DisplayName("Not Found")
    void testDeleteUrl_NotFound() {
      String shortCode = "abc123";
      when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());

      boolean result = urlShortenerService.deleteUrl(shortCode);

      assertFalse(result);
      verify(urlRepository, never()).deleteByShortCode(shortCode);
      verify(cacheService, never()).invalidate("url:" + shortCode);
    }
  }

  @Nested
  @DisplayName("updateUrl")
  class UpdateUrlTests {
    @Test
    @DisplayName("Success")
    void testUpdateUrl_Success() {
      String shortCode = "abc123";
      String newLongUrl = "https://new-example.com";
      when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.of(new UrlMapping(shortCode, "https://example.com")));

      boolean result = urlShortenerService.updateUrl(shortCode, newLongUrl);

      assertTrue(result);
      verify(urlRepository, times(1)).save(new UrlMapping(shortCode, newLongUrl));
      verify(cacheService, times(1)).invalidate("url:" + shortCode);
    }

    @Test
    @DisplayName("Not Found")
    void testUpdateUrl_NotFound() {
      String shortCode = "abc123";
      String newLongUrl = "https://new-example.com";
      when(urlRepository.findByShortCode(shortCode)).thenReturn(Optional.empty());

      boolean result = urlShortenerService.updateUrl(shortCode, newLongUrl);

      assertFalse(result);
      verify(urlRepository, never()).save(any());
      verify(cacheService, never()).invalidate("url:" + shortCode);
    }
  }
}