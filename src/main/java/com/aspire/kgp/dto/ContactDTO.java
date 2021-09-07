package com.aspire.kgp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.google.gson.annotations.SerializedName;

@JsonFilter("contactFilter")
public class ContactDTO extends UserDTO {
  @SerializedName("current_job_title")
  private String currentJobTitle;
  private CompanyDTO company;
  @SerializedName("published_bio")
  private String publishedBio;
  @SerializedName("home_phone")
  private String homePhone;
  @SerializedName("base_salary")
  private String baseSalary;
  @SerializedName("target_bonus_value")
  private String targetBonusValue;
  private String equity;
  @SerializedName("compensation_expectation")
  private String compensationExpectation;
  @SerializedName("compensation_notes")
  private String compensationNotes;
  @SerializedName("job_history")
  private List<JobHistoryDTO> jobHistory;
  @SerializedName("education_details")
  private List<EducationDTO> educationDetails;
  @SerializedName("board_details")
  private List<BoardDetailsDTO> boardDetails;

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

  public String getPublishedBio() {
    return publishedBio;
  }

  public void setPublishedBio(String publishedBio) {
    this.publishedBio = publishedBio;
  }

  public String getHomePhone() {
    return homePhone;
  }

  public void setHomePhone(String homePhone) {
    this.homePhone = homePhone;
  }

  public String getBaseSalary() {
    return baseSalary;
  }

  public void setBaseSalary(String baseSalary) {
    this.baseSalary = baseSalary;
  }

  public String getTargetBonusValue() {
    return targetBonusValue;
  }

  public void setTargetBonusValue(String targetBonusValue) {
    this.targetBonusValue = targetBonusValue;
  }

  public String getEquity() {
    return equity;
  }

  public void setEquity(String equity) {
    this.equity = equity;
  }

  public String getCompensationExpectation() {
    return compensationExpectation;
  }

  public void setCompensationExpectation(String compensationExpectation) {
    this.compensationExpectation = compensationExpectation;
  }

  public String getCompensationNotes() {
    return compensationNotes;
  }

  public void setCompensationNotes(String compensationNotes) {
    this.compensationNotes = compensationNotes;
  }

  public List<JobHistoryDTO> getJobHistory() {
    return jobHistory;
  }

  public void setJobHistory(List<JobHistoryDTO> jobHistory) {
    this.jobHistory = jobHistory;
  }

  public List<EducationDTO> getEducationDetails() {
    return educationDetails;
  }

  public void setEducationDetails(List<EducationDTO> educationDetails) {
    this.educationDetails = educationDetails;
  }

  public List<BoardDetailsDTO> getBoardDetails() {
    return boardDetails;
  }

  public void setBoardDetails(List<BoardDetailsDTO> boardDetails) {
    this.boardDetails = boardDetails;
  }
}
