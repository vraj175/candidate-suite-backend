package com.aspire.kgp.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class UserVideo extends SuperBase {

  @Column(name = "fileToken", nullable = false)
  private String fileToken;

  @Column(columnDefinition = "boolean default false")
  private boolean isDeleted;

  @Column(name = "contactId", nullable = false)
  private String contactId;

  public String getContactId() {
    return contactId;
  }

  public void setContactId(String contactId) {
    this.contactId = contactId;
  }

  public String getFileToken() {
    return fileToken;
  }

  public void setFileToken(String fileToken) {
    this.fileToken = fileToken;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
