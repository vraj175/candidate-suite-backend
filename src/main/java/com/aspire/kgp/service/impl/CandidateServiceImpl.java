package com.aspire.kgp.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CandidateFeedbackDTO;
import com.aspire.kgp.dto.CandidateFeedbackReplyDTO;
import com.aspire.kgp.dto.CompanyDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.service.CandidateService;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.WebSocketNotificationService;
import com.aspire.kgp.util.CommonUtil;
import com.aspire.kgp.util.RestUtil;
import com.aspire.kgp.util.StaticContentsMultiLanguageUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class CandidateServiceImpl implements CandidateService {
  static Log log = LogFactory.getLog(CandidateServiceImpl.class.getName());

  @Autowired
  RestUtil restUtil;

  @Autowired
  MailService mailService;

  @Autowired
  UserService userService;

  @Value("${clientsuite.url}")
  private String clientsuiteUrl;

  @Autowired
  WebSocketNotificationService webSocketNotificationService;

  @Override
  public CandidateDTO getCandidateDetails(String candidateId) {
    String apiResponse = restUtil
        .newGetMethod(Constant.CONDIDATE_URL.replace(Constant.CANDIDATE_ID_BRACES, candidateId));
    JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
    CandidateDTO candidateDTO =
        new Gson().fromJson(json.get("candidate"), new TypeToken<CandidateDTO>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;
        }.getType());
    if (candidateDTO == null) {
      throw new APIException("Invalid Candidate Id");
    }
    candidateDTO.setId(candidateId);
    candidateDTO.getSearch().setPartners(addJsonArraytoList(json, "partners"));
    candidateDTO.getSearch().setRecruiters(addJsonArraytoList(json, "recruiters"));
    candidateDTO.getSearch().setResearchers(addJsonArraytoList(json, "researchers"));
    candidateDTO.getSearch().setEas(addJsonArraytoList(json, "eas"));
    return candidateDTO;
  }

  private List<UserDTO> addJsonArraytoList(JsonObject json, String listfor) {
    JsonArray partnerArray =
        json.getAsJsonObject("candidate").getAsJsonObject("search").getAsJsonArray(listfor);
    List<UserDTO> partnerList = new ArrayList<>();
    List<CandidateDTO> executionCreditList = new ArrayList<>();
    partnerArray.forEach(e -> partnerList
        .add(new Gson().fromJson(e.getAsJsonObject().get("user"), new TypeToken<UserDTO>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;
        }.getType())));
    partnerArray.forEach(e -> executionCreditList
        .add(new Gson().fromJson(e.getAsJsonObject(), new TypeToken<CandidateDTO>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;
        }.getType())));
    for (int i = 0; i < partnerList.size(); i++) {
      partnerList.get(i).setExecutionCredit(executionCreditList.get(i).getExecutionCredit());
    }
    return partnerList;
  }

  @Override
  public ResponseEntity<byte[]> getAthenaReport(String pageSize, String locale, String contactId) {
    byte[] byteContent = new byte[1024];
    int contentLength = 0;
    String fileName = null;
    String dispositionRes = null;
    int responseCode = 0;
    try {
      URL url = new URL(clientsuiteUrl + Constant.ATHENA_REPORT_URL.replace("{pageSize}", pageSize)
          .replace("{locale}", locale).replace("{contactId}", contactId));

      HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
      responseCode = httpConn.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        InputStream connInputStream = httpConn.getInputStream();
        dispositionRes = httpConn.getHeaderField("Content-Disposition");
        contentLength = httpConn.getContentLength();
        byteContent = downloadFile(url.openStream());
        connInputStream.close();
        httpConn.disconnect();
      }
    } catch (IOException e) {
      throw new APIException("Error While creating PDF");
    }

    if (responseCode != HttpURLConnection.HTTP_OK) {
      throw new APIException("Error while fetching Report from Bsuite.");
    }
    if (CommonUtil.checkNullString(dispositionRes)) {
      throw new NotFoundException("Conatct Id is wrong or not available.");
    }
    fileName = dispositionRes.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentLength(contentLength);
    ContentDisposition contentDisposition =
        ContentDisposition.builder("attachment").filename(fileName).build();
    headers.setContentDisposition(contentDisposition);
    return new ResponseEntity<>(byteContent, headers, HttpStatus.OK);
  }

  private byte[] downloadFile(InputStream connectionInput) {
    BufferedInputStream bis = new BufferedInputStream(connectionInput);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
      byte[] buffer = new byte[1024];

      int count = 0;
      while ((count = bis.read(buffer, 0, 1024)) != -1) {
        os.write(buffer, 0, count);
      }
    } catch (Exception e) {
      throw new APIException("Error While Downloading PDF from bytes");
    }
    return os.toByteArray();
  }

  @Override
  public String addCandidateFeedback(String candidateId, String comments, String galaxyId,
      HttpServletRequest request, boolean isReplyFeedback,
      CandidateFeedbackDTO candidateFeedbackDTO, String type) {
    log.info("Saving new feedback");
    JsonObject paramJSON = new JsonObject();
    HashMap<String, String> paramRequest = new HashMap<>();
    String feedbackId = "";
    paramJSON.addProperty("comments", comments);
    if (type.equals("candidate")) {
      paramJSON.addProperty("createdBy", candidateId);
      paramJSON.addProperty("type", "Candidate");
    } else if (type.equals("partner")) {
      paramJSON.addProperty("createdBy", galaxyId);
      paramJSON.addProperty("type", "User");
    }
    Set<String> kgpPartnerEmailList = new HashSet<>();
    CandidateDTO apiResponse = getCandidateDetails(candidateId);
    try {
      kgpPartnerEmailList = CommonUtil.teamPartnerMemberList(apiResponse.getSearch().getPartners(),
          kgpPartnerEmailList);
      kgpPartnerEmailList =
          CommonUtil.teamMemberList(apiResponse.getSearch().getRecruiters(), kgpPartnerEmailList);;
      kgpPartnerEmailList =
          CommonUtil.teamMemberList(apiResponse.getSearch().getEas(), kgpPartnerEmailList);
      paramRequest.put("feedback", comments);
      paramRequest.put("candidateName",
          apiResponse.getContact().getFirstName() + " " + apiResponse.getContact().getLastName());
      paramRequest.put("searchId", apiResponse.getSearch().getId());
      paramRequest.put("clientName",
          apiResponse.getContact().getFirstName() + " " + apiResponse.getContact().getLastName());
      paramRequest.put("searchName", apiResponse.getSearch().getJobTitle());
      paramRequest.put("companyName", apiResponse.getSearch().getCompany().getName());
      paramRequest.put("contactId", apiResponse.getContact().getId());
      paramRequest.put("candidateId", candidateId);
      if (CommonUtil.checkNullString(comments)
          || CommonUtil.checkNullString(paramRequest.get("candidateName"))
          || CommonUtil.checkNullString(paramRequest.get("searchId"))
          || CommonUtil.checkNullString(paramRequest.get("searchName"))
          || CommonUtil.checkNullString(paramRequest.get("companyName"))
          || CommonUtil.checkNullString(paramRequest.get("contactId"))
          || CommonUtil.checkNullString(paramRequest.get("candidateId"))
          || CommonUtil.checkNullString(paramRequest.get("clientName"))) {
        return "parameters are not valid to sent feedback mail";
      }
      if (isReplyFeedback) {
        if (candidateFeedbackDTO != null) {
          CandidateFeedbackReplyDTO candidateFeedbackReplyDTO = candidateFeedbackDTO.getReplies()
              .stream().filter(e -> e.getCommentId().equals(candidateFeedbackDTO.getId()))
              .findFirst().orElse(new CandidateFeedbackReplyDTO());
          isReplyFeedback = Boolean.TRUE;
          paramRequest.put("reply", candidateFeedbackReplyDTO.getReply());
          paramRequest.put("feedback", candidateFeedbackDTO.getComments());
          paramRequest.put("commentId", candidateFeedbackReplyDTO.getCommentId());
        }
      } else {
        feedbackId = restUtil.postMethod(
            Constant.CANDIDATE_FEEDBACK_URL.replace(Constant.CANDIDATE_ID_BRACES, candidateId),
            paramJSON.toString(), null);
        if (feedbackId != null && feedbackId.contains("id")) {
          log.info("Feedback added succefully with commentId:- " + feedbackId);
          CompanyDTO commentId = new Gson().fromJson(feedbackId, new TypeToken<CompanyDTO>() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;
          }.getType());
          paramRequest.put("commentId", commentId.getId());
        }
      }
    } catch (APIException e2) {
      log.error("error while adding feedback" + e2);
      throw new APIException("error while add feedback");
    } catch (JsonSyntaxException e) {
      log.error("oops ! invalid json");
      throw new JsonSyntaxException("error while get team member");
    }
    addCandidateFeedbackNotification(kgpPartnerEmailList, request, paramRequest, isReplyFeedback,
        type);
    return feedbackId;
  }

  private void addCandidateFeedbackNotification(Set<String> kgpPartnerEmailList,
      HttpServletRequest request, HashMap<String, String> paramRequest, boolean isReplyFeedback,
      String type) {
    try {

      for (String kgpTeamMeberDetails : kgpPartnerEmailList) {
        log.info("Partner Email : " + kgpTeamMeberDetails);
        sendClientFeedbackMail(kgpTeamMeberDetails.split("##")[0],
            kgpTeamMeberDetails.split("##")[1], paramRequest, request, isReplyFeedback,
            kgpTeamMeberDetails.split("##")[2], type);
      }
      if (type.equals(Constant.PARTNER)) {
        String feedbackUrl = CommonUtil.getServerUrl(request) + request.getContextPath()
            + "/my-status/" + paramRequest.get("candidateId") + "/" + paramRequest.get("searchId")
            + "/" + paramRequest.get("searchName") + "/" + paramRequest.get("contactId") + "/"
            + "true" + "/" + paramRequest.get("commentId");
        feedbackUrl = feedbackUrl.replaceAll(Constant.SPACE_STRING, "%20");
        webSocketNotificationService.sendWebSocketNotification(null, paramRequest.get("contactId"),
            Boolean.TRUE.equals(isReplyFeedback)
                ? Constant.PARTNER_FEEDBACK_REPLY_COMMENT + "##" + paramRequest.get("searchName")
                    + "##" + feedbackUrl
                : Constant.PARTNER_FEEDBACK_NEW_COMMENT + "##" + paramRequest.get("searchName")
                    + "##" + feedbackUrl,
            Constant.CONTACT);
      }
    } catch (Exception ex) {
      log.info(ex);
      throw new APIException("Error in send feedback email");
    }
  }

  private void sendClientFeedbackMail(String email, String partnerName,
      HashMap<String, String> paramRequest, HttpServletRequest request, Boolean isReplyFeedback,
      String kgpTeamId, String type) {
    log.info("sending client feedback email");
    String locate = "en_US";
    try {
      Map<String, String> staticContentsMap =
          StaticContentsMultiLanguageUtil.getStaticContentsMap(locate, Constant.EMAILS_CONTENT_MAP);
      String mailSubject = staticContentsMap.get("candidate.suite.feedback.email.subject");
      mailService.sendEmail(email, null, mailSubject + " " + partnerName,
          mailService.getFeedbackEmailContent(request, staticContentsMap,
              Constant.CANDIDATE_FEEDBACK_EMAIL_TEMPLATE, partnerName, paramRequest,
              isReplyFeedback),
          null);

      String feedbackUrl = CommonUtil.getServerUrl(request) + request.getContextPath()
          + "/my-status/" + paramRequest.get("candidateId") + "/" + paramRequest.get("searchId")
          + "/" + paramRequest.get("searchName") + "/" + paramRequest.get("contactId") + "/"
          + "true" + "/" + paramRequest.get("commentId");
      feedbackUrl = feedbackUrl.replaceAll(Constant.SPACE_STRING, "%20");
      if (type.equals(Constant.PARTNER)) {
        webSocketNotificationService.sendWebSocketNotification(kgpTeamId,
            paramRequest.get("contactId"),
            Boolean.TRUE.equals(isReplyFeedback)
                ? Constant.PARTNER_FEEDBACK_REPLY_COMMENT + "##" + paramRequest.get("searchName")
                    + "##" + feedbackUrl
                : Constant.PARTNER_FEEDBACK_NEW_COMMENT + "##" + paramRequest.get("searchName")
                    + "##" + feedbackUrl,
            Constant.PARTNER);
      } else
        webSocketNotificationService.sendWebSocketNotification(kgpTeamId,
            paramRequest.get("contactId"),
            Boolean.TRUE.equals(isReplyFeedback)
                ? Constant.CONTACT_FEEDBACK_REPLY_COMMENT + "##" + paramRequest.get("searchName")
                    + "##" + feedbackUrl
                : Constant.CONTACT_FEEDBACK_NEW_COMMENT + "##" + paramRequest.get("searchName")
                    + "##" + feedbackUrl,
            Constant.PARTNER);

    } catch (Exception e) {
      log.info(e);
      throw new APIException("Error in sending candidate feedback email");
    }
    log.info("Client Feedback Mail sent to all partners successfully.");
  }

  private String generateJwtToken(String userName, String password) {
    log.info("generating Token for user...");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, 1); // to get previous year add -1
    Date nextYear = cal.getTime();
    String auth = userName + ":" + password;
    String token = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    return Jwts.builder().setSubject("candidateSuite").setExpiration(nextYear)
        .setIssuer(Constant.FROM_MAIL).claim("authentication", "Basic " + token)
        .signWith(SignatureAlgorithm.HS512,
            Base64.getEncoder().encodeToString("candidateSuite-secret-key".getBytes()))
        .compact();
  }

  @Override
  public List<CandidateFeedbackDTO> getCandidateFeedback(String candidateId) {
    String apiResponse = restUtil.newGetMethod(
        Constant.CANDIDATE_FEEDBACK_URL.replace(Constant.CANDIDATE_ID_BRACES, candidateId));
    if (!apiResponse.contains("candidate_id")) {
      throw new NotFoundException("Invalid Candidate Id");
    }
    List<CandidateFeedbackDTO> candidateFeedbackList;
    try {
      candidateFeedbackList =
          new Gson().fromJson(apiResponse, new TypeToken<List<CandidateFeedbackDTO>>() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;
          }.getType());
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.CONVERT_JSON_ERROR);
    }

    return candidateFeedbackList;
  }

  @Override
  public CandidateFeedbackDTO addCandidateFeedbackReply(String candidateId, String commentId,
      String reply, String galaxyId, HttpServletRequest request, String type) {
    List<CandidateFeedbackReplyDTO> candidateFeedbackReply = new ArrayList<>();
    JsonObject paramJSON = new JsonObject();
    paramJSON.addProperty("reply", reply);
    if (type.equals("candidate")) {
      paramJSON.addProperty("created_by", candidateId);
      paramJSON.addProperty("type", "Candidate");
    } else if (type.equals("partner")) {
      paramJSON.addProperty("created_by", galaxyId);
      paramJSON.addProperty("type", "User");
    }
    String jsonString =
        restUtil.postMethod(Constant.CANDIDATE_FEEDBACK_REPLY_URL.replace("{commentId}", commentId)
            .replace(Constant.CANDIDATE_ID_BRACES, candidateId), paramJSON.toString(), null);
    log.debug("Json String response after add candidate Feedback reply " + jsonString);
    if (jsonString.contains("invalid")) {
      throw new APIException("Invalid candidateId or commentId");
    }
    String replyId = new Gson().fromJson(jsonString, CandidateFeedbackDTO.class).getId();
    CandidateFeedbackDTO candidateFeedbackDTO =
        getCandidateFeedbackByCommentId(candidateId, commentId);

    CandidateFeedbackReplyDTO candidateFeedbackReplyDTO = candidateFeedbackDTO.getReplies().stream()
        .filter(e -> e.getId().equals(replyId)).findFirst().orElse(new CandidateFeedbackReplyDTO());

    candidateFeedbackReply.add(candidateFeedbackReplyDTO);
    candidateFeedbackDTO.setReplies(candidateFeedbackReply);
    addCandidateFeedback(candidateId, reply, galaxyId, request, true, candidateFeedbackDTO, type);
    return candidateFeedbackDTO;
  }

  @Override
  public CandidateFeedbackDTO getCandidateFeedbackByCommentId(String candidateId,
      String commentId) {
    List<CandidateFeedbackDTO> candidateFeedbackList = getCandidateFeedback(candidateId);
    return candidateFeedbackList.stream().filter(e -> e.getId().equals(commentId)).findFirst()
        .orElse(new CandidateFeedbackDTO());
  }

  @Override
  public String updateCommentStatus(String commentId, boolean status) {
    log.info("update feedback status");
    String jsonResponseString = null;
    JsonObject paramJSON = new JsonObject();

    paramJSON.addProperty("status", status);
    try {
      jsonResponseString = restUtil.putMethod(Constant.CANDIDATE_FEEDBACK_STATUS_UPDATE_URL
          .replace(Constant.COMMENT_ID_BRACES, commentId), paramJSON.toString());
    } catch (UnsupportedEncodingException e) {
      log.error(e.getMessage());
    }
    JsonObject json = (JsonObject) JsonParser.parseString(jsonResponseString);
    return json.get("responseString").toString();
  }
}
