package com.example.urlshortener.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class UrlEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull(message = "Original URL cannot be null")
  @NotBlank(message = "Original URL cannot be blank")
  private String originalUrl;

  @NotNull(message = "Short URL cannot be null")
  @NotBlank(message = "Short URL cannot be blank")
  private String shortUrl;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  public UrlEntity() {
    // empty
  }

  public UrlEntity(String originalUrl, String shortUrl) {
    this.originalUrl = originalUrl;
    this.shortUrl = shortUrl;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(@NotNull @NotBlank String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public void setShortUrl(@NotNull @NotBlank String shortUrl) {
    this.shortUrl = shortUrl;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@NotNull LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "UrlEntity{" + "id=" + id
        + ", originalUrl='" + originalUrl + '\''
        + ", shortUrl='" + shortUrl + '\''
        + ", createdAt=" + createdAt + '}';
  }
}

