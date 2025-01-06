package com.example.urlshortener.controller;

import com.example.urlshortener.dto.UrlRequest;
import com.example.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@Validated
public class UrlShortenerController implements UrlShortenerResource {

  @Autowired
  private UrlShortenerService urlShortenerService;

  @Override
  public ResponseEntity<String> shortenUrl(UrlRequest request) {
    String shortCode = urlShortenerService.shortenUrl(request.getUrl(), request.getCustomCode());
    return ResponseEntity.ok(shortCode);
  }

  @Override
  public ResponseEntity<Void> redirect(String shortCode) {
    String longUrl = urlShortenerService.getLongUrl(shortCode);
    if (longUrl != null) {
      return ResponseEntity.status(HttpStatus.FOUND)
          .location(URI.create(longUrl))
          .build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Override
  public ResponseEntity<Void> deleteUrl(String shortCode) {
    boolean deleted = urlShortenerService.deleteUrl(shortCode);
    if (deleted) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Override
  public ResponseEntity<Void> updateUrl(String shortCode, UrlRequest request) {
    boolean updated = urlShortenerService.updateUrl(shortCode, request.getUrl());
    if (updated) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}