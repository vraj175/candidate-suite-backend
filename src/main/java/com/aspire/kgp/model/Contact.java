package com.aspire.kgp.model;

import javax.persistence.Entity;

@Entity
public class Contact extends SuperBase {

  private String galaxyId;
  private String firstName;
  private String lastName;
  private String currentCompanyName;
  private String currentTitle;
  private String workPhone;
  private String mobilePhone;
  private String workEmail;
  private String personalEmail;
  private String linkedInUrl;
  private String city;
  private String state;
  private String compensationNotes;
  private String compensationExpectation;
  private String equity;
  private String baseSalary;
  private String targetBonusSalary;

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

  public String getCurrentCompanyName() {
    return currentCompanyName;
  }

  public void setCurrentCompanyName(String currentCompanyName) {
    this.currentCompanyName = currentCompanyName;
  }

  public String getCurrentTitle() {
    return currentTitle;
  }

  public void setCurrentTitle(String currentTitle) {
    this.currentTitle = currentTitle;
  }

  public String getWorkPhone() {
    return workPhone;
  }

  public void setWorkPhone(String workPhone) {
    this.workPhone = workPhone;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getWorkEmail() {
    return workEmail;
  }

  public void setWorkEmail(String workEmail) {
    this.workEmail = workEmail;
  }

  public String getPersonalEmail() {
    return personalEmail;
  }

  public void setPersonalEmail(String personalEmail) {
    this.personalEmail = personalEmail;
  }

  public String getLinkedInUrl() {
    return linkedInUrl;
  }

  public void setLinkedInUrl(String linkedInUrl) {
    this.linkedInUrl = linkedInUrl;
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

  public String getTargetBonusSalary() {
    return targetBonusSalary;
  }

  public void setTargetBonusSalary(String targetBonusSalary) {
    this.targetBonusSalary = targetBonusSalary;
  }



}
