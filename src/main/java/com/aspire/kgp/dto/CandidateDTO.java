package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("candidateFilter")
public class CandidateDTO {
  private UserDTO contact;
  private SearchDTO search;

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
