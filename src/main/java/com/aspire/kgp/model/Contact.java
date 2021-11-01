package com.aspire.kgp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.aspire.kgp.dto.EducationDTO;
import com.google.gson.annotations.SerializedName;

@Entity
public class Contact extends SuperBase {

  @Column(name = "galaxyId", nullable = false)
  private String galaxyId;

  @Column(name = "firstName", nullable = false)
  private String firstName;

  @Column(name = "lastName", nullable = false)
  private String lastName;

  @Column(name = "city")
  private String city;

  @Column(name = "state")
  private String state;

  @Column(name = "company", nullable = false)
  private String company;

  @Column(name = "currentJobTitle", nullable = false)
  private String currentJobTitle;

  @Column(name = "mobilePhone")
  private String mobilePhone;

  @Column(name = "homePhone")
  private String homePhone;

  @Column(name = "workEmail")
  private String workEmail;

  @Column(name = "email")
  private String email;

  @Column(name = "linkedInUrl")
  private String linkedInUrl;

  @Column(name = "compensationNotes")
  private String compensationNotes;

  @Column(name = "compensationExpectation")
  private String compensationExpectation;

  @Column(name = "equity")
  private String equity;

  @Column(name = "baseSalary")
  private String baseSalary;

  @Column(name = "targetBonusValue")
  private String targetBonusValue;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "contact", referencedColumnName = "id")
  private List<BoardHistory> boardHistory = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "contact", referencedColumnName = "id")
  private List<JobHistory> jobHistory = new ArrayList<>();

  @Transient
  @SerializedName("education_details")
  private List<EducationDTO> educationDetails;


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
