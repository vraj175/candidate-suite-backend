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
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.CandidateService;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.util.CommonUtil;
import com.aspire.kgp.util.RestUtil;
import com.aspire.kgp.util.StaticContentsMultiLanguageUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import freemarker.template.TemplateException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class CandidateServiceImpl implements CandidateService {
  static Log log = LogFactory.getLog(CandidateServiceImpl.class.getName());

  @Autowired
  RestUtil restUtil;

  @Autowired
  MailService mailService;

  @Value("${clientsuite.url}")
  private String clientsuiteUrl;

  @Override
  public CandidateDTO getCandidateDetails(String candidateId) {
    String apiResponse =
        restUtil.newGetMethod(Constant.CONDIDATE_URL.replace(Constant.CANDIDATE_ID, candidateId));
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
    partnerArray.forEach(e -> partnerList
        .add(new Gson().fromJson(e.getAsJsonObject().get("user"), new TypeToken<UserDTO>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;
        }.getType())));
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
      HttpServletRequest request) {
    log.info("Saving new feedback");
    JsonObject paramJSON = new JsonObject();
    HashMap<String, String> paramRequest = new HashMap<>();
    String feedbackId;
    String feedback = "";
    String candidateName = "";
    String searchId = "";
    String searchName = "";
    String companyName = "";
    String contactId = "";
    paramJSON.addProperty("createdBy", galaxyId);
    paramJSON.addProperty("comments", comments);
    try {
      feedbackId = restUtil.postMethod(
          Constant.CANDIDATE_FEEDBACK_URL.replace(Constant.CANDIDATE_ID, candidateId),
          paramJSON.toString(), null);
    } catch (APIException e2) {
      log.error("error while adding feedback" + e2);
      throw new APIException("error while add feedback");
    }
    if (feedbackId != null && !feedbackId.isEmpty()) {
      CandidateDTO apiResponse = getCandidateDetails(candidateId);
      Set<String> kgpPartnerEmailList = new HashSet<>();
      try {
        log.info("Feedback added succefully with commentId:- " + feedbackId);
        if (feedbackId.contains("id")) {
          kgpPartnerEmailList =
              teamMemberList(apiResponse.getSearch().getPartners(), kgpPartnerEmailList);
          kgpPartnerEmailList =
              teamMemberList(apiResponse.getSearch().getRecruiters(), kgpPartnerEmailList);
          kgpPartnerEmailList =
              teamMemberList(apiResponse.getSearch().getResearchers(), kgpPartnerEmailList);
          kgpPartnerEmailList =
              teamMemberList(apiResponse.getSearch().getEas(), kgpPartnerEmailList);

          feedback = comments;
          candidateName =
              apiResponse.getContact().getFirstName() + apiResponse.getContact().getLastName();
          searchId = apiResponse.getSearch().getId();
          searchName = apiResponse.getSearch().getJobTitle();
          companyName = apiResponse.getSearch().getCompany().getName();
          contactId = apiResponse.getContactId();
          paramRequest.put(feedback, comments);
          paramRequest.put(candidateName,
              apiResponse.getContact().getFirstName() + apiResponse.getContact().getLastName());
          paramRequest.put(searchId, comments);
          paramRequest.put(searchName, comments);
          paramRequest.put(companyName, comments);
          paramRequest.put(contactId, comments);
          paramRequest.put("candidateId", candidateId);
          paramRequest.put("commentId", feedbackId);
        }
      } catch (JsonSyntaxException e) {
        log.error("oops ! invalid json");
        throw new JsonSyntaxException("error while get team member");
      } catch (APIException e1) {
        log.error("error while get kgp team member.");
        throw new APIException("error while get kgp team member");
      }
      try {
        for (String kgpTeamMeberDetails : kgpPartnerEmailList) {
          log.info("Partner Email : " + kgpTeamMeberDetails);
          sendClientFeedbackMail(kgpTeamMeberDetails.split("##")[0],
              kgpTeamMeberDetails.split("##")[1], paramRequest, request);
        }
      } catch (Exception e) {
        log.info(e);
        throw new APIException("Error in send feedback email");
      }
    }
    return feedbackId;
  }


  private void sendClientFeedbackMail(String email, String partnerName,
      HashMap<String, String> paramRequest, HttpServletRequest request) {
    log.info("sending client feedback email");
    String locate = "en_US";
    email = "abhishek.jaiswal@aspiresoftserv.com";
//    User user = (User) request.getAttribute("user");
    UserDTO userDTO = new UserDTO();
    userDTO.setToken(generateJwtToken(email, email));
    userDTO.setEmail(email);
//    userDTO.setFirstName(user.get);
    try {
      Map<String, String> staticContentsMap =
          StaticContentsMultiLanguageUtil.getStaticContentsMap(locate, Constant.EMAILS_CONTENT_MAP);
      String mailSubject = staticContentsMap.get("candidate.suite.feedback.email.subject");
      mailService.sendEmail(email, null, mailSubject + " " + partnerName,
          mailService.getFeedbackEmailContent(request, userDTO, staticContentsMap,
              Constant.CANDIDATE_FEEDBACK_EMAIL_TEMPLATE, partnerName, paramRequest),
          null);
    } catch (Exception e) {
      log.info(e);
      throw new APIException("Error in sending candidate feedback email");
    }
    log.info("Client Feedback Mail sent to all partners successfully.");
  }

  /**
   * Create team user map from JSON string of record
   * 
   * @param partnerEmailList Set<String>
   * @throws UnsupportedEncodingException
   */
  private Set<String> teamMemberList(List<UserDTO> users, Set<String> partnerEmailList) {
    log.info("Creating Team member email and name set");
    for (UserDTO user : users) {
      if (user != null && CommonUtil.checkNotNullString(user.getId())) {
        partnerEmailList.add(user.getEmail() + "##" + user.getName());
      }
    }
    return partnerEmailList;
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
    String apiResponse = restUtil
        .newGetMethod(Constant.CANDIDATE_FEEDBACK_URL.replace(Constant.CANDIDATE_ID, candidateId));
    if (!apiResponse.contains("candidate_id")) {
      throw new APIException("Invalid Candidate Id");
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

}
