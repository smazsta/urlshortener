package com.example.urlshortener.dto;

import com.example.urlshortener.annotation.ValidationGroups;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@GroupSequence({ValidationGroups.NotNullGroup.class, ValidationGroups.NotBlankGroup.class, UrlRequest.class})
public class UrlRequest {

  @NotNull(message = "URL cannot be null", groups = ValidationGroups.NotNullGroup.class)
  @NotBlank(message = "URL cannot be blank", groups = ValidationGroups.NotBlankGroup.class)
  @URL(message = "URL must be valid")
  private String url;
}