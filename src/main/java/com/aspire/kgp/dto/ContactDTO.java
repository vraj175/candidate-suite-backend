package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.google.gson.annotations.SerializedName;

@JsonFilter("contactFilter")
public class ContactDTO extends UserDTO {
  @SerializedName("current_job_title")
  private String currentJobTitle;
  private CompanyDTO company;
  @SerializedName("mobile_phone")
  private String mobilePhone;
  @SerializedName("published_bio")
  private String publishedBio;

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

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getPublishedBio() {
    return publishedBio;
  }

  public void setPublishedBio(String publishedBio) {
    this.publishedBio = publishedBio;
  }
}
