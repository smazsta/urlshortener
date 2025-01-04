package com.example.urlshortener.repository;

import com.example.urlshortener.model.UrlMapping;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UrlRepository {

  private final Map<String, UrlMapping> urlMap = new ConcurrentHashMap<>();
  private final AtomicLong idCounter = new AtomicLong(1);

  public UrlMapping save(UrlMapping urlMapping) {
    urlMap.put(urlMapping.getShortCode(), urlMapping);
    return urlMapping;
  }

  public Optional<UrlMapping> findByShortCode(String shortCode) {
    return Optional.ofNullable(urlMap.get(shortCode));
  }

  public boolean existsByShortCode(String shortCode) {
    return urlMap.containsKey(shortCode);
  }

  public void deleteByShortCode(String shortCode) {
    urlMap.remove(shortCode);
  }

  public long getNextId() {
    return idCounter.getAndIncrement();
  }
}