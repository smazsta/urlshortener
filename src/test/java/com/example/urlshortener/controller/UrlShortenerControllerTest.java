/*
 * Copyright (C) Smazsta, Inc.
 * All Rights Reserved.
 */
package com.example.urlshortener.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.urlshortener.config.TestSecurityConfig;
import com.example.urlshortener.dto.UrlRequest;
import com.example.urlshortener.exception.CacheFailureException;
import com.example.urlshortener.exception.DatabaseConnectionException;
import com.example.urlshortener.exception.EncodingException;
import com.example.urlshortener.service.UrlShortenerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UrlShortenerController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UrlShortenerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UrlShortenerService urlShortenerService;

  @Nested
  @DisplayName("POST /shorten")
  class ShortenUrlTests {
    @Test
    @DisplayName("Success")
    void testShortenUrl_Success() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl("https://example.com");

      when(urlShortenerService.shortenUrl(request.getUrl())).thenReturn("abc123");

      mockMvc.perform(post("/api/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(content().string("abc123"));
    }

    @Test
    @DisplayName("Invalid URL Format")
    void testShortenUrl_InvalidUrlFormat() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl("htp://invalid-url");

      mockMvc.perform(post("/api/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.url").value("Invalid URL format"));
    }

    @Test
    @DisplayName("URL Too Long")
    void testShortenUrl_UrlTooLong() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl("https://example.com/" + "a".repeat(3000));

      mockMvc.perform(post("/api/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.url").value("URL must be less than 2048 characters"));
    }

    @Test
    @DisplayName("Null URL")
    void testShortenUrl_NullUrl() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl(null); // Null URL

      mockMvc.perform(post("/api/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.url").value("URL cannot be null"));
    }

    @Test
    @DisplayName("Blank URL")
    void testShortenUrl_BlankUrl() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl(""); // Blank URL

      mockMvc.perform(post("/api/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.url").value("URL cannot be blank"));
    }

    @Test
    @DisplayName("Database Connection Issue - Returns 503 Service Unavailable")
    void testShortenUrl_DatabaseConnectionIssue() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl("https://example.com");

      when(urlShortenerService.shortenUrl(request.getUrl()))
          .thenThrow(new DatabaseConnectionException("Database unavailable"));

      mockMvc.perform(post("/api/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isServiceUnavailable())
          .andExpect(jsonPath("$.message").value("Database connection error: Database unavailable"));
    }

    @Test
    @DisplayName("Encoding Error - Returns 500 Internal Server Error")
    void testShortenUrl_EncodingError() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl("https://example.com");

      when(urlShortenerService.shortenUrl(request.getUrl()))
          .thenThrow(new EncodingException("Invalid encoding"));

      mockMvc.perform(post("/api/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.message").value("Encoding error: Invalid encoding"));
    }
  }

  @Nested
  @DisplayName("PUT /{shortCode}")
  class UpdateUrlTests {
    @Test
    @DisplayName("Invalid URL Format")
    void testShortenUrl_InvalidUrlFormat() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl("htp://invalid-url");

      mockMvc.perform(post("/api/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.url").value("Invalid URL format"));
    }

    @Test
    @DisplayName("URL Too Long")
    void testShortenUrl_UrlTooLong() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl("https://example.com/" + "a".repeat(3000));

      mockMvc.perform(post("/api/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.url").value("URL must be less than 2048 characters"));
    }

    @Test
    @DisplayName("Null URL")
    void testUpdateUrl_NullUrl() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl(null); // Null URL

      mockMvc.perform(put("/api/abc123")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.url").value("URL cannot be null"));
    }

    @Test
    @DisplayName("Blank URL")
    void testUpdateUrl_BlankUrl() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl(""); // Blank URL

      mockMvc.perform(put("/api/abc123")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.url").value("URL cannot be blank"));
    }
  }

  @Nested
  @DisplayName("GET /{shortCode}")
  class RedirectTests {
    @Test
    @DisplayName("Success")
    void testRedirect_Success() throws Exception {
      when(urlShortenerService.getLongUrl("abc123")).thenReturn("https://example.com");

      mockMvc.perform(get("/api/abc123"))
          .andExpect(status().isFound())
          .andExpect(header().string("Location", "https://example.com"));
    }

    @Test
    @DisplayName("Not Found")
    void testRedirect_NotFound() throws Exception {
      when(urlShortenerService.getLongUrl("abc123")).thenReturn(null);

      mockMvc.perform(get("/api/abc123"))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Cache Failure - Returns 500 Internal Server Error")
    void testGetLongUrl_CacheFailure() throws Exception {
      when(urlShortenerService.getLongUrl("abc123"))
          .thenThrow(new CacheFailureException("Cache unavailable"));

      mockMvc.perform(get("/api/abc123"))
          .andExpect(status().isInternalServerError())
          .andExpect(jsonPath("$.message").value("Cache failure: Cache unavailable"));
    }
  }

  @Nested
  @DisplayName("DELETE /{shortCode}")
  class DeleteUrlTests {
    @Test
    @DisplayName("Success")
    void testDeleteUrl_Success() throws Exception {
      when(urlShortenerService.deleteUrl("abc123")).thenReturn(true);

      mockMvc.perform(delete("/api/abc123"))
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Not Found")
    void testDeleteUrl_NotFound() throws Exception {
      when(urlShortenerService.deleteUrl("abc123")).thenReturn(false);

      mockMvc.perform(delete("/api/abc123"))
          .andExpect(status().isNotFound());
    }
  }
}