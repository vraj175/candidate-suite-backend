package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("candidateFilter")
public class CandidateDTO {
  private String id;
  private UserDTO user;
  private SearchDTO search;
  private ContactDTO contact;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SearchDTO getSearch() {
    return search;
  }

  public void setSearch(SearchDTO search) {
    this.search = search;
  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }

  public ContactDTO getContact() {
    return contact;
  }

  public void setContact(ContactDTO contact) {
    this.contact = contact;
  }
  
  
}
