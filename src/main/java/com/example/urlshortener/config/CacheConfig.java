package com.example.urlshortener.config;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "cache")
@Validated
public class CacheConfig {

  @Min(1)
  private int maxSize = 1000;
}
