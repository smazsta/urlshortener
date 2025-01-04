package com.example.urlshortener.config;

import com.example.urlshortener.controller.UrlShortenerController;
import com.example.urlshortener.service.UrlShortenerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(UrlShortenerController.class)
@Import(CorsConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class CorsConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UrlShortenerService urlShortenerService;

  @Test
  @DisplayName("Preflight request from allowed origin should return CORS headers")
  public void testCorsHeadersForAllowedOrigin() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.options("/example-endpoint")
            .header("Origin", "https://trusted-domain.com")
            .header("Access-Control-Request-Method", "POST")
            .header("Access-Control-Request-Headers", "Content-Type"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.header().exists("Access-Control-Allow-Origin"))
        .andExpect(MockMvcResultMatchers.header()
            .string("Access-Control-Allow-Origin", "https://trusted-domain.com"))
        .andExpect(MockMvcResultMatchers.header()
            .string("Access-Control-Allow-Methods", "GET,POST"))
        .andExpect(MockMvcResultMatchers.header()
            .string("Access-Control-Allow-Headers", "Content-Type"))
        .andExpect(MockMvcResultMatchers.header()
            .string("Access-Control-Allow-Credentials", "true"))
        .andExpect(MockMvcResultMatchers.header().exists("Access-Control-Max-Age"));
  }

  @Test
  @DisplayName("Preflight request from disallowed origin should return 403 Forbidden")
  public void testCorsHeadersForDisallowedOrigin() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.options("/example-endpoint")
            .header("Origin", "https://untrusted-domain.com")
            .header("Access-Control-Request-Method", "POST")
            .header("Access-Control-Request-Headers", "Content-Type"))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @DisplayName("Actual request from allowed origin should include CORS headers")
  public void testCorsHeadersForActualRequest() throws Exception {
    when(urlShortenerService.shortenUrl(anyString())).thenReturn("abc123");

    String jsonPayload = "{ \"url\": \"https://example.com\" }";

    mockMvc.perform(MockMvcRequestBuilders.post("/shorten")
            .header("Origin", "https://trusted-domain.com")
            .contentType("application/json")
            .content(jsonPayload)) // Send valid JSON
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.header().exists("Access-Control-Allow-Origin"))
        .andExpect(MockMvcResultMatchers.header()
            .string("Access-Control-Allow-Origin", "https://trusted-domain.com"));
  }
}