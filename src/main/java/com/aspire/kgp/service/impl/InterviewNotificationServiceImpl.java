package com.aspire.kgp.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.ClientTeamDTO;
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.dto.InterviewNotificationRequestDTO;
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
  public void sendNotification(List<CandidateDTO> list, String time) {
    log.info("staring email sending...");
    String mailSubject;
    String templateName;
    if (time.equals(Constant.BEFORE_ONE_HOUR)) {
      mailSubject = "REMINDER: Upcoming Interview starts 1 hour";
      templateName = Constant.INTERVIEW_NOTIFICATION_TEMPLATE;
    } else if (time.equals(Constant.BEFORE_ONE_DAY)) {
      mailSubject = "REMINDER: Upcoming Interview starts 1 day";
      templateName = Constant.INTERVIEW_NOTIFICATION_TEMPLATE;
    } else {
      mailSubject = "Interview Feedback";
      templateName = Constant.INTERVIEW_FEEDBACK_NOTIFICATION_TEMPLATE;
    }


    // String apiResponse = getInterViewNotificationDetails(time);
    String apiResponse = "{ clientinterviews: [{\r\n"
        + "    \"id\": \"26fa66e2-670d-4aac-947c-26fb128fc2e8\",\r\n"
        + "    \"stage\": \"Client Interview\",\r\n" + "    \"contact\": {\r\n"
        + "      \"id\": \"85dd22a9-5c7f-4f81-a392-4180851f54ff\",\r\n"
        + "      \"first_name\": \"Jorge\",\r\n" + "      \"last_name\": \"Muñoz\"\r\n"
        + "    },\r\n" + "    \"search\": {\r\n"
        + "      \"id\": \"70fcd549-5971-4d9e-813c-cf53fc9d9aa9\",\r\n"
        + "      \"job_title\": \"Director Ejecutivo\",\r\n" + "      \"job_number\": 42047,\r\n"
        + "      \"company\": {\r\n"
        + "        \"id\": \"9b9a6c20-4954-42e4-8580-61cb925d00db\",\r\n"
        + "        \"name\": \"Grupo Educativo Transforma\"\r\n" + "      },\r\n"
        + "      \"client_team\": [\r\n" + "        {\r\n"
        + "          \"id\": \"5f9341a2-004b-4943-8c08-287483b9ba5e\",\r\n"
        + "          \"search_id\": \"70fcd549-5971-4d9e-813c-cf53fc9d9aa9\",\r\n"
        + "          \"is_hiring_manager\": null,\r\n"
        + "          \"clientsuite_invitation_sent_on\": null,\r\n"
        + "          \"position\": 3,\r\n"
        + "          \"created_at\": \"2020-12-17T13:57:25.428Z\",\r\n"
        + "          \"updated_at\": \"2020-12-17T13:57:25.440Z\",\r\n"
        + "          \"contact\": {\r\n"
        + "            \"id\": \"232c9fdf-23e2-4059-af16-edf927cfa0d8\",\r\n"
        + "            \"first_name\": \"Alejandro\",\r\n"
        + "            \"last_name\": \"Desmaison Fernandini\",\r\n"
        + "            \"mobile_phone\": \"+51 997 501 250\",\r\n"
        + "            \"work_email\": \"adesmaison@franquiciasperu.com\"\r\n" + "          }\r\n"
        + "        }\r\n" + "      ]\r\n" + "    }\r\n" + "  }],\r\n" + " kgpinterviews: [\r\n"
        + "  {\r\n" + "    \"id\": \"2f6721c5-02fa-4f41-8675-e7449d22951d\",\r\n"
        + "    \"stage\": \"Client Interview\",\r\n" + "    \"contact\": {\r\n"
        + "      \"id\": \"ff58de87-dc7c-4c9e-a31a-4227b44515b2\",\r\n"
        + "      \"first_name\": \"sa\",\r\n" + "      \"last_name\": \"cghg\"\r\n" + "    },\r\n"
        + "    \"search\": {\r\n" + "      \"id\": \"b54e8ad3-7f36-45dd-a829-fb550e29f098\",\r\n"
        + "      \"job_title\": \"mike\",\r\n" + "      \"job_number\": 12,\r\n"
        + "      \"company\": {\r\n"
        + "        \"id\": \"b8b57530-f5b9-4c73-af03-508ca2dc48af\",\r\n"
        + "        \"name\": \"EverestEngineering\"\r\n" + "      },\r\n"
        + "      \"partners\": [\r\n" + "        {\r\n"
        + "          \"id\": \"c1bb9fdd-8fc8-407f-9fbd-2672da4465d9\",\r\n"
        + "          \"search_id\": \"b54e8ad3-7f36-45dd-a829-fb550e29f098\",\r\n"
        + "          \"origination_credit\": null,\r\n" + "          \"selling_credit\": null,\r\n"
        + "          \"execution_credit\": null,\r\n" + "          \"position\": 1,\r\n"
        + "          \"created_at\": \"2021-10-26T05:52:55.296Z\",\r\n"
        + "          \"updated_at\": \"2021-10-26T05:52:55.342Z\",\r\n"
        + "          \"user\": {\r\n" + "            \"name\": \"Itachi Uchiha\",\r\n"
        + "            \"id\": \"4adf43ef-367c-4e84-9748-e756d84ba377\",\r\n"
        + "            \"first_name\": \"Itachi\",\r\n"
        + "            \"last_name\": \"Uchiha\",\r\n"
        + "            \"email\": \"Dummy@gmail.com\"\r\n" + "          }\r\n" + "        }\r\n"
        + "      ],\r\n" + "      \"recruiters\": []\r\n" + "    }\r\n" + "  }\r\n" + "]\r\n" + "}";

    JsonObject jsonObjects = (JsonObject) JsonParser.parseString(apiResponse);
    JsonArray kgpTeamJsonArray = jsonObjects.getAsJsonArray("kgpinterviews");
    JsonArray clientJsonArray = jsonObjects.getAsJsonArray("clientinterviews");


    List<CandidateDTO> kgpInterviewCandidateList =
        getCandidateListFromJsonResponse(kgpTeamJsonArray, "KGP_TEAM");
    List<CandidateDTO> clientInterviewCandidateList =
        getCandidateListFromJsonResponse(clientJsonArray, "CLIENT_TEAM");


    for (CandidateDTO candidateDTO : kgpInterviewCandidateList) {
      for (UserDTO user : candidateDTO.getSearch().getPartners()) {
        sendCandidateNotification(mailSubject, time, candidateDTO, user, null, "KGP", templateName);
        sendKgpPartnerNotification(mailSubject, time, candidateDTO, user, templateName);
      }
    }
    for (CandidateDTO candidateDTO : clientInterviewCandidateList) {
      for (ClientTeamDTO clientTeam : candidateDTO.getSearch().getClienTeam()) {
        sendCandidateNotification(mailSubject, time, candidateDTO, null, clientTeam, "CLIENT",
            templateName);
        sendClientNotification(mailSubject, time, candidateDTO, clientTeam, templateName);
      }
    }
  }


  private void sendCandidateNotification(String mailSubject, String time, CandidateDTO candidateDTO,
      UserDTO userDTO, ClientTeamDTO clientTeamDTO, String stage, String templateName) {
    log.info("candidate notification send... candidate details " + candidateDTO);
    log.debug("candidate notification details : type" + time + " Stage " + stage
        + " kgp team details " + userDTO + " client details " + clientTeamDTO);
    String contain =
        mailService.getInterviewNotificationEmailContent(Constant.CANDIDATE_NOTIFICATION,
            candidateDTO, userDTO, clientTeamDTO, time, stage, templateName);
    sendMail("vraj.patel@aspiresoftserv.com", mailSubject, contain);

  }

  private void sendKgpPartnerNotification(String mailSubject, String time,
      CandidateDTO candidateDTO, UserDTO userDTO, String templateName) {
    log.info("KGP Partner notification send... candidate details " + candidateDTO);
    log.debug("KGP Partner notification details : type" + time + " kgp team details " + userDTO);
    String contain = mailService.getInterviewNotificationEmailContent(Constant.KGP_NOTIFICATION,
        candidateDTO, userDTO, null, time, null, templateName);
    sendMail("vraj.patel@aspiresoftserv.com", mailSubject, contain);
  }



  private void sendClientNotification(String mailSubject, String time, CandidateDTO candidateDTO,
      ClientTeamDTO clientTeamDTO, String templateName) {
    log.info("Client Team notification send... candidate details " + candidateDTO);
    log.debug(
        "Client Team notification details : type" + time + " Client Team details " + clientTeamDTO);
    String contain = mailService.getInterviewNotificationEmailContent(Constant.CLIENT_NOTIFICATION,
        candidateDTO, null, clientTeamDTO, time, null, templateName);
    sendMail("vraj.patel@aspiresoftserv.com", mailSubject, contain);
  }

  private void sendMail(String email, String mailSubject, String contain) {
    try {
      mailService.sendEmail(email, null, mailSubject, contain, null);
    } catch (Exception e) {
      throw new APIException("Error in send Email");
    }
  }


  @Override
  public String getInterViewNotificationDetails(String schedulerType) {
    List<CandidateDTO> candidateFeedbackList = null;
    LocalDateTime date = java.time.LocalDateTime.now();
    Date currentDateAndTime = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    getScheduleDate(currentDateAndTime, schedulerType);

    String apiResponse = restUtil.newGetMethod(
        Constant.CANDIDATE_LIST_URL.replace("{searchId}", "a1882a04-8cee-4f6a-9c75-48d03d61728d"));
    return apiResponse;
  }



  private List<CandidateDTO> getCandidateListFromJsonResponse(JsonArray jsonArray, String team) {
    List<CandidateDTO> lisCandidateDTO = new ArrayList<>();
    for (JsonElement jsonElement : jsonArray) {
      JsonObject json = jsonElement.getAsJsonObject();
      CandidateDTO candidateDTO = new Gson().fromJson(json, new TypeToken<CandidateDTO>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());

      if (team.equals("KGP_TEAM")) {
        candidateDTO.getSearch().setPartners(addKgpTeamJsonArraytoList(json, "partners"));
        candidateDTO.getSearch().setRecruiters(addKgpTeamJsonArraytoList(json, "recruiters"));
      } else {
        candidateDTO.getSearch().setClienTeam(addClientTeamJsonArraytoList(json, "client_team"));
      }
      lisCandidateDTO.add(candidateDTO);
    }
    return lisCandidateDTO;
  }

  private List<UserDTO> addKgpTeamJsonArraytoList(JsonObject json, String listfor) {
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

  private List<ClientTeamDTO> addClientTeamJsonArraytoList(JsonObject json, String listfor) {
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

  private InterviewNotificationRequestDTO getScheduleDate(Date currentDateAndTime, String type) {
    InterviewNotificationRequestDTO interviewNotificationRequestDTO =
        new InterviewNotificationRequestDTO();
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

    interviewNotificationRequestDTO
        .setFromDate(formatter.format(fromDateCalender.getTime().getTime()));
    interviewNotificationRequestDTO.setToDate(formatter.format(toDateCalender.getTime().getTime()));
    return interviewNotificationRequestDTO;
  }

}
