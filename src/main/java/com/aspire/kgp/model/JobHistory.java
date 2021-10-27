package com.aspire.kgp.model;

import javax.persistence.Entity;

@Entity
public class JobHistory extends SuperBase {
  private String galaxyId;
  private String contactId;
  private String companyName;
  private String startYear;
  private String endYear;
  private String title;

  public String getGalaxyId() {
    return galaxyId;
  }

  public void setGalaxyId(String galaxyId) {
    this.galaxyId = galaxyId;
  }

  public String getContactId() {
    return contactId;
  }

  public void setContactId(String contactId) {
    this.contactId = contactId;
  }

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


}
