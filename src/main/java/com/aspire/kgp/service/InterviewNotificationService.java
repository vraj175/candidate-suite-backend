package com.aspire.kgp.service;

import java.util.Date;
import java.util.List;

import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.ClientTeamDTO;
import com.aspire.kgp.dto.InterviewNotificationRequestDTO;
import com.aspire.kgp.dto.UserDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface InterviewNotificationService {

  void sendNotification(String schedulerType);

  String getInterViewDetails(String schedulerType);

  void sendCandidateNotification(String mailSubject, String schedulerType,
      CandidateDTO candidateDTO, UserDTO userDTO, ClientTeamDTO clientTeamDTO, String stage,
      String templateName);

  void sendKgpPartnerNotification(String mailSubject, String schedulerType,
      CandidateDTO candidateDTO, UserDTO userDTO, String templateName);

  void sendClientNotification(String mailSubject, String time, CandidateDTO candidateDTO,
      ClientTeamDTO clientTeamDTO, String templateName);

  void sendMail(String email, String mailSubject, String contain);

  List<CandidateDTO> getCandidateListFromJsonResponse(JsonArray jsonArray, String team);

  List<UserDTO> addKgpTeamJsonArraytoList(JsonObject json, String listfor);

  List<ClientTeamDTO> addClientTeamJsonArraytoList(JsonObject json, String listfor);

  InterviewNotificationRequestDTO getScheduleDate(Date currentDateAndTime, String type);

}
