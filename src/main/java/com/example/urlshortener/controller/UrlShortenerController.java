package com.example.urlshortener.controller;

import com.example.urlshortener.dto.UrlRequest;
import com.example.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlShortenerController {

  @Autowired
  private UrlShortenerService urlShortenerService;

  @PostMapping("/shorten")
  public ResponseEntity<String> shortenUrl(@Valid @RequestBody UrlRequest request) {
    String shortCode = urlShortenerService.shortenUrl(request.getUrl());
    return ResponseEntity.ok(shortCode);
  }

  @GetMapping("/{shortCode}")
  public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
    String longUrl = urlShortenerService.getLongUrl(shortCode);
    if (longUrl != null) {
      return ResponseEntity.status(HttpStatus.FOUND)
          .location(URI.create(longUrl))
          .build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @DeleteMapping("/{shortCode}")
  public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode) {
    boolean deleted = urlShortenerService.deleteUrl(shortCode);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @PutMapping("/{shortCode}")
  public ResponseEntity<Void> updateUrl(@PathVariable String shortCode, @Valid @RequestBody UrlRequest request) {
    boolean updated = urlShortenerService.updateUrl(shortCode, request.getUrl());
    if (updated) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}