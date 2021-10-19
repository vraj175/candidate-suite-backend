package com.aspire.kgp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NotificationSchedule extends SuperBase {

  @Column(name = "scheduleId", nullable = false)
  private String scheduleId;

  @Column(name = "taskType", nullable = false)
  private String taskType;

  @Column(name = "candidateId", nullable = false)
  private String candidateId;

  @Column(name = "message", nullable = false)
  private String message;

  @Column(name = "date", nullable = false)
  private Date date;

  public String getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(String scheduleId) {
    this.scheduleId = scheduleId;
  }

  public String getTaskType() {
    return taskType;
  }

  public void setTaskType(String taskType) {
    this.taskType = taskType;
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

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }


}
