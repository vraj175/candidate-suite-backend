package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("candidateFilter")
public class CandidateDTO {
  private String id;
  private UserDTO contact;
  private SearchDTO search;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public UserDTO getContact() {
    return contact;
  }

  public void setContact(UserDTO contact) {
    this.contact = contact;
  }

  public SearchDTO getSearch() {
    return search;
  }

  public void setSearch(SearchDTO search) {
    this.search = search;
  }
}
