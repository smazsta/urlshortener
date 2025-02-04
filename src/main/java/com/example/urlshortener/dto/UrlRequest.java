package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UrlRequest {

  @NotNull(message = "URL cannot be null")
  @NotBlank(message = "URL cannot be blank")
  private String url;

  public UrlRequest() {
    // empty
  }

  public UrlRequest(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
