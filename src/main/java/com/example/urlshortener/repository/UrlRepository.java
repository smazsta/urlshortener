/*
 * Copyright (C) Smazsta, Inc.
 * All Rights Reserved.
 */
package com.example.urlshortener.repository;

import com.example.urlshortener.model.UrlMapping;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class UrlRepository {

  private final Map<String, UrlMapping> urlMap = new ConcurrentHashMap<>();

  public void save(UrlMapping urlMapping) {
    urlMap.put(urlMapping.getShortCode(), urlMapping);
  }

  public Optional<UrlMapping> findByShortCode(String shortCode) {
    return Optional.ofNullable(urlMap.get(shortCode));
  }

  public void deleteByShortCode(String shortCode) {
    urlMap.remove(shortCode);
  }
}