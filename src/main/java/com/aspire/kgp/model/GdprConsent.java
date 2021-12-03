package com.aspire.kgp.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class GdprConsent extends SuperBase {

  @Column(name = "contactId", nullable = false, unique = true)
  private String contactId;

  @Column(name = "gdprFirstQuestion")
  private String gdprFirstQuestion;

  @Column(name = "gdprSecondQuestion")
  private String gdprSecondQuestion;

  public String getContactId() {
    return contactId;
  }

  public void setContactId(String contactId) {
    this.contactId = contactId;
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


}
