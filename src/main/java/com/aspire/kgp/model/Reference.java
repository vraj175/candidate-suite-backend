package com.aspire.kgp.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Reference extends SuperBase {

  @Column(name = "contactId")
  private String contactId;

  @Column(name = "refContactName")
  private String refContactName;

  @Column(name = "searchName")
  private String searchName;

  @Column(name = "searchId")
  private String searchId;

  @Column(name = "phone")
  private String phone;

  @Column(name = "email")
  private String email;

  @Column(name = "workEmail")
  private String workEmail;

  @Column(name = "relationship")
  private String relationship;

  @Column(name = "refType")
  private String refType;

  @Column(name = "companyName")
  private String companyName;

  @Column(name = "title")
  private String title;

  public String getWorkEmail() {
    return workEmail;
  }

  public void setWorkEmail(String workEmail) {
    this.workEmail = workEmail;
  }

  public String getSearchId() {
    return searchId;
  }

  public void setSearchId(String searchId) {
    this.searchId = searchId;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContactId() {
    return contactId;
  }

  public void setContactId(String contactId) {
    this.contactId = contactId;
  }

  public String getRefContactName() {
    return refContactName;
  }

  public void setRefContactName(String refContactName) {
    this.refContactName = refContactName;
  }

  public String getSearchName() {
    return searchName;
  }

  public void setSearchName(String searchName) {
    this.searchName = searchName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRelationship() {
    return relationship;
  }

  public void setRelationship(String relationship) {
    this.relationship = relationship;
  }

  public String getRefType() {
    return refType;
  }

  public void setRefType(String refType) {
    this.refType = refType;
  }



}
