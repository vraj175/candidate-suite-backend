package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("clientTeamFilter")
public class ClientTeamDTO {

  private String id;
  private ContactDTO contact;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ContactDTO getContact() {
    return contact;
  }

  public void setContact(ContactDTO contact) {
    this.contact = contact;
  }


}
