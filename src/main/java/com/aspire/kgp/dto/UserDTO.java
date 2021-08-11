package com.aspire.kgp.dto;

import com.google.gson.annotations.SerializedName;

public class UserDTO {
  private String id;
  @SerializedName("first_name")
  private String firstName;
  @SerializedName("last_name")
  private String lastName;
  @SerializedName("work_email")
  private String workEmail;
  @SerializedName("private_email")
  private String privateEmail;
  private String token;
  @SerializedName("email")
  private String email;
  public String getEmail() {
	return email;
  }

  public void setEmail(String email) {
	this.email = email;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getWorkEmail() {
    return workEmail;
  }

  public void setWorkEmail(String workEmail) {
    this.workEmail = workEmail;
  }

  public String getPrivateEmail() {
    return privateEmail;
  }

  public void setPrivateEmail(String privateEmail) {
    this.privateEmail = privateEmail;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
