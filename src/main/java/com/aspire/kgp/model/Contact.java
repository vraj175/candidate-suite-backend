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

  @Transient
  @SerializedName("first_name")
  private String firstName;

  @Transient
  @SerializedName("last_name")
  private String lastName;

  @Transient
  @SerializedName("city")
  private String city;

  @Transient
  @SerializedName("state")
  private String state;

  @Transient
  private String company;

  @Transient
  @SerializedName("current_job_title")
  private String currentJobTitle;

  @Transient
  @SerializedName("mobile_phone")
  private String mobilePhone;

  @Transient
  @SerializedName("home_phone")
  private String homePhone;

  @Transient
  @SerializedName("work_email")
  private String workEmail;

  @Transient
  @SerializedName("private_email")
  private String email;

  @Transient
  @SerializedName("linkedin_url")
  private String linkedInUrl;

  @Transient
  private String compensationNotes;

  @Transient
  private String compensationExpectation;

  @Transient
  private String equity;

  @Transient
  private String baseSalary;

  @Transient
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

  @Transient
  @SerializedName("current_job_start_year")
  private String currentJobStartYear;

  @Transient
  @SerializedName("current_job_end_year")
  private String currentJobEndtYear;
  
  @Column(name = "gdprFirstQuestion", nullable = false)
  private String gdprFirstQuestion;
  
  @Column(name = "gdprSecondQuestion", nullable = false)
  private String gdprSecondQuestion;


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

  public String getGdprFirstQuestion() {
    return gdprFirstQuestion;
  }

  public void setGdprFirstQuestion(String gdprFirstQuestion) {
    this.gdprFirstQuestion = gdprFirstQuestion;
  }

  public String getGdprSecondQuestion() {
    return gdprSecondQuestion;
  }

  public void setGdprSecondQuestion(String gdprSecondQuestion) {
    this.gdprSecondQuestion = gdprSecondQuestion;
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
        + ", jobHistory=" + jobHistory + ", educationDetails=" + educationDetails
        + ", currentJobStartYear=" + currentJobStartYear + ", currentJobEndtYear="
        + currentJobEndtYear + "]";
  }
}
