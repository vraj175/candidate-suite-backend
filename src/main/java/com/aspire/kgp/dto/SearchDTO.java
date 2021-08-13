package com.aspire.kgp.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SearchDTO {
  private String id;
  @SerializedName("job_title")
  private String jobTitle;
  @SerializedName("job_number")
  private String jobNumber;
  private String stage;
  private CompanyDTO company;
  @SerializedName("partners")
  private List<PartnerDTO> partners;
  @SerializedName("recruiters")
  private List<RecruiterDTO> recruiters;

  public List<PartnerDTO> getPartners() {
    return partners;
  }

  public void setPartners(List<PartnerDTO> partners) {
    this.partners = partners;
  }

  public List<RecruiterDTO> getRecruiters() {
    return recruiters;
  }

  public void setRecruiters(List<RecruiterDTO> recruiters) {
    this.recruiters = recruiters;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getJobNumber() {
    return jobNumber;
  }

  public void setJobNumber(String jobNumber) {
    this.jobNumber = jobNumber;
  }

  public String getStage() {
    return stage;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }

  public CompanyDTO getCompany() {
    return company;
  }

  public void setCompany(CompanyDTO company) {
    this.company = company;
  }

}
