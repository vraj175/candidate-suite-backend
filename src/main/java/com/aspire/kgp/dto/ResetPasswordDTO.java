package com.aspire.kgp.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResetPasswordDTO {
  @JsonProperty(value = "Email", required = true)
  @NotEmpty(message = "Email must not be empty")
  @Email(message = "Email must be a valid email address")
  private String email;

  @JsonProperty(value = "old Password", required = true)
  @NotEmpty(message = "old Password cannot be missing or empty")
  private String oldPassword;

  @JsonProperty(value = "new Password", required = true)
  @NotEmpty(message = "new Password cannot be missing or empty")
  private String newPassword;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getOldPassword() {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

}
