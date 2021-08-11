package com.aspire.kgp.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.aspire.kgp.validator.Language;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InviteDTO {
  
  @JsonProperty(value="Candidate Id" ,required = true)
  @NotEmpty(message = "candidate id cannot be missing or empty")
  String candidateId;
  
  @Language
  @JsonProperty(value="Language" ,required = true)
  String language;
  
  @JsonProperty(value="Email" ,required = true)
  @NotEmpty(message = "Email must not be empty")
  @Email(message = "Email must be a valid email address")
  String email;
  
  @JsonProperty("BCC")
  String[] bcc;
  
  @JsonProperty("remove Duplicate")
  boolean removeDuplicate;

  @JsonProperty(value = "Partner Id", required = true)
  @NotEmpty(message = "partner id cannot be missing or empty")
  String partnerId;

  public String getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
  }

  public String getCandidateId() {
    return candidateId;
  }

  public void setCandidateId(String candidateId) {
    this.candidateId = candidateId;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String[] getBcc() {
    return bcc;
  }

  public void setBcc(String[] bcc) {
    this.bcc = bcc;
  }

  public boolean isRemoveDuplicate() {
    return removeDuplicate;
  }

  public void setRemoveDuplicate(boolean removeDuplicate) {
    this.removeDuplicate = removeDuplicate;
  }

}
