package com.example.urlshortener.service;

import com.example.urlshortener.model.UrlEntity;
import com.example.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

  @Mock
  private UrlRepository urlRepository;

  @InjectMocks
  private UrlService urlService;

  @Test
  @DisplayName("Should create and save short URL")
  void shouldCreateAndSaveShortUrlTest() {
    String originalUrl = "www.google.com";
    String shortUrl = Integer.toHexString(originalUrl.hashCode());

    when(urlRepository.save(any(UrlEntity.class))).thenReturn(new UrlEntity(originalUrl, shortUrl));

    String result = urlService.createShortUrl(originalUrl);

    assertEquals(shortUrl, result);
    verify(urlRepository).save(any(UrlEntity.class));
  }

  @Test
  @DisplayName("Should return original URL")
  void shouldGetOriginalUrlTest() {
    String originalUrl = "www.google.com";
    String shortUrl = "google";

    when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(new UrlEntity(originalUrl, shortUrl)));

    Optional<String> result = urlService.getOriginalUrl(shortUrl);

    assertNotNull(result);
    assertEquals(originalUrl, result.get());
    verify(urlRepository).findByShortUrl(any(String.class));
  }

  @Test
  @DisplayName("Should return empty URL if no original URL found")
  void shouldReturnEmptyIfNoUrlFoundTest() {
    String shortUrl = "google";
    when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

    Optional<String> result = urlService.getOriginalUrl(shortUrl);

    assertTrue(result.isEmpty());
    verify(urlRepository).findByShortUrl(any(String.class));
  }
}