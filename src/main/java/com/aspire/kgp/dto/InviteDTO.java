package com.aspire.kgp.dto;

public class InviteDTO {
  String candidateId;
  String language;
  String email;
  String[] bcc;
  boolean removeDuplicate;

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
