package com.aspire.kgp.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import com.aspire.kgp.dto.InterviewNotificationRequestDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.service.InterviewNotificationService;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.util.RestUtil;

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

    for (CandidateDTO candidateDTO : list) {
      String stage = candidateDTO.getStage();
      if (stage.equals("KGP")) {
        for (UserDTO user : candidateDTO.getSearch().getPartners()) {
          sendCandidateNotification(mailSubject, time, candidateDTO, user, null, stage,
              templateName);
          sendKgpPartnerNotification(mailSubject, time, candidateDTO, user, templateName);
        }
      } else {
        for (ClientTeamDTO clientTeam : candidateDTO.getSearch().getClienTeam()) {
          sendCandidateNotification(mailSubject, time, candidateDTO, null, clientTeam, stage,
              templateName);
          sendClientNotification(mailSubject, time, candidateDTO, clientTeam, templateName);
        }
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
    sendMail(candidateDTO.getContact().getEmail(), mailSubject, contain);

  }

  private void sendKgpPartnerNotification(String mailSubject, String time,
      CandidateDTO candidateDTO, UserDTO userDTO, String templateName) {
    log.info("KGP Partner notification send... candidate details " + candidateDTO);
    log.debug("KGP Partner notification details : type" + time + " kgp team details " + userDTO);
    String contain = mailService.getInterviewNotificationEmailContent(Constant.KGP_NOTIFICATION,
        candidateDTO, userDTO, null, time, null, templateName);
    sendMail(userDTO.getEmail(), mailSubject, contain);
  }



  private void sendClientNotification(String mailSubject, String time, CandidateDTO candidateDTO,
      ClientTeamDTO clientTeamDTO, String templateName) {
    log.info("Client Team notification send... candidate details " + candidateDTO);
    log.debug(
        "Client Team notification details : type" + time + " Client Team details " + clientTeamDTO);
    String contain = mailService.getInterviewNotificationEmailContent(Constant.CLIENT_NOTIFICATION,
        candidateDTO, null, clientTeamDTO, time, null, templateName);
    sendMail(clientTeamDTO.getContact().getEmail(), mailSubject, contain);
  }

  private void sendMail(String email, String mailSubject, String contain) {
    try {
      mailService.sendEmail(email, null, mailSubject, contain, null);
    } catch (Exception e) {
      throw new APIException("Error in send Email");
    }
  }


  @Override
  public List<CandidateDTO> getInterViewNotificationDetails(String schedulerType) {
    List<CandidateDTO> candidateFeedbackList = null;
    LocalDateTime date = java.time.LocalDateTime.now();
    Date currentDateAndTime = Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    getScheduleDate(currentDateAndTime, schedulerType);
    return candidateFeedbackList;
  }

  private InterviewNotificationRequestDTO getScheduleDate(Date currentDateAndTime, String type) {
    InterviewNotificationRequestDTO interviewNotificationRequestDTO =
        new InterviewNotificationRequestDTO();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    Calendar fromDateCalender = Calendar.getInstance();
    Calendar toDateCalender = Calendar.getInstance();
    fromDateCalender.setTime(currentDateAndTime);
    toDateCalender.setTime(currentDateAndTime);

    if (type.equals("ONE_DAY")) {
      fromDateCalender.add(Calendar.HOUR, 24);
      toDateCalender.add(Calendar.MINUTE, 2879);
    } else if (type.equals("ONE_HOUR")) {
      fromDateCalender.add(Calendar.HOUR, 1);
      toDateCalender.add(Calendar.MINUTE, 90);
    } else if (type.equals("FEEDBACK")) {
      fromDateCalender.add(Calendar.MINUTE, -150);
      toDateCalender.add(Calendar.HOUR, -2);
    }

    interviewNotificationRequestDTO
        .setFromDate(formatter.format(fromDateCalender.getTime().getTime()));
    interviewNotificationRequestDTO.setToDate(formatter.format(toDateCalender.getTime().getTime()));
    return interviewNotificationRequestDTO;
  }

}
