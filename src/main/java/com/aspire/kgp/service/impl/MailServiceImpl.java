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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.util.CommonUtil;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Service
public class MailServiceImpl implements MailService {
  private static final Log log = LogFactory.getLog(MailServiceImpl.class);

  @Autowired
  JavaMailSender mailSender;

  @Autowired
  Configuration configuration;

  @Override
  public void sendEmail(String mailTo, String[] mailBcc, String mailSubject, String mailContent,
      List<Object> attachments) throws MessagingException, UnsupportedEncodingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

    mimeMessageHelper.setSubject(mailSubject);
    mimeMessageHelper.setFrom(new InternetAddress(Constant.FROM_MAIL, Constant.SENDER_NAME));
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
      Map<String, String> staticContentsMap, String templateName)
      throws IOException, TemplateException {
    log.info("starting getEmailContent");
    StringWriter stringWriter = new StringWriter();
    Map<String, Object> model = new HashMap<>();
    model.put("serverUrl", CommonUtil.getServerUrl(request) + request.getContextPath());
    model.put("homeUrl", "/login");
    model.put("name", user.getFirstName() + " " + user.getLastName());
    model.put("token", user.getToken());
    model.put("userEmail", user.getEmail());
    model.put("staticContentsMap", staticContentsMap);
    configuration.getTemplate(templateName).process(model, stringWriter);
    log.info("ending getEmailContent");
    return stringWriter.getBuffer().toString();
  }

  @Override
  public String getFeedbackEmailContent(HttpServletRequest request, UserDTO userDTO,
      Map<String, String> staticContentsMap, String templateName)
      throws IOException, TemplateException {
    log.info("starting getEmailContent for feedback");
    StringWriter stringWriter = new StringWriter();
    Map<String, Object> model = new HashMap<>();
    model.put("serverUrl", CommonUtil.getServerUrl(request) + request.getContextPath());
    model.put("clientName", "Pratik Patel");
    model.put("partnerName", "Poorav Solanki");
    model.put("searchName", "Full Stack Developer & Analyzer");
    model.put("clientContactName", "Abhishek's ");
    model.put("candidateName", "Abhishek Jaiswal");
    model.put("companyName", "zAspire Software Solutions");
    model.put("comment", " Feedback added By Abhishek Jaiswal");
    model.put("staticContentsMap", staticContentsMap);
    model.put("isReplyAdded", "true");
    model.put("reply", "Hi THere Replying here.");
    configuration.getTemplate(templateName).process(model, stringWriter);
    log.info("ending getEmailContent for feedback");
    return stringWriter.getBuffer().toString();
  }
}