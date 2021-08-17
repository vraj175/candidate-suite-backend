package com.aspire.kgp.dto;

import com.google.gson.annotations.SerializedName;

public class ContactDTO extends UserDTO {
  @SerializedName("current_job_title")
  private String currentJobTitle;
  private CompanyDTO company;

  public String getCurrentJobTitle() {
    return currentJobTitle;
  }

  public void setCurrentJobTitle(String currentJobTitle) {
    this.currentJobTitle = currentJobTitle;
  }

  public CompanyDTO getCompany() {
    return company;
  }

  public void setCompany(CompanyDTO company) {
    this.company = company;
  }

}
