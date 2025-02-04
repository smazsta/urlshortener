package com.example.urlshortener.repository;

import com.example.urlshortener.model.UrlEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UrlRepositoryTest {

  @Autowired
  private UrlRepository urlRepository;

  @Test
  @DisplayName("Should save and find UrlEntity in repository")
  void shouldSaveAndFindEntityTest() {
    UrlEntity urlEntity = new UrlEntity("www.gooogle.com", "google");

    urlRepository.save(urlEntity);

    Optional<UrlEntity> expected = urlRepository.findById(1L);

    assertTrue(expected.isPresent());
    assertEquals(urlEntity.getId(), expected.get().getId());
    assertEquals(urlEntity.getOriginalUrl(), expected.get().getOriginalUrl());
    assertEquals(urlEntity.getShortUrl(), expected.get().getShortUrl());
    assertEquals(urlEntity.getCreatedAt(), expected.get().getCreatedAt());
  }

  @Test
  @DisplayName("Should return UrlEntity for provided short URL")
  void shouldReturnEntityByShortUrlTest() {
    String shortUrl = "google";
    UrlEntity urlEntity = new UrlEntity("www.gooogle.com", shortUrl);

    urlRepository.save(urlEntity);

    Optional<UrlEntity> expected = urlRepository.findByShortUrl(shortUrl);

    assertTrue(expected.isPresent());
    assertEquals(urlEntity.getId(), expected.get().getId());
    assertEquals(urlEntity.getOriginalUrl(), expected.get().getOriginalUrl());
    assertEquals(urlEntity.getShortUrl(), expected.get().getShortUrl());
    assertEquals(urlEntity.getCreatedAt(), expected.get().getCreatedAt());
  }
}