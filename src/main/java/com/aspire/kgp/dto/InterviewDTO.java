package com.aspire.kgp.dto;

import com.aspire.kgp.util.CommonUtil;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.google.gson.annotations.SerializedName;

@JsonFilter("interviewFilter")
public class InterviewDTO {
  private String id;
  private String method;
  private String comments;
  private int position;
  @SerializedName("interview_date")
  private String interviewDate;
  private UserDTO client;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public String getInterviewDate() {
    return CommonUtil.removeTime(interviewDate);
  }

  public void setInterviewDate(String interviewDate) {
    this.interviewDate = interviewDate;
  }

  public UserDTO getClient() {
    return client;
  }

  public void setClient(UserDTO client) {
    this.client = client;
  }

}
