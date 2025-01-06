package com.example.urlshortener.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.UrlValidator;

public class CustomUrlValidator implements ConstraintValidator<ValidUrl, String> {

  private static final String[] SCHEMES = {"http", "https"};

  @Override
  public boolean isValid(String url, ConstraintValidatorContext context) {
    if (url == null) {
      return false;
    }
    UrlValidator urlValidator = new UrlValidator(SCHEMES);
    return urlValidator.isValid(url);
  }
}
