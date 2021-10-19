package com.aspire.kgp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserSearch extends SuperBase {

  @Column(name = "companyId", nullable = false)
  private String companyId;

  @Column(name = "searchId", nullable = false)
  private String searchId;

  @Column(name = "candidateId", nullable = false)
  private String candidateId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user", referencedColumnName = "id", insertable = true, nullable = false,
      updatable = true)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "invitedBy", referencedColumnName = "id", insertable = true, nullable = true,
      updatable = true)
  private User invitedBy;

  @Column(columnDefinition = "boolean default false")
  private boolean isDeleted;

  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  public String getSearchId() {
    return searchId;
  }

  public void setSearchId(String searchId) {
    this.searchId = searchId;
  }

  public String getCandidateId() {
    return candidateId;
  }

  public void setCandidateId(String candidateId) {
    this.candidateId = candidateId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getInvitedBy() {
    return invitedBy;
  }

  public void setInvitedBy(User invitedBy) {
    this.invitedBy = invitedBy;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}
