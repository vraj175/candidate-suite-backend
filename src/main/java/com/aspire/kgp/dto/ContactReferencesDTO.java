package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.google.gson.annotations.SerializedName;

@JsonFilter("contactReferenceFilter")
public class ContactReferencesDTO {

  private String id;
  @SerializedName("search_id")
  private String searchId;
  private String relationship;
  private ContactDTO contact;
  private String type;
  private String source;
  @SerializedName("ref_contact_id")
  private String refContactId;
  private SearchDTO search;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSearchId() {
    return searchId;
  }

  public void setSearchId(String searchId) {
    this.searchId = searchId;
  }

  public String getRelationship() {
    return relationship;
  }

  public void setRelationship(String relationship) {
    this.relationship = relationship;
  }

  public ContactDTO getContact() {
    return contact;
  }

  public void setContact(ContactDTO contact) {
    this.contact = contact;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getRefContactId() {
    return refContactId;
  }

  public void setRefContactId(String refContactId) {
    this.refContactId = refContactId;
  }

  public SearchDTO getSearch() {
    return search;
  }

  public void setSearch(SearchDTO search) {
    this.search = search;
  }

}
