package com.example.urlshortener.controller;

import com.example.urlshortener.config.TestSecurityConfig;
import com.example.urlshortener.dto.UrlRequest;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

      mockMvc.perform(post("/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(content().string("abc123"));
    }

    @Test
    @DisplayName("Invalid URL")
    void testShortenUrl_InvalidUrl() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl("invalid-url"); // Invalid URL

      mockMvc.perform(post("/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.url").value("URL must be valid"));
    }

    @Test
    @DisplayName("Null URL")
    void testShortenUrl_NullUrl() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl(null); // Null URL

      mockMvc.perform(post("/shorten")
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

      mockMvc.perform(post("/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.url").value("URL cannot be blank"));
    }
  }

  @Nested
  @DisplayName("PUT /{shortCode}")
  class UpdateUrlTests {
    @Test
    @DisplayName("Success")
    void testUpdateUrl_Success() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl("https://new-example.com");

      when(urlShortenerService.updateUrl("abc123", request.getUrl())).thenReturn(true);

      mockMvc.perform(put("/abc123")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Invalid URL")
    void testUpdateUrl_InvalidUrl() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl("invalid-url"); // Invalid URL

      mockMvc.perform(put("/abc123")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.url").value("URL must be valid"));
    }

    @Test
    @DisplayName("Null URL")
    void testUpdateUrl_NullUrl() throws Exception {
      UrlRequest request = new UrlRequest();
      request.setUrl(null); // Null URL

      mockMvc.perform(put("/abc123")
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

      mockMvc.perform(put("/abc123")
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

      mockMvc.perform(get("/abc123"))
          .andExpect(status().isFound())
          .andExpect(header().string("Location", "https://example.com"));
    }

    @Test
    @DisplayName("Not Found")
    void testRedirect_NotFound() throws Exception {
      when(urlShortenerService.getLongUrl("abc123")).thenReturn(null);

      mockMvc.perform(get("/abc123"))
          .andExpect(status().isNotFound());
    }
  }

  @Nested
  @DisplayName("DELETE /{shortCode}")
  class DeleteUrlTests {
    @Test
    @DisplayName("Success")
    void testDeleteUrl_Success() throws Exception {
      when(urlShortenerService.deleteUrl("abc123")).thenReturn(true);

      mockMvc.perform(delete("/abc123"))
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Not Found")
    void testDeleteUrl_NotFound() throws Exception {
      when(urlShortenerService.deleteUrl("abc123")).thenReturn(false);

      mockMvc.perform(delete("/abc123"))
          .andExpect(status().isNotFound());
    }
  }
}