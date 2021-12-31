package com.aspire.kgp.service.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.ClientTeamDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.util.CommonUtil;
import com.aspire.kgp.util.StaticContentsMultiLanguageUtil;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Service
public class MailServiceImpl implements MailService {
  private static final Log log = LogFactory.getLog(MailServiceImpl.class);

  @Autowired
  JavaMailSender mailSender;

  @Autowired
  Configuration configuration;

  @Value("${galaxy.base.api.url}")
  private String baseApiUrl;

  @Value("${clientsuite.url}")
  private String clientsuiteUrl;

  @Value("${candidatesuite.url}")
  private String candidateSuiteUrl;

  @Override
  public void sendEmail(String mailTo, String[] mailBcc, String mailSubject, String mailContent,
      List<Object> attachments) throws MessagingException, UnsupportedEncodingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
    mimeMessageHelper.setSubject(mailSubject);
    mimeMessageHelper.setFrom(new InternetAddress(Constant.FROM_MAIL, Constant.SENDER_NAME));
    if (mailTo.equals("pritmatrix@gmail.com") || mailTo.equals("pratik.patel@aspiresoftware.in")) {
      mailTo = "poorav.solanki@aspiresoftserv.com";
    }
    mimeMessageHelper.setTo(mailTo);
    if (mailBcc != null && mailBcc.length > 0) {
      mimeMessageHelper.setBcc(mailBcc);
    }
    mimeMessageHelper.setText(mailContent, Boolean.TRUE);
    if (attachments != null && !attachments.isEmpty()) {
      // mimeMessageHelper.set
    }

