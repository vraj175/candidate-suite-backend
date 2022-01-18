package com.aspire.kgp.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.UserVideo;
import com.aspire.kgp.repository.UserVideoRepository;
import com.aspire.kgp.service.CandidateService;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.service.UserSearchService;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.UserVideoService;
import com.aspire.kgp.service.WebSocketNotificationService;
import com.aspire.kgp.util.CommonUtil;
import com.aspire.kgp.util.RestUtil;
import com.aspire.kgp.util.StaticContentsMultiLanguageUtil;
import com.google.gson.JsonSyntaxException;

@Service
public class UserVideoServiceImpl implements UserVideoService {
  static final Log log = LogFactory.getLog(UserVideoServiceImpl.class);

  @Value("${s3Bucket.url}")
  private String s3BucketbaseApiUrl;

  @Autowired
  RestUtil restUtil;

  @Autowired
  UserVideoRepository repository;

  @Autowired
  UserSearchService searchService;

  @Autowired
  MailService mailService;

  @Autowired
  CandidateService candidateService;

  @Autowired
  UserService userService;

  @Autowired
  WebSocketNotificationService webSocketNotificationService;

  @Value("${galaxy.base.api.url}")
  private String baseApiUrl;

  @Override
  public UserVideo saveorUpdate(UserVideo userVideo) {
    return repository.save(userVideo);
  }

  @Override
  public UserVideo addContactVideo(String contactId, String fileToken, String candidateId,
      HttpServletRequest request) {
    UserVideo userVideo = new UserVideo();
    userVideo.setContactId(contactId);
    userVideo.setFileToken(fileToken);
    sentUploadNotification(contactId, request, candidateId, "Video");
    return saveorUpdate(userVideo);
  }

  @Override
  public List<UserVideo> findByContactId(String contactId) {
    return repository.findByContactIdAndIsDeletedFalseOrderByCreatedDateDesc(contactId);
  }

  @Override
  public int getS3BucketVideoStatusCode(String videoToken) {
    GetMethod get = new GetMethod(
        s3BucketbaseApiUrl + "/" + videoToken.replaceAll(Constant.SPACE_STRING, "%20"));
    try {
      log.info("Request time: " + new Date());
      new HttpClient().executeMethod(get);
      log.info(get.getStatusCode());
      return get.getStatusCode();

    } catch (IOException e) {
      log.error("error " + e);
    } finally {
      get.releaseConnection();
    }
    return 0;
  }

  private void sentUploadNotification(String contactId, HttpServletRequest request,
      String candidateId, String type) {
    Set<String> kgpPartnerEmailList = new HashSet<>();
    HashMap<String, String> paramRequest = new HashMap<>();
    CandidateDTO apiResponse = candidateService.getCandidateDetails(candidateId);
    try {
      kgpPartnerEmailList = CommonUtil.teamPartnerMemberList(apiResponse.getSearch().getPartners(),
          kgpPartnerEmailList);
      kgpPartnerEmailList =
          CommonUtil.teamMemberList(apiResponse.getSearch().getRecruiters(), kgpPartnerEmailList);
      paramRequest.put("candidateName",
          apiResponse.getContact().getFirstName() + " " + apiResponse.getContact().getLastName());
      paramRequest.put("searchId", apiResponse.getSearch().getId());
      paramRequest.put("clientName",
          apiResponse.getContact().getFirstName() + " " + apiResponse.getContact().getLastName());
      paramRequest.put("searchName", apiResponse.getSearch().getJobTitle());
      paramRequest.put("companyName", apiResponse.getSearch().getCompany().getName());
      paramRequest.put("candidateId", candidateId);
      paramRequest.put("contactId", contactId);
      paramRequest.put("type", type);
    } catch (JsonSyntaxException e) {
      log.error("oops ! invalid json");
      throw new JsonSyntaxException("error while get team member");
    }
    try {
      for (String kgpTeamMeberDetails : kgpPartnerEmailList) {
        log.info("Partner Email : " + kgpTeamMeberDetails);
        sendClientUploadNotificationMail(kgpTeamMeberDetails.split("##")[0],
            kgpTeamMeberDetails.split("##")[1], request, paramRequest,
            kgpTeamMeberDetails.split("##")[2], contactId);
      }
    } catch (Exception ex) {
      log.info(ex);
      throw new APIException("Error in send upload notification email");
    }

  }


  private void sendClientUploadNotificationMail(String email, String partnerName,
      HttpServletRequest request, HashMap<String, String> paramRequest, String kgpTeamId,
      String contactId) {
    log.info("sending client upload notification email");
    String locate = "en_US";
    UserDTO userDTO = null;
    String content = "";
    String access = "";
    User user = (User) request.getAttribute("user");
    String role = user.getRole().getName();
    paramRequest.put("role", role);
    try {
      Map<String, String> staticContentsMap =
          StaticContentsMultiLanguageUtil.getStaticContentsMap(locate, Constant.EMAILS_CONTENT_MAP);
      String mailSubject = staticContentsMap.get("candidate.suite.upload.email.subject");
      if (Constant.PARTNER.equalsIgnoreCase(role)) {
        userDTO = userService.getGalaxyUserDetails(user.getGalaxyId());
        mailSubject = mailSubject + " - " + paramRequest.get("type") + " " + "Uploaded from "
            + userDTO.getFirstName() + " " + userDTO.getLastName();
        content = userDTO.getFirstName() + " " + userDTO.getLastName() + " has uploaded "
            + paramRequest.get("candidateName") + "'s";
        paramRequest.put("content", content);
        paramRequest.put("clientName", userDTO.getFirstName() + " " + userDTO.getLastName());
        paramRequest.put("clickButtonUrl",
            baseApiUrl.replace("api", "contacts") + "/" + paramRequest.get("contactId"));
        access = " to access in Galaxy";
        paramRequest.put("access", access);

      } else {
        userDTO = userService.getContactDetails(user.getGalaxyId());
        mailSubject = mailSubject + " - " + paramRequest.get("type") + " " + "Uploaded from "
            + paramRequest.get("candidateName");
        content = paramRequest.get("candidateName") + " has uploaded their ";
        paramRequest.put("clickButtonUrl",
            baseApiUrl.replace("api", "contacts") + "/" + paramRequest.get("contactId"));
        access = " to access in Galaxy";
        paramRequest.put("access", access);
        paramRequest.put("content", content);
        paramRequest.put("clientName", paramRequest.get("candidateName"));

      }
      mailService.sendEmail(email, null, mailSubject, mailService.getUploadEmailContent(request,
          staticContentsMap, Constant.CANDIDATE_UPLOAD_EMAIL_TEMPLATE, partnerName, paramRequest),
          null);
      webSocketNotificationService.sendWebSocketNotification(kgpTeamId, contactId,
          Constant.CONTACT_VIDEO_UPLOADED, Constant.PARTNER);
    } catch (Exception e) {
      log.info(e);
      throw new APIException("Error in sending candidate upload email");
    }
    log.info("Client upload Mail sent to all partners successfully.");
  }
}
