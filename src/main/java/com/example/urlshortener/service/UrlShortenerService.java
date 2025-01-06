package com.example.urlshortener.service;

import com.example.urlshortener.exception.CustomCodeAlreadyInUseException;
import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.utils.ShortCodeGenerator;
import com.example.urlshortener.utils.StringSanitizer;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UrlShortenerService {

  @Autowired
  private UrlRepository urlRepository;

  @Autowired
  private CacheService cacheService;

  @Autowired
  private ShortCodeGenerator shortCodeGenerator;

  private static final String CACHE_PREFIX = "url:";

  public String shortenUrl(String longUrl, String customCode) {
    String sanitizedUrl = StringSanitizer.sanitize(longUrl);

    if (customCode != null && !customCode.isEmpty()) {
      if (urlRepository.findByShortCode(customCode).isPresent()) {
        throw new CustomCodeAlreadyInUseException("Custom code is already in use.");
      }
    }

    String shortCode = (customCode != null && !customCode.isEmpty())
        ? customCode
        : shortCodeGenerator.generate();

    UrlMapping urlMapping = new UrlMapping(shortCode, sanitizedUrl);
    urlRepository.save(urlMapping);

    return shortCode;
  }

  public String getLongUrl(String shortCode) {
    String cacheKey = CACHE_PREFIX + shortCode;
    String longUrl = cacheService.get(cacheKey);

    if (longUrl != null) {
      return longUrl;
    }

    Optional<UrlMapping> urlMapping = urlRepository.findByShortCode(shortCode);
    if (urlMapping.isPresent()) {
      longUrl = urlMapping.get().getLongUrl();
      cacheService.put(cacheKey, longUrl);
      return longUrl;
    }

    log.warn("Long URL not found for short code: {}", shortCode);
    return null;
  }

  public boolean deleteUrl(String shortCode) {
    Optional<UrlMapping> urlMapping = urlRepository.findByShortCode(shortCode);
    if (urlMapping.isPresent()) {
      urlRepository.deleteByShortCode(shortCode);
      cacheService.invalidate(CACHE_PREFIX + shortCode);
      return true;
    }
    log.warn("URL mapping not found for short code: {}", shortCode);
    return false;
  }

  public boolean updateUrl(String shortCode, String newLongUrl) {
    String sanitizedUrl = StringSanitizer.sanitize(newLongUrl);

    Optional<UrlMapping> urlMapping = urlRepository.findByShortCode(shortCode);
    if (urlMapping.isPresent()) {
      UrlMapping updatedMapping = new UrlMapping(shortCode, sanitizedUrl);
      urlRepository.save(updatedMapping);
      cacheService.invalidate(CACHE_PREFIX + shortCode);
      return true;
    }
    log.warn("URL mapping not found for short code: {}", shortCode);
    return false;
  }
}
