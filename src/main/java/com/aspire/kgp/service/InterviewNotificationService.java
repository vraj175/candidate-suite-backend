package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.dto.CandidateDTO;

public interface InterviewNotificationService {

  public void sendNotification(List<CandidateDTO> list, String type);

  public List<CandidateDTO> getInterViewNotificationDetails(String schedulerType);

}
