package com.example.urlshortener.controller;

import com.example.urlshortener.dto.UrlRequest;
import com.example.urlshortener.service.UrlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrlController.class)
@ActiveProfiles("test")
class UrlControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UrlService urlService;

  @Nested
  class POST_url {

    @Test
    @DisplayName("Should return short URL")
    void shouldReturnShortUrlTest() throws Exception {
      String shortUrl = "google";
      when(urlService.createShortUrl(anyString())).thenReturn(shortUrl);

      UrlRequest request = new UrlRequest("www.google.com");

      mockMvc.perform(post("/url/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.url").value(shortUrl));
    }

    @ParameterizedTest
    @DisplayName("Should return bad request for invalid URLs")
    @ValueSource(strings = { " ", "", "null" })
    void shouldReturnBadRequestForInvalidUrls(String url) throws Exception {
      String testUrl = "null".equals(url) ? null : url;
      UrlRequest request = new UrlRequest(testUrl);

      mockMvc.perform(post("/url/shorten")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  class GET_url {
    @Test
    @DisplayName("Should return original URL")
    void shouldReturnOriginalUrlTest() throws Exception {
      String shortUrl = "google";
      String originalUrl = "www.google.com";
      when(urlService.getOriginalUrl(anyString())).thenReturn(Optional.of(originalUrl));

      mockMvc.perform(get("/url/original")
              .contentType(MediaType.APPLICATION_JSON)
              .param("shortUrl", shortUrl))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.url").value(originalUrl));
    }

    @Test
    @DisplayName("Should return Not Found for missing URL")
    void shouldReturnNotFoundTest() throws Exception {
      String shortUrl = "google";
      when(urlService.getOriginalUrl(anyString())).thenReturn(Optional.empty());

      mockMvc.perform(get("/url/original")
              .contentType(MediaType.APPLICATION_JSON)
              .param("shortUrl", shortUrl))
          .andExpect(status().isNotFound());
    }
  }
}