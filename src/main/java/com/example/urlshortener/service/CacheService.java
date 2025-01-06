package com.example.urlshortener.service;

import com.example.urlshortener.config.CacheConfig;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

  private final Map<String, String> cache;
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final int maxCacheSize;

  public CacheService(CacheConfig cacheConfig) {
    this.maxCacheSize = cacheConfig.getMaxSize();
    System.out.println("Cache size: " + maxCacheSize);
    this.cache = new LinkedHashMap<>(maxCacheSize, 0.75f, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
        return size() > maxCacheSize;
      }
    };
  }

  public void put(String key, String value) {
    lock.writeLock().lock();
    try {
      cache.put(key, value);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public String get(String key) {
    lock.readLock().lock();
    try {
      return cache.get(key);
    } finally {
      lock.readLock().unlock();
    }
  }

  public void invalidate(String key) {
    lock.writeLock().lock();
    try {
      cache.remove(key);
    } finally {
      lock.writeLock().unlock();
    }
  }
}
