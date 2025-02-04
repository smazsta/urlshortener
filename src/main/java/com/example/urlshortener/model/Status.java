package com.example.urlshortener.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Status {

  @NotNull(message = "Status cannot be null")
  @NotBlank(message = "Status cannot be blank")
  private String status;

  public Status() {
  }

  public Status(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(@NotNull @NotBlank String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "Status{" + "status='" + status + '\'' + '}';
  }
}
