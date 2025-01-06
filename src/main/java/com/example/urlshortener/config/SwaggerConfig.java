package com.example.urlshortener.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI urlShortenerOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("URL Shortener API")
            .description("API for shortening, retrieving, updating, and deleting URLs.")
            .version("1.0")
            .contact(new Contact()
                .name("Alan Vykes")
                .email("alan.vykes@example.com")
                .url("https://example.com"))
        );
  }
}