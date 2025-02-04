package com.example.urlshortener.controller;

import com.example.urlshortener.model.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

  @GetMapping
  public ResponseEntity<Status> getStatus() {
    return ResponseEntity.ok(new Status("UP"));
  }
}
