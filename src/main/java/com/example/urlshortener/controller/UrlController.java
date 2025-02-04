package com.example.urlshortener.controller;

import com.example.urlshortener.dto.UrlRequest;
import com.example.urlshortener.dto.UrlResponse;
import com.example.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/url")
@Validated
public class UrlController {

  private final UrlService urlService;

  public UrlController(UrlService urlService) {
    this.urlService = urlService;
  }

  @PostMapping("/shorten")
  public ResponseEntity<UrlResponse> createShortUrl(@RequestBody @Valid UrlRequest urlRequest) {
    UrlResponse response = new UrlResponse(urlService.createShortUrl(urlRequest.getUrl()));
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/original")
  public ResponseEntity<UrlResponse> getOriginalUrl(@RequestParam(name = "shortUrl") @NotNull @NotBlank String shortUrl) {
    return urlService.getOriginalUrl(shortUrl)
        .map(body -> ResponseEntity.ok(new UrlResponse(body)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}

