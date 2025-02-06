package com.example.urlshortener.service;

import com.example.urlshortener.model.UrlEntity;
import com.example.urlshortener.repository.UrlRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlService {

  private final UrlRepository repository;

  public UrlService(UrlRepository repository) {
    this.repository = repository;
  }

  @CachePut(value = "urlCache", key = "#result")
  public String createShortUrl(String originalUrl) {
    String shortUrl = generateShortUrl(originalUrl);
    UrlEntity urlEntity = new UrlEntity(originalUrl, shortUrl);
    repository.save(urlEntity);

    return shortUrl;
  }

  @Cacheable(value = "urlCache", key = "#p0")
  public Optional<String> getOriginalUrl(String shortUrl) {
    return repository.findByShortUrl(shortUrl).map(UrlEntity::getOriginalUrl);
  }

  private String generateShortUrl(String originalUrl) {
    return Integer.toHexString(originalUrl.hashCode());
  }
}
