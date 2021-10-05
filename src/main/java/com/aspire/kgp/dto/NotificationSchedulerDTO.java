package com.aspire.kgp.dto;

import javax.validation.constraints.NotEmpty;

public class NotificationSchedulerDTO {
  @NotEmpty(message = "candidateId cannot be missing or empty")
  private String candidateId;

  @NotEmpty(message = "scheduleId cannot be missing or empty")
  private String scheduleId;

  @NotEmpty(message = "interview schedule message cannot be missing or empty")
  private String message;

  @NotEmpty(message = "interview schedule date cannot be missing or empty")
  private String date;

  public String getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(String scheduleId) {
    this.scheduleId = scheduleId;
  }

  public String getCandidateId() {
    return candidateId;
  }

  public void setCandidateId(String candidateId) {
    this.candidateId = candidateId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
