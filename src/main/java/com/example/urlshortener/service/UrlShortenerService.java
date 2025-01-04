package com.example.urlshortener.service;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.utils.Base62Encoder;
import com.example.urlshortener.utils.StringSanitizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlShortenerService {

  @Autowired
  private UrlRepository urlRepository;

  @Autowired
  private CacheService cacheService;

  private static final String CACHE_PREFIX = "url:";

  public String shortenUrl(String longUrl) {
    String sanitizedUrl = StringSanitizer.sanitize(longUrl);

    long id = urlRepository.getNextId();
    String shortCode;

    do {
      shortCode = Base62Encoder.encode(id);
    } while (urlRepository.existsByShortCode(shortCode));

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

    return null;
  }

  public boolean deleteUrl(String shortCode) {
    Optional<UrlMapping> urlMapping = urlRepository.findByShortCode(shortCode);
    if (urlMapping.isPresent()) {
      urlRepository.deleteByShortCode(shortCode);
      cacheService.invalidate(CACHE_PREFIX + shortCode);
      return true;
    }
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
    return false;
  }
}