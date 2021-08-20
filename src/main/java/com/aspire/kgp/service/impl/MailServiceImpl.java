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

  @Override
  public void sendEmail(String mailTo, String[] mailBcc, String mailSubject, String mailContent,
      List<Object> attachments) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try {
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

    } catch (MessagingException e) {
      log.info("MessagingException::");
    } catch (UnsupportedEncodingException e) {
      log.info("UnsupportedEncodingException::");
    }
  }

  @Override
  public String getInviteEmailContent(HttpServletRequest request, UserDTO user, String language)
      throws IOException, TemplateException {
    log.info("starting getInviteEmailContent");
    StringWriter stringWriter = new StringWriter();
    Map<String, Object> model = new HashMap<>();
    model.put("serverUrl", CommonUtil.getServerUrl(request) + request.getContextPath());
    model.put("homeUrl", "");
    model.put("name", user.getFirstName() + " " + user.getLastName());
    model.put("token", user.getToken());
    model.put("userEmail", user.getEmail());
    String languageCode = CommonUtil.getLanguageCode(language);
    model.put("staticContentsMap", StaticContentsMultiLanguageUtil
        .getStaticContentsMap(languageCode, Constant.EMAILS_CONTENT_MAP));
    configuration.getTemplate(Constant.CANDIDATE_INVITE_EMAIL_TEMPLATE).process(model,
        stringWriter);
    log.info("ending getInviteEmailContent");
    return stringWriter.getBuffer().toString();
  }

  @Override
  public String getForgotPasswordContent(HttpServletRequest request, UserDTO userDTO,
      String language) throws IOException, TemplateException {
    String homeUrl = "";
    String serverUrl = CommonUtil.getServerUrl(request) + request.getContextPath();
    return "<p>Dear "
        + userDTO.getFirstName() + " " + userDTO.getLastName()
        + ", You can Reset your password by clicking on  <a href=\"" + serverUrl + homeUrl
        + "?token=" + userDTO.getToken()
        + "\" target=\"_blank\" style=\"color:#5443d5; font-weight: bold; text-decoration:none;\">"
        + serverUrl + "</a>   Sincerely, Admin</p>";
  }

}
