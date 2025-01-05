package com.example.urlshortener.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class RequestRateLimiterTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Test rate limiter allows 100 requests per minute")
  public void testRateLimiterAllows100Requests() throws Exception {
    for (int i = 0; i < 100; i++) {
      mockMvc.perform(post("/shorten")
              .contentType("application/json")
              .content("{\"url\": \"https://example.com\"}"))
          .andExpect(status().isOk());
    }
  }

  @Test
  @DisplayName("Test rate limiter blocks the 101st request")
  public void testRateLimiterBlocks101stRequest() throws Exception {
    for (int i = 0; i < 100; i++) {
      mockMvc.perform(post("/shorten")
              .contentType("application/json")
              .content("{\"url\": \"https://example.com\"}"))
          .andExpect(status().isOk());
    }

    mockMvc.perform(post("/shorten")
            .contentType("application/json")
            .content("{\"url\": \"https://example.com\"}"))
        .andExpect(status().isTooManyRequests())
        .andExpect(header().exists("X-Rate-Limit-Retry-After-Seconds"));
  }
}