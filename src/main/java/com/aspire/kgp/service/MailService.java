package com.aspire.kgp.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.ClientTeamDTO;
import com.aspire.kgp.dto.UserDTO;

import freemarker.template.TemplateException;

public interface MailService {
  public void sendEmail(String mailTo, String[] mailBcc, String mailSubject, String mailContent,
      List<Object> attachments) throws MessagingException, UnsupportedEncodingException;

  public String getEmailContent(HttpServletRequest request, UserDTO userDTO,
      Map<String, String> staticContentsMap, String templateName, CandidateDTO candidateDTO)
      throws IOException, TemplateException;

  public String getFeedbackEmailContent(HttpServletRequest request,
      Map<String, String> staticContentsMap, String candidateFeedbackEmailTemplate,
      String partnerName, Map<String, String> paramRequest, Boolean isReplyFeedback)
      throws IOException, TemplateException;

  public String getUploadEmailContent(HttpServletRequest request,
      Map<String, String> staticContentsMap, String candidateUploadEmailTemplate,
      String partnerName, Map<String, String> paramRequest) throws IOException, TemplateException;

  String getMyInfoUpdateEmailContent(HttpServletRequest request,
      Map<String, String> staticContentsMap, String candidateUploadEmailTemplate,
      String partnerName, Map<String, String> paramRequest,
      Map<String, Map<String, String>> changesMap) throws TemplateException, IOException;

  public String getInterviewNotificationEmailContent(String type, CandidateDTO candidateDTO,
      UserDTO userDTO, ClientTeamDTO clientTeamDTO, String time, String stage, String templateName);
}
