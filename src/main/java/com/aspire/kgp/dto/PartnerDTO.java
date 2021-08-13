package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PartnerDTO {

  private String url;
  private String partnerName;
  private String partnerTitle;
  private UserDTO user;

  public String getPartnerName() {
    return partnerName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setPartnerName(String partnerName) {
    this.partnerName = partnerName;
  }

  public String getPartnerTitle() {
    return partnerTitle;
  }

  public void setPartnerTitle(String partnerTitle) {
    this.partnerTitle = partnerTitle;
  }

  @JsonIgnore
  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }

}
