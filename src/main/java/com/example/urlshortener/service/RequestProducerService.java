package com.example.urlshortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RequestProducerService {

  @Autowired
  private UrlShortenerService urlShortenerService;

  private static final int THREAD_COUNT = 1000;
  private static final int REQUESTS_PER_THREAD = 1000;
  private static final String BASE_URL = "https://example.com/";

  private final AtomicInteger successCount = new AtomicInteger(0);
  private final AtomicInteger failureCount = new AtomicInteger(0);

  public long simulateHighTraffic() {
    long startTime = System.currentTimeMillis();

    try {
      final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
      CountDownLatch latch = new CountDownLatch(THREAD_COUNT * REQUESTS_PER_THREAD);

      for (int i = 0; i < THREAD_COUNT; i++) {
        executorService.submit(() -> {
          for (int j = 0; j < REQUESTS_PER_THREAD; j++) {
            final int requestId = j;
            CompletableFuture.runAsync(() -> {
              try {
                String longUrl = BASE_URL + Thread.currentThread().threadId() + "-" + requestId;
                String shortCode = urlShortenerService.shortenUrl(longUrl);
                String retrievedUrl = urlShortenerService.getLongUrl(shortCode);

                if (longUrl.equals(retrievedUrl)) {
                  successCount.incrementAndGet();
                } else {
                  failureCount.incrementAndGet();
                }
              } catch (Exception e) {
                failureCount.incrementAndGet();
              } finally {
                latch.countDown();
              }
            }, executorService);
          }
        });
      }

      if (!latch.await(1, TimeUnit.MINUTES)) {
        System.err.println("Timeout: Some requests did not complete in time.");
      }

      executorService.shutdown();
      try {
        if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
          System.err.println("ExecutorService did not terminate in time.");
        }
      } catch (InterruptedException e) {
        System.err.println("ExecutorService shutdown interrupted: " + e.getMessage());
      }
    } catch (InterruptedException e) {
      System.err.println("High traffic simulation interrupted: " + e.getMessage());
    }

    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;

    System.out.println("High traffic simulation completed.");
    System.out.println("Successful requests: " + successCount.get());
    System.out.println("Failed requests: " + failureCount.get());
    System.out.println("Total time taken: " + totalTime + " ms");

    return totalTime;
  }
}