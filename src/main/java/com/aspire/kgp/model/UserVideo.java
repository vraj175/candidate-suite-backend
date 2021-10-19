package com.aspire.kgp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserVideo extends SuperBase {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userSearch", referencedColumnName = "id", insertable = true, nullable = false,
      updatable = true)
  private UserSearch userSearch;

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

  public UserSearch getUserSearch() {
    return userSearch;
  }

  public void setUserSearch(UserSearch userSearch) {
    this.userSearch = userSearch;
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
