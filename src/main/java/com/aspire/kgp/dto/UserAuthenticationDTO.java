package com.aspire.kgp.dto;

public class UserAuthenticationDTO {
  String id;
  String name;
  String accessToken;
  String refreshToken;
  
  public UserAuthenticationDTO(String id, String name, String accessToken, String refreshToken) {
    super();
    this.id = id;
    this.name = name;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
