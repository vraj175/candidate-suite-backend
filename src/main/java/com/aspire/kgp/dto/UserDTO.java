package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

@JsonFilter("userFilter")
public class UserDTO {
  private String id;
  @SerializedName("first_name")
  private String firstName;
  @SerializedName("last_name")
  private String lastName;
  @SerializedName("work_email")
  private String workEmail;
  @SerializedName(value = "private_email", alternate = {"email"})
  private String email;
  private String role;
  @JsonIgnore
  private String token;
  private String title;
  private String country;
  @SerializedName("linkedin_url")
  private String linkedinUrl;
  private String bio;
  private boolean passwordReset;
  @SerializedName("mobile_phone")
  private String mobilePhone;
  @SerializedName("work_phone")
  private String workPhone;
  private String name;
  private String location;
  @SerializedName("execution_credit")
  private String executionCredit;

  public String getExecutionCredit() {
    return executionCredit;
  }

  public void setExecutionCredit(String executionCredit) {
    this.executionCredit = executionCredit;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  private String state;
  private String city;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getWorkPhone() {
    return workPhone;
  }

  public void setWorkPhone(String workPhone) {
    this.workPhone = workPhone;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getLinkedinUrl() {
    return linkedinUrl;
  }

  public void setLinkedinUrl(String linkedinUrl) {
    this.linkedinUrl = linkedinUrl;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public boolean isPasswordReset() {
    return passwordReset;
  }

  public void setPasswordReset(boolean passwordReset) {
    this.passwordReset = passwordReset;
  }

  @Override
  public String toString() {
    return "UserDTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName
        + ", workEmail=" + workEmail + ", email=" + email + ", role=" + role + ", token=" + token
        + ", title=" + title + ", country=" + country + ", linkedinUrl=" + linkedinUrl + ", bio="
        + bio + ", passwordReset=" + passwordReset + ", mobilePhone=" + mobilePhone + ", workPhone="
        + workPhone + ", name=" + name + "]";
  }

}
