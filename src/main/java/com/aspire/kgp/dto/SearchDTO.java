package com.aspire.kgp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.google.gson.annotations.SerializedName;

@JsonFilter("searchFilter")
public class SearchDTO {
  private String id;
  @SerializedName("job_title")
  private String jobTitle;
  @SerializedName("job_number")
  private String jobNumber;
  private String stage;
  private CompanyDTO company;
  private List<UserDTO> partners;
  private List<UserDTO> recruiters;
  private List<UserDTO> researchers;
  private List<UserDTO> eas;

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

  public List<UserDTO> getPartners() {
    return partners;
  }

  public void setPartners(List<UserDTO> partners) {
    this.partners = partners;
  }

  public List<UserDTO> getRecruiters() {
    return recruiters;
  }

  public void setRecruiters(List<UserDTO> recruiters) {
    this.recruiters = recruiters;
  }

  public List<UserDTO> getResearchers() {
    return researchers;
  }

  public void setResearchers(List<UserDTO> researchers) {
    this.researchers = researchers;
  }

  public List<UserDTO> getEas() {
    return eas;
  }

  public void setEas(List<UserDTO> eas) {
    this.eas = eas;
  }

}
