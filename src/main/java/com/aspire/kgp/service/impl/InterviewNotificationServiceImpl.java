package com.aspire.kgp.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.ClientTeamDTO;
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.service.InterviewNotificationService;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.util.RestUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class InterviewNotificationServiceImpl implements InterviewNotificationService {
  static Log log = LogFactory.getLog(InterviewNotificationServiceImpl.class.getName());

  @Autowired
  MailService mailService;

  @Autowired
  RestUtil restUtil;

  @Override
  public void sendNotification(String schedulerType) {
    log.info("staring email sending...");
    String mailSubject;
    String templateName;
    if (schedulerType.equals(Constant.BEFORE_ONE_HOUR)) {
      mailSubject = "REMINDER: Upcoming Interview starts 1 hour";
      templateName = Constant.INTERVIEW_NOTIFICATION_TEMPLATE;
    } else if (schedulerType.equals(Constant.BEFORE_ONE_DAY)) {
      mailSubject = "REMINDER: Upcoming Interview starts 1 day";
      templateName = Constant.INTERVIEW_NOTIFICATION_TEMPLATE;
    } else {
      mailSubject = "Interview Feedback";
      templateName = Constant.INTERVIEW_FEEDBACK_NOTIFICATION_TEMPLATE;
    }


    String apiResponse = getInterViewDetails(schedulerType);
    JsonObject jsonObjects = (JsonObject) JsonParser.parseString(apiResponse);
    JsonArray kgpTeamJsonArray = jsonObjects.getAsJsonArray("kgpInterviews");
    JsonArray clientJsonArray = jsonObjects.getAsJsonArray("clientsInterviews");


    List<CandidateDTO> kgpInterviewCandidateList =
        getCandidateListFromJsonResponse(kgpTeamJsonArray, Constant.KGP_TEAM);
    List<CandidateDTO> clientInterviewCandidateList =
        getCandidateListFromJsonResponse(clientJsonArray, Constant.CLIENT_TEAM);

    kgpInterviewCandidateList.stream().forEach(candidateDTO -> {
      if (!candidateDTO.getSearch().getPartners().isEmpty()) {

        candidateDTO.getSearch().getPartners().stream().forEach(kgpTeam -> {
          sendCandidateNotification(mailSubject, schedulerType, candidateDTO, kgpTeam, null,
              Constant.KGP_TEAM, templateName);
          sendKgpPartnerNotification(mailSubject, schedulerType, candidateDTO, kgpTeam,
              templateName);
        });
      } else if (!candidateDTO.getSearch().getRecruiters().isEmpty()) {

        candidateDTO.getSearch().getRecruiters().stream().forEach(kgpTeam -> {
          sendCandidateNotification(mailSubject, schedulerType, candidateDTO, kgpTeam, null,
              Constant.KGP_TEAM, templateName);
          sendKgpPartnerNotification(mailSubject, schedulerType, candidateDTO, kgpTeam,
              templateName);
        });

      } else if (!candidateDTO.getSearch().getResearchers().isEmpty()) {

        candidateDTO.getSearch().getResearchers().stream().forEach(kgpTeam -> {
          sendCandidateNotification(mailSubject, schedulerType, candidateDTO, kgpTeam, null,
              Constant.KGP_TEAM, templateName);
          sendKgpPartnerNotification(mailSubject, schedulerType, candidateDTO, kgpTeam,
              templateName);
        });
      } else if (!candidateDTO.getSearch().getEas().isEmpty()) {
        candidateDTO.getSearch().getEas().stream().forEach(kgpTeam -> {
          sendCandidateNotification(mailSubject, schedulerType, candidateDTO, kgpTeam, null,
              Constant.KGP_TEAM, templateName);
          sendKgpPartnerNotification(mailSubject, schedulerType, candidateDTO, kgpTeam,
              templateName);
        });
      }
    });


    clientInterviewCandidateList.stream().forEach(candidateDTO -> {
      candidateDTO.getSearch().getClienTeam().forEach(clientTeam -> {
        sendCandidateNotification(mailSubject, schedulerType, candidateDTO, null, clientTeam,
            Constant.CLIENT_TEAM, templateName);
        sendClientNotification(mailSubject, schedulerType, candidateDTO, clientTeam, templateName);
      });
    });
  }

  @Override
  public void sendCandidateNotification(String mailSubject, String schedulerType,
      CandidateDTO candidateDTO, UserDTO userDTO, ClientTeamDTO clientTeamDTO, String stage,
      String templateName) {
    log.info("candidate notification send... Scheduler Type " + schedulerType);
    log.debug("candidate notification details : type" + schedulerType + " Stage " + stage
        + " kgp team details " + userDTO + " client details " + clientTeamDTO
        + " candidate detail: " + candidateDTO);
    String contain =
        mailService.getInterviewNotificationEmailContent(Constant.CANDIDATE_NOTIFICATION,
            candidateDTO, userDTO, clientTeamDTO, schedulerType, stage, templateName);
    sendMail(candidateDTO.getContact().getEmail() == null ? candidateDTO.getContact().getWorkEmail()
        : candidateDTO.getContact().getEmail(), mailSubject, contain);

  }

  @Override
  public void sendKgpPartnerNotification(String mailSubject, String schedulerType,
      CandidateDTO candidateDTO, UserDTO userDTO, String templateName) {
    log.info("KGP Partner notification send... Scheduler Type " + schedulerType);
    log.debug("KGP Partner notification details : type" + schedulerType + " candidate details "
        + candidateDTO + " kgp team details " + userDTO);
    String contain = mailService.getInterviewNotificationEmailContent(Constant.KGP_NOTIFICATION,
        candidateDTO, userDTO, null, schedulerType, null, templateName);
    sendMail(userDTO.getEmail() == null ? userDTO.getWorkEmail() : userDTO.getEmail(), mailSubject,
        contain);
  }

  @Override
  public void sendClientNotification(String mailSubject, String time, CandidateDTO candidateDTO,
      ClientTeamDTO clientTeamDTO, String templateName) {
    log.info("Client Team notification send... Scheduler Type " + time);
    log.debug("Client Team notification details : type" + time + " Client Team details "
        + clientTeamDTO + " candidate details " + candidateDTO);
    String contain = mailService.getInterviewNotificationEmailContent(Constant.CLIENT_NOTIFICATION,
        candidateDTO, null, clientTeamDTO, time, null, templateName);
    sendMail(
        clientTeamDTO.getContact().getEmail() == null ? clientTeamDTO.getContact().getWorkEmail()
            : clientTeamDTO.getContact().getEmail(),
        mailSubject, contain);
  }

  @Override
  public void sendMail(String email, String mailSubject, String contain) {
    try {
      mailService.sendEmail(email, null, mailSubject, contain, null);
    } catch (Exception e) {
      throw new APIException("Error in send Email");
    }
  }

  @Override
  public String getInterViewDetails(String schedulerType) {
    LocalDateTime date = java.time.LocalDateTime.now();
    Date currentDateAndTime = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    Map<String, String> interviewNotificationReqMap =
        getScheduleDate(currentDateAndTime, schedulerType);

    JsonObject paramJSON = new JsonObject();
    paramJSON.addProperty(Constant.FROM_DATE, interviewNotificationReqMap.get(Constant.FROM_DATE));
    paramJSON.addProperty(Constant.TO_DATE, interviewNotificationReqMap.get(Constant.TO_DATE));

    return restUtil.postMethod(Constant.CANDIDATE_SUITE_INTERVIEW, paramJSON.toString(), null);

  }

  @Override
  public List<CandidateDTO> getCandidateListFromJsonResponse(JsonArray jsonArray, String team) {
    List<CandidateDTO> lisCandidateDTO = new ArrayList<>();
    for (JsonElement jsonElement : jsonArray) {
      JsonObject json = jsonElement.getAsJsonObject();
      CandidateDTO candidateDTO = new Gson().fromJson(json, new TypeToken<CandidateDTO>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());

      if (team.equals(Constant.KGP_TEAM)) {
        candidateDTO.getSearch().setPartners(addKgpTeamJsonArraytoList(json, "partners"));
        candidateDTO.getSearch().setRecruiters(addKgpTeamJsonArraytoList(json, "recruiters"));
        candidateDTO.getSearch().setResearchers(addKgpTeamJsonArraytoList(json, "researchers"));
        candidateDTO.getSearch().setEas(addKgpTeamJsonArraytoList(json, "eas"));
      } else {
        candidateDTO.getSearch().setClienTeam(addClientTeamJsonArraytoList(json, "client_team"));
      }
      lisCandidateDTO.add(candidateDTO);
    }
    return lisCandidateDTO;
  }

  @Override
  public List<UserDTO> addKgpTeamJsonArraytoList(JsonObject json, String listfor) {
    JsonArray partnerArray = json.getAsJsonObject("search").getAsJsonArray(listfor);
    List<UserDTO> partnerList = new ArrayList<>();
    partnerArray.forEach(e -> partnerList
        .add(new Gson().fromJson(e.getAsJsonObject().get("user"), new TypeToken<UserDTO>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;
        }.getType())));
    return partnerList;
  }

  @Override
  public List<ClientTeamDTO> addClientTeamJsonArraytoList(JsonObject json, String listfor) {
    JsonArray partnerArray = json.getAsJsonObject("search").getAsJsonArray(listfor);
    List<ClientTeamDTO> clientList = new ArrayList<>();

    partnerArray.forEach(q -> {
      ClientTeamDTO clientTeamDTO = new ClientTeamDTO();
      clientTeamDTO.setContact(
          new Gson().fromJson(q.getAsJsonObject().get("contact"), new TypeToken<ContactDTO>() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;
          }.getType()));
      clientList.add(clientTeamDTO);
    });
    return clientList;
  }

  @Override
  public Map<String, String> getScheduleDate(Date currentDateAndTime, String type) {
    Map<String, String> interviewNotificationReqMap = new HashMap<>();
    SimpleDateFormat formatter = new SimpleDateFormat(Constant.GALAXY_DATE_AND_TIME_FORMATTER);
    Calendar fromDateCalender = Calendar.getInstance();
    Calendar toDateCalender = Calendar.getInstance();
    fromDateCalender.setTime(currentDateAndTime);
    toDateCalender.setTime(currentDateAndTime);

    if (type.equals(Constant.BEFORE_ONE_DAY)) {
      fromDateCalender.add(Calendar.HOUR, 24);
      toDateCalender.add(Calendar.MINUTE, 2879);
    } else if (type.equals(Constant.BEFORE_ONE_HOUR)) {
      fromDateCalender.add(Calendar.HOUR, 1);
      toDateCalender.add(Calendar.MINUTE, 90);
    } else if (type.equals(Constant.AFTER_INTERVIEW)) {
      fromDateCalender.add(Calendar.MINUTE, -150);
      toDateCalender.add(Calendar.HOUR, -2);
    }

    interviewNotificationReqMap.put(Constant.FROM_DATE,
        formatter.format(fromDateCalender.getTime().getTime()));
    interviewNotificationReqMap.put(Constant.TO_DATE,
        formatter.format(toDateCalender.getTime().getTime()));
    return interviewNotificationReqMap;
  }

}
