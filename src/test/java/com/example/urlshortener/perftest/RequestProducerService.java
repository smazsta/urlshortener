package com.example.urlshortener.perftest;

import com.example.urlshortener.service.UrlShortenerService;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
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
    log.info("Starting high traffic simulation");

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
                String shortCode = urlShortenerService.shortenUrl(longUrl, null);
                String retrievedUrl = urlShortenerService.getLongUrl(shortCode);

                if (longUrl.equals(retrievedUrl)) {
                  successCount.incrementAndGet();
                } else {
                  failureCount.incrementAndGet();
                }
              } catch (Exception e) {
                log.error("Error during high traffic simulation: {}", e.getMessage(), e);
                failureCount.incrementAndGet();
              } finally {
                latch.countDown();
              }
            }, executorService);
          }
        });
      }

      if (!latch.await(1, TimeUnit.MINUTES)) {
        log.warn("Timeout: Some requests did not complete in time.");
      }

      executorService.shutdown();
      try {
        if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
          log.warn("ExecutorService did not terminate in time.");
        }
      } catch (InterruptedException e) {
        log.error("ExecutorService shutdown interrupted: {}", e.getMessage(), e);
      }
    } catch (InterruptedException e) {
      log.error("High traffic simulation interrupted: {}", e.getMessage(), e);
    }

    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;

    log.info("High traffic simulation completed.");
    log.info("Successful requests: {}", successCount.get());
    log.info("Failed requests: {}", failureCount.get());
    log.info("Total time taken: {} ms", totalTime);

    return totalTime;
  }
}
