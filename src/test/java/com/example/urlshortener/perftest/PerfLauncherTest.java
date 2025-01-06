/*
 * Copyright (C) Smazsta, Inc.
 * All Rights Reserved.
 */
package com.example.urlshortener.perftest;

import com.example.urlshortener.UrlShortenerApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("perf-test")
public class PerfLauncherTest {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(UrlShortenerApplication.class, args);
        RequestProducerService requestProducerService = context.getBean(RequestProducerService.class);

        long totalTime = requestProducerService.simulateHighTraffic();

        System.out.println("Total time taken for all requests: " + totalTime + " ms");

        context.close();
    }
}