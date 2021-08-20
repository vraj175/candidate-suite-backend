package com.aspire.kgp.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.aspire.kgp.dto.UserDTO;

import freemarker.template.TemplateException;

public interface MailService {
  public void sendEmail(String mailTo, String[] mailBcc, String mailSubject, String mailContent,
      List<Object> attachments);

  public String getInviteEmailContent(HttpServletRequest request, UserDTO userDTO, String language)
      throws IOException, TemplateException;
  
  public String getForgotPasswordContent(HttpServletRequest request, UserDTO userDTO, String language)
      throws IOException, TemplateException;

}
