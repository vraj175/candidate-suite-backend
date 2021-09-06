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

}
