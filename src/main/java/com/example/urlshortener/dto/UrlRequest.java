/*
 * Copyright (C) Smazsta, Inc.
 * All Rights Reserved.
 */
package com.example.urlshortener.dto;

import com.example.urlshortener.validation.ValidUrl;
import com.example.urlshortener.validation.ValidationGroups;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@GroupSequence({ValidationGroups.NotNullGroup.class, ValidationGroups.NotBlankGroup.class, UrlRequest.class})
public class UrlRequest {

  @NotNull(message = "URL cannot be null", groups = ValidationGroups.NotNullGroup.class)
  @NotBlank(message = "URL cannot be blank", groups = ValidationGroups.NotBlankGroup.class)
  @Size(max = 2048, message = "URL must be less than 2048 characters")
  @ValidUrl(message = "Invalid URL format")
  private String url;
}