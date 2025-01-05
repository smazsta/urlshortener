package com.example.urlshortener.service;

import com.example.urlshortener.exception.CacheFailureException;
import com.example.urlshortener.exception.DatabaseConnectionException;
import com.example.urlshortener.exception.EncodingException;
import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.utils.Base62Encoder;
import com.example.urlshortener.utils.StringSanitizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UrlShortenerService {

  @Autowired
  private UrlRepository urlRepository;

  @Autowired
  private CacheService cacheService;

  private static final String CACHE_PREFIX = "url:";

  public String shortenUrl(String longUrl) {
    try {
      log.debug("Shortening URL: {}", longUrl);
      String sanitizedUrl = StringSanitizer.sanitize(longUrl);
      long id = urlRepository.getNextId();
      String shortCode;

      do {
        shortCode = Base62Encoder.encode(id);
      } while (urlRepository.existsByShortCode(shortCode));

      UrlMapping urlMapping = new UrlMapping(shortCode, sanitizedUrl);
      urlRepository.save(urlMapping);

      log.info("Successfully shortened URL: {} -> {}", longUrl, shortCode);
      return shortCode;
    } catch (IllegalArgumentException e) {
      log.error("Encoding error while shortening URL: {}", longUrl, e);
      throw new EncodingException("Failed to encode URL: " + e.getMessage());
    } catch (Exception e) {
      log.error("Database connection error while shortening URL: {}", longUrl, e);
      throw new DatabaseConnectionException("Failed to connect to the database");
    }
  }

  public String getLongUrl(String shortCode) {
    String cacheKey = CACHE_PREFIX + shortCode;
    String longUrl = cacheService.get(cacheKey);

    if (longUrl != null) {
      log.debug("Cache hit for short code: {}", shortCode);
      return longUrl;
    }

    log.debug("Cache miss for short code: {}", shortCode);
    try {
      Optional<UrlMapping> urlMapping = urlRepository.findByShortCode(shortCode);
      if (urlMapping.isPresent()) {
        longUrl = urlMapping.get().getLongUrl();
        cacheService.put(cacheKey, longUrl);
        log.info("Retrieved long URL from database: {} -> {}", shortCode, longUrl);
        return longUrl;
      }
    } catch (Exception e) {
      log.error("Cache or database failure while retrieving long URL for short code: {}", shortCode, e);
      throw new CacheFailureException("Failed to retrieve data from cache or database");
    }

    log.warn("Long URL not found for short code: {}", shortCode);
    return null;
  }

  public boolean deleteUrl(String shortCode) {
    log.debug("Deleting URL mapping for short code: {}", shortCode);
    Optional<UrlMapping> urlMapping = urlRepository.findByShortCode(shortCode);
    if (urlMapping.isPresent()) {
      urlRepository.deleteByShortCode(shortCode);
      cacheService.invalidate(CACHE_PREFIX + shortCode);
      log.info("Successfully deleted URL mapping for short code: {}", shortCode);
      return true;
    }
    log.warn("URL mapping not found for short code: {}", shortCode);
    return false;
  }

  public boolean updateUrl(String shortCode, String newLongUrl) {
    log.debug("Updating URL mapping for short code: {}", shortCode);
    String sanitizedUrl = StringSanitizer.sanitize(newLongUrl);

    Optional<UrlMapping> urlMapping = urlRepository.findByShortCode(shortCode);
    if (urlMapping.isPresent()) {
      UrlMapping updatedMapping = new UrlMapping(shortCode, sanitizedUrl);
      urlRepository.save(updatedMapping);
      cacheService.invalidate(CACHE_PREFIX + shortCode);
      log.info("Successfully updated URL mapping for short code: {}", shortCode);
      return true;
    }
    log.warn("URL mapping not found for short code: {}", shortCode);
    return false;
  }
}