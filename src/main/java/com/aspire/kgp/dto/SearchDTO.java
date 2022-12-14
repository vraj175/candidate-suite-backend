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
  @SerializedName("client_team")
  private List<ClientTeamDTO> clienTeam;
  private String city;
  private String state;
  @SerializedName("is_approved_by_partner")
  private String isApprovedByPartner;

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

  public List<ClientTeamDTO> getClienTeam() {
    return clienTeam;
  }

  public void setClienTeam(List<ClientTeamDTO> clienTeam) {
    this.clienTeam = clienTeam;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getIsApprovedByPartner() {
    return isApprovedByPartner;
  }

  public void setIsApprovedByPartner(String isApprovedByPartner) {
    this.isApprovedByPartner = isApprovedByPartner;
  }

  @Override
  public String toString() {
    return "SearchDTO [id=" + id + ", jobTitle=" + jobTitle + ", jobNumber=" + jobNumber
        + ", stage=" + stage + ", company=" + company + ", partners=" + partners + ", recruiters="
        + recruiters + ", researchers=" + researchers + ", eas=" + eas + ", clienTeam=" + clienTeam
        + ", city=" + city + ", state=" + state + "]";
  }

}
