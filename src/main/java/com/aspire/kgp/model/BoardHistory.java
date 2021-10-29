package com.aspire.kgp.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class BoardHistory extends SuperBase {

  @Column(name = "companyName")
  private String companyName;

  @Column(name = "startYear")
  private String startYear;

  @Column(name = "endYear")
  private String endYear;

  @Column(name = "title")
  private String title;

  @Column(name = "commitee")
  private String commitee;


  public String getCompanyName() {
    return companyName;
  }


  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }


  public String getStartYear() {
    return startYear;
  }


  public void setStartYear(String startYear) {
    this.startYear = startYear;
  }


  public String getEndYear() {
    return endYear;
  }


  public void setEndYear(String endYear) {
    this.endYear = endYear;
  }


  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }


  public String getCommitee() {
    return commitee;
  }


  public void setCommitee(String commitee) {
    this.commitee = commitee;
  }

}
