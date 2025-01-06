package com.example.urlshortener.controller;

import com.example.urlshortener.dto.UrlRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "URL Shortener", description = "API for shortening, retrieving, updating, and deleting URLs.")
@RequestMapping(path = UrlShortenerResource.PATH)
@Validated
public interface UrlShortenerResource {
  String PATH = "/api";

  @PostMapping("/shorten")
  @Operation(summary = "Shorten a URL", description = "Creates a short code for the provided long URL.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "URL successfully shortened"),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "409", description = "Custom code is already in use")
  })
  ResponseEntity<String> shortenUrl(
      @Parameter(description = "URL to be shortened", required = true)
      @Valid @RequestBody UrlRequest request);

  @GetMapping("/{shortCode}")
  @Operation(summary = "Redirect to the original URL",description = "Redirects to the original URL associated with the provided short code.")
  @ApiResponses({
      @ApiResponse(responseCode = "302", description = "Redirect to the original URL"),
      @ApiResponse(responseCode = "404", description = "Short code not found")
  })
  ResponseEntity<Void> redirect(
      @Parameter(description = "Short code for the URL", required = true)
      @PathVariable String shortCode);

  @DeleteMapping("/{shortCode}")
  @Operation(summary = "Delete a URL",description = "Deletes the URL mapping associated with the provided short code.")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "URL successfully deleted"),
      @ApiResponse(responseCode = "404", description = "Short code not found")
  })
  ResponseEntity<Void> deleteUrl(
      @Parameter(description = "Short code for the URL", required = true)
      @PathVariable String shortCode);

  @PutMapping("/{shortCode}")
  @Operation(
      summary = "Update a URL",
      description = "Updates the original URL associated with the provided short code."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "URL successfully updated"),
      @ApiResponse(responseCode = "404", description = "Short code not found")
  })
  ResponseEntity<Void> updateUrl(
      @Parameter(description = "Short code for the URL", required = true)
      @PathVariable String shortCode,
      @Parameter(description = "New URL to associate with the short code", required = true)
      @Valid @RequestBody UrlRequest request);
}
