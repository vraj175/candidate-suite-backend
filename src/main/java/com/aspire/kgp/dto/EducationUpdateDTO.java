package com.aspire.kgp.dto;

import com.google.gson.annotations.SerializedName;

public class EducationUpdateDTO extends EducationDTO {

  @SerializedName("contact_id")
  private String contactId;

  public String getContactId() {
    return contactId;
  }

  public void setContactId(String contactId) {
    this.contactId = contactId;
  }
}
