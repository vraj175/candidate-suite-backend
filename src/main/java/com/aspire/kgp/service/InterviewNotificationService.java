package com.aspire.kgp.service;

public interface InterviewNotificationService {

  public void sendNotification(String schedulerType);

  public String getInterViewDetails(String schedulerType);

}
