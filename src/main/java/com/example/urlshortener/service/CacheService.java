package com.example.urlshortener.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class CacheService {

  private static final int MAX_CACHE_SIZE = 1000;
  private final Map<String, String> cache;
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  public CacheService() {
    this.cache = new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
        return size() > MAX_CACHE_SIZE;
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