    mailSender.send(mimeMessageHelper.getMimeMessage());
  }

  @Override
  public String getEmailContent(HttpServletRequest request, UserDTO user,
      Map<String, String> staticContentsMap, String templateName, CandidateDTO candidateDTO)
      throws IOException, TemplateException {
    log.info("starting getEmailContent");
    StringWriter stringWriter = new StringWriter();
    Map<String, Object> model = new HashMap<>();
    model.put("serverUrl", CommonUtil.getServerUrl(request) + request.getContextPath());
    model.put("homeUrl", "/login");
    model.put("name", user.getFirstName() + " " + user.getLastName());
    model.put("firstName", user.getFirstName());
    model.put("token", user.getToken());
    model.put("userEmail", user.getEmail());
    model.put("staticContentsMap", staticContentsMap);
    if (candidateDTO != null) {
      model.put("searchTitle", candidateDTO.getSearch().getJobTitle());
      model.put("companyName", candidateDTO.getSearch().getCompany().getName().trim());
    }
    configuration.getTemplate(templateName).process(model, stringWriter);
    log.info("ending getEmailContent");
    return stringWriter.getBuffer().toString();
  }

  @Override
  public String getFeedbackEmailContent(HttpServletRequest request,
      Map<String, String> staticContentsMap, String candidateFeedbackEmailTemplate,
      String partnerName, Map<String, String> paramRequest, Boolean isReplyFeedback)
      throws IOException, TemplateException {
    log.info("starting getEmailContent for Email feedback");
    StringWriter stringWriter = new StringWriter();
    Map<String, Object> model = new HashMap<>();
    model.put("serverUrl", CommonUtil.getServerUrl(request) + request.getContextPath());
    model.put("clientName", paramRequest.get("clientName"));
    model.put("partnerName", partnerName);
    model.put("searchName", paramRequest.get("searchName"));
    model.put("candidateName", paramRequest.get("candidateName"));
    model.put("companyName", paramRequest.get("companyName"));
    model.put("comment", paramRequest.get("feedback"));
    model.put("staticContentsMap", staticContentsMap);
    model.put("isReplyAdded", isReplyFeedback.toString());
    model.put("reply", paramRequest.get("reply"));
    model.put("replyButtonUrl",
        CommonUtil.getServerUrl(request) + request.getContextPath() + "/my-status/"
            + paramRequest.get("candidateId") + "/" + paramRequest.get("searchId") + "/"
            + paramRequest.get("searchName") + "/" + paramRequest.get("contactId") + "/" + "true"
            + "/" + paramRequest.get("commentId"));
    configuration.getTemplate(candidateFeedbackEmailTemplate).process(model, stringWriter);
    log.info("ending getEmailContent for Email feedback");
    return stringWriter.getBuffer().toString();
  }

  @Override
  public String getUploadEmailContent(HttpServletRequest request,
      Map<String, String> staticContentsMap, String candidateUploadEmailTemplate,
      String partnerName, Map<String, String> paramRequest) throws TemplateException, IOException {
    log.info("starting getEmailContent for notification Upload Documnets email");
    StringWriter stringWriter = new StringWriter();
    Map<String, Object> model = new HashMap<>();
    model.put("serverUrl", CommonUtil.getServerUrl(request) + request.getContextPath());
    model.put("clientName", paramRequest.get("clientName"));
    model.put("partnerName", partnerName);
    model.put("searchName", paramRequest.get("searchName"));
    model.put("candidateName", paramRequest.get("candidateName"));
    model.put("companyName", paramRequest.get("companyName"));
    model.put("file", paramRequest.get("type"));
    model.put("content", paramRequest.get("content"));
    model.put("access", paramRequest.get("access"));
    model.put("staticContentsMap", staticContentsMap);
    model.put("clickButtonUrl", paramRequest.get("clickButtonUrl"));
    configuration.getTemplate(candidateUploadEmailTemplate).process(model, stringWriter);
    log.info("ending getEmailContent for notification Upload Documnets email");
    return stringWriter.getBuffer().toString();

  }

  @Override
  public String getMyInfoUpdateEmailContent(HttpServletRequest request,
      Map<String, String> staticContentsMap, String candidateUploadEmailTemplate,
      String partnerName, Map<String, String> paramRequest,
      Map<String, Map<String, String>> changesMap) throws TemplateException, IOException {

    log.info("starting getEmailContent for notification Upload Documnets email");
    StringWriter stringWriter = new StringWriter();
    Map<String, String> currentInfoChanges = changesMap.get("CurrentInfo");
    Map<String, String> jobHistoryChanges = changesMap.get("jobHistory");
    Map<String, String> boardHistoryChanges = changesMap.get("boardHistory");
    Map<String, String> educationChanges = changesMap.get("education");

    Map<String, Object> model = new HashMap<>();
    model.put("serverUrl", CommonUtil.getServerUrl(request) + request.getContextPath());
    model.put("clientName", paramRequest.get("clientName"));
    model.put("partnerName", partnerName);
    model.put("searchName", paramRequest.get("searchName"));
    model.put("candidateName", paramRequest.get("candidateName"));
    model.put("companyName", paramRequest.get("companyName"));
    model.put("content", paramRequest.get("content"));
    model.put("access", paramRequest.get("access"));
    model.put("staticContentsMap", staticContentsMap);
    model.put("clickButtonUrl", paramRequest.get("clickButtonUrl"));

    boolean currentInfoChangesAvailable = currentInfoChanges.size() > 0;
    model.put("currentInfoChangesAvailable", currentInfoChangesAvailable);
    boolean jobHistoryChangesAvailable = jobHistoryChanges.size() > 0;
    model.put("jobHistoryChangesAvailable", jobHistoryChangesAvailable);
    boolean boardHistoryChangesAvailable = boardHistoryChanges.size() > 0;
    model.put("boardHistoryChangesAvailable", boardHistoryChangesAvailable);
    boolean educationChangesAvailable = educationChanges.size() > 0;
    model.put("educationChangesAvailable", educationChangesAvailable);

    model.put("currentInfoChanges", currentInfoChanges);
    model.put("jobHistoryChanges", jobHistoryChanges);
    model.put("boardHistoryChanges", boardHistoryChanges);
    model.put("educationChanges", educationChanges);

    configuration.getTemplate(candidateUploadEmailTemplate).process(model, stringWriter);
    log.info("ending getEmailContent for notification Upload Documnets email");
    return stringWriter.getBuffer().toString();
  }

  @Override
  public String getInterviewNotificationEmailContent(String notificationType,
      CandidateDTO candidateDTO, UserDTO userDTO, ClientTeamDTO clientTeamDTO, String schedulerType,
      String stage, String templateName) {

    log.debug("get Interview Notification Email Content...");
    StringWriter stringWriter = new StringWriter();
    Map<String, Object> model = new HashMap<>();
    if (schedulerType.equals(Constant.BEFORE_ONE_HOUR))
      model.put("time", "in an hour");
    else
      model.put("time", "tomorrow ");

    model.put(Constant.SERVER_URL, candidateSuiteUrl);
    model.put(Constant.HOME_URL, "/login");
    model.put(Constant.POSITION_TITLE, candidateDTO.getSearch().getJobTitle());
    model.put(Constant.FEEDBACK_NOTIFICATION_COMPANY_NAME,
        candidateDTO.getSearch().getCompany().getName());
    model.put(Constant.STATIC_CONTENT_MAP,
        StaticContentsMultiLanguageUtil.getStaticContentsMap("en_US", Constant.EMAILS_CONTENT_MAP));

    if (notificationType.equals(Constant.CANDIDATE_NOTIFICATION)) {
      model.put(Constant.CLICK_HERE, "Click Here");
      model.put(Constant.CLICK_HERE_MSG, "to access the login details.");
      model.put(Constant.CLICK_HERE_SERVER_URL, candidateSuiteUrl);
      model.put(Constant.NAME, candidateDTO.getContact().getFirstName());
      model.put(Constant.COMPANY_NAME, candidateDTO.getSearch().getCompany().getName() + ",");
      model.put(Constant.POSITION_TITLE_TYPE, "role");
      model.put(Constant.FEEDBACK_NOTIFICATION_CLICK_MSG, "Candidate Suite ");
      model.put(Constant.FEEDBACK_NOTIFICATION_CANDIDATE_NAME, "");
      model.put(Constant.FEEDBACK_NOTIFICATION_URL,
          Constant.CANDIDATE_SUITE_FEEDBACK_PAGE_URL
              .replace(Constant.CANDIDATE_ID_BRACES, candidateDTO.getId())
              .replace(Constant.SEARCH_ID_BRACES, candidateDTO.getSearch().getId())
              .replace(Constant.SEARCH_TITLE_BRACES, candidateDTO.getSearch().getJobTitle())
              .replace(Constant.CONTACT_ID, candidateDTO.getContact().getId())
              .replaceAll(Constant.SPACE_STRING, "%20"));
      if (stage.equals(Constant.KGP_TEAM))
        model.put(Constant.CANDIDATE_NAME, userDTO.getName());
      else
        model.put(Constant.CANDIDATE_NAME, clientTeamDTO.getContact().getFirstName() + " "
            + clientTeamDTO.getContact().getLastName());

    } else if (notificationType.equals(Constant.CLIENT_NOTIFICATION)) {
      model.put(Constant.CLICK_HERE, "Click Here");
      model.put(Constant.CLICK_HERE_MSG, "to access their details in Client Suite.");
      model.put(Constant.CLICK_HERE_SERVER_URL, clientsuiteUrl);
      model.put(Constant.NAME, clientTeamDTO.getContact().getFirstName());
      model.put(Constant.CANDIDATE_NAME,
          candidateDTO.getContact().getFirstName() + " " + candidateDTO.getContact().getLastName());
      model.put(Constant.COMPANY_NAME, "");
      model.put(Constant.POSITION_TITLE_TYPE, "role");
      model.put(Constant.FEEDBACK_NOTIFICATION_CLICK_MSG, "ClientSuite ");
      model.put(Constant.FEEDBACK_NOTIFICATION_URL, "");
      model.put(Constant.FEEDBACK_NOTIFICATION_CANDIDATE_NAME,
          " with <b>" + candidateDTO.getContact().getFirstName() + " "
              + candidateDTO.getContact().getLastName() + "</b>");
    } else {
      model.put(Constant.CLICK_HERE, "");
      model.put(Constant.CLICK_HERE_MSG, "");
      model.put(Constant.CLICK_HERE_SERVER_URL, clientsuiteUrl);
      model.put(Constant.NAME, userDTO.getFirstName());
      model.put(Constant.CANDIDATE_NAME,
          candidateDTO.getContact().getFirstName() + " " + candidateDTO.getContact().getLastName());
      model.put(Constant.COMPANY_NAME, candidateDTO.getSearch().getCompany().getName() + ",");
      model.put(Constant.POSITION_TITLE_TYPE, "search");
      model.put(Constant.FEEDBACK_NOTIFICATION_CLICK_MSG, "ClientSuite ");
      model.put(Constant.FEEDBACK_NOTIFICATION_URL, "");
      model.put(Constant.FEEDBACK_NOTIFICATION_CANDIDATE_NAME,
          " with <b>" + candidateDTO.getContact().getFirstName() + " "
              + candidateDTO.getContact().getLastName() + "</b>");
    }

    try {
      configuration.getTemplate(templateName).process(model, stringWriter);
    } catch (TemplateException | IOException e) {
      e.printStackTrace();
    }
    log.debug("Successfully Get Interview Notification Email Content");
    return stringWriter.getBuffer().toString();

  }
}
