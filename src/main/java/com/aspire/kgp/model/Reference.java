package com.aspire.kgp.model;

import javax.persistence.Entity;

@Entity
public class Reference extends SuperBase {
  private String galaxyId;
  private String contactId;
  private String refContactName;
  private String searchName;
  private String phone;
  private String email;
  private String relationship;
  private String refType;

  public String getGalaxyId() {
    return galaxyId;
  }

  public void setGalaxyId(String galaxyId) {
    this.galaxyId = galaxyId;
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
