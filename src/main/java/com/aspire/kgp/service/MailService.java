package com.aspire.kgp.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import com.aspire.kgp.dto.UserDTO;

import freemarker.template.TemplateException;

public interface MailService {
  public void sendEmail(String mailTo, String[] mailBcc, String mailSubject, String mailContent,
      List<Object> attachments) throws MessagingException, UnsupportedEncodingException;

  public String getEmailContent(HttpServletRequest request, UserDTO userDTO,
      Map<String, String> staticContentsMap, String templateName)
      throws IOException, TemplateException;

}
