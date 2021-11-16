package com.aspire.kgp.model;

import java.util.ArrayList;
import java.util.List;

import com.aspire.kgp.dto.EducationDTO;

public class Contact {

  private String galaxyId;

  private String firstName;

  private String lastName;

  private String city;

  private String state;

  private String company;

  private String currentJobTitle;

  private String mobilePhone;

  private String homePhone;

  private String workEmail;

  private String email;

  private String linkedInUrl;

  private String compensationNotes;

  private String compensationExpectation;

  private String equity;

  private String baseSalary;

  private String targetBonusValue;

  private List<BoardHistory> boardHistory = new ArrayList<>();

  private List<JobHistory> jobHistory = new ArrayList<>();

  private List<EducationDTO> educationDetails;

  private String currentJobStartYear;

  private String currentJobEndtYear;


  public String getGalaxyId() {
    return galaxyId;
  }

  public void setGalaxyId(String galaxyId) {
    this.galaxyId = galaxyId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getCurrentJobTitle() {
    return currentJobTitle;
  }

  public void setCurrentJobTitle(String currentJobTitle) {
    this.currentJobTitle = currentJobTitle;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getHomePhone() {
    return homePhone;
  }

  public void setHomePhone(String homePhone) {
    this.homePhone = homePhone;
  }

  public String getWorkEmail() {
    return workEmail;
  }

  public void setWorkEmail(String workEmail) {
    this.workEmail = workEmail;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLinkedInUrl() {
    return linkedInUrl;
  }

  public void setLinkedInUrl(String linkedInUrl) {
    this.linkedInUrl = linkedInUrl;
  }

  public String getCompensationNotes() {
    return compensationNotes;
  }

  public void setCompensationNotes(String compensationNotes) {
    this.compensationNotes = compensationNotes;
  }

  public String getCompensationExpectation() {
    return compensationExpectation;
  }

  public void setCompensationExpectation(String compensationExpectation) {
    this.compensationExpectation = compensationExpectation;
  }

  public String getEquity() {
    return equity;
  }

  public void setEquity(String equity) {
    this.equity = equity;
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

  public List<BoardHistory> getBoardHistory() {
    return boardHistory;
  }

  public void setBoardHistory(List<BoardHistory> boardHistory) {
    this.boardHistory = boardHistory;
  }

  public List<JobHistory> getJobHistory() {
    return jobHistory;
  }

  public void setJobHistory(List<JobHistory> jobHistory) {
    this.jobHistory = jobHistory;
  }

  public List<EducationDTO> getEducationDetails() {
    return educationDetails;
  }

  public void setEducationDetails(List<EducationDTO> educationDetails) {
    this.educationDetails = educationDetails;
  }

  public String getCurrentJobStartYear() {
    return currentJobStartYear;
  }

  public void setCurrentJobStartYear(String currentJobStartYear) {
    this.currentJobStartYear = currentJobStartYear;
  }

  public String getCurrentJobEndtYear() {
    return currentJobEndtYear;
  }

  public void setCurrentJobEndtYear(String currentJobEndtYear) {
    this.currentJobEndtYear = currentJobEndtYear;
  }

  @Override
  public String toString() {
    return "Contact [galaxyId=" + galaxyId + ", firstName=" + firstName + ", lastName=" + lastName
        + ", city=" + city + ", state=" + state + ", company=" + company + ", currentJobTitle="
        + currentJobTitle + ", mobilePhone=" + mobilePhone + ", homePhone=" + homePhone
        + ", workEmail=" + workEmail + ", email=" + email + ", linkedInUrl=" + linkedInUrl
        + ", compensationNotes=" + compensationNotes + ", compensationExpectation="
        + compensationExpectation + ", equity=" + equity + ", baseSalary=" + baseSalary
        + ", targetBonusValue=" + targetBonusValue + ", boardHistory=" + boardHistory
        + ", jobHistory=" + jobHistory + ", educationDetails=" + educationDetails + "]";
  }


}
