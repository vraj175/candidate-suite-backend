package com.aspire.kgp.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

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
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.service.CandidateService;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.util.CommonUtil;
import com.aspire.kgp.util.RestUtil;
import com.aspire.kgp.util.StaticContentsMultiLanguageUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
  @Transactional(value = TxType.REQUIRES_NEW)
  public ResponseEntity<Object> saveFeedbackAndSendmail(HttpServletRequest resourceRequest) {
    log.info("Entring into saving feedback and sending email to candidate");
    JsonObject params = new JsonObject();
    String addOrUpdate = resourceRequest.getParameter("addUpdateaction");
    log.info("action is :: " + addOrUpdate);
    log.info("UserId : " + resourceRequest.getParameter("userId"));

    if (CommonUtil.checkNullString(addOrUpdate))
      addOrUpdate = "addNewFeedback";
    if (addOrUpdate.equals("updateFeedback")) {
      params.addProperty("clientId", resourceRequest.getParameter("clientId"));
      params.addProperty("feedbackId", resourceRequest.getParameter("feedbackId"));
    }
    String clientId = resourceRequest.getParameter("clientId");
    if (CommonUtil.checkNotNullString(clientId)) {
      params.addProperty("clientId", resourceRequest.getParameter("clientId"));
      params.addProperty("clientName", resourceRequest.getParameter("clientName"));
    }
    log.info("clientId  : " + clientId);
    params.addProperty("addOrUpdate", addOrUpdate);
    params.addProperty("userId", resourceRequest.getParameter("userId"));
    params.addProperty("candidateId", resourceRequest.getParameter("candidateId"));
    params.addProperty("candidateName", resourceRequest.getParameter("candidateName"));
    params.addProperty("feedback", resourceRequest.getParameter("feedback"));
    params.addProperty("searchId", resourceRequest.getParameter("searchId"));
    params.addProperty("searchTitle", resourceRequest.getParameter("searchName"));
    params.addProperty("jobNumber", resourceRequest.getParameter("searchNumber"));
    params.addProperty("candidateType", resourceRequest.getParameter("candidateType"));
    params.addProperty("companyName", resourceRequest.getParameter("companyName"));
    params.addProperty("clientType", "contact");
    saveGeneralComments(params.toString(), resourceRequest);
    log.info("Exiting into saving feedback and sending email to candidate");
    return null;
  }

  private String saveGeneralComments(String params, HttpServletRequest resourceRequest) {
    log.info("saving client feedback");
    JsonObject response = new JsonObject();

    // write save feedback code here

    try {
      sendClientFeedbackMail(params, resourceRequest);
    } catch (Exception e) {
      log.info(e);
      throw new APIException("Error in send feedback email");
    }
    response.addProperty("success", "true");
    response.addProperty("Message", "General Comments save succesfully");
    log.info("Save or update feedback successfully!!");
    return response.toString();
  }

  public void sendClientFeedbackMail(String params, HttpServletRequest resourceRequest)
      throws IOException, TemplateException {
    log.info("sending client feedback email");
    String locate = "en_US";
    String email = "abhishek.jaiswal@aspiresoftserv.com";
    UserDTO userDTO = new UserDTO();
    userDTO.setToken(generateJwtToken(email, email));
    userDTO.setEmail(email);
    userDTO.setFirstName("Abhishek");
    userDTO.setLastName("Jaiswal");
    try {
      Map<String, String> staticContentsMap =
          StaticContentsMultiLanguageUtil.getStaticContentsMap(locate, Constant.EMAILS_CONTENT_MAP);
      String mailSubject = staticContentsMap.get("candidate.suite.feedback.email.subject");
      mailService.sendEmail(email, null, mailSubject + " Abhishek Jaiswal",
          mailService.getFeedbackEmailContent(resourceRequest, userDTO, staticContentsMap,
              Constant.CANDIDATE_FEEDBACK_EMAIL_TEMPLATE),
          null);
    } catch (Exception e) {
      log.info(e);
      throw new APIException("Error in sending candidate feedback email");
    }
    log.info("Client Feedback Mail sent to all partners successfully.");

  }

  private String generateJwtToken(String userName, String password) {
    log.info("generating Token for user...");
    Date dt = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, 1);
    dt = c.getTime();

    String auth = userName + ":" + password;
    String token = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

    return Jwts.builder().setSubject("candidateSuite").setExpiration(dt)
        .setIssuer(Constant.FROM_MAIL).claim("authentication", "Basic " + token)
        .signWith(SignatureAlgorithm.HS512,
            Base64.getEncoder().encodeToString("candidateSuite-secret-key".getBytes()))
        .compact();
  }

  @Override
  public String addCandidateFeedback(String candidateId, String comments, String galaxyId) {
    JsonObject paramJSON = new JsonObject();
    paramJSON.addProperty("createdBy", galaxyId);
    paramJSON.addProperty("comments", comments);
    return restUtil.postMethod(
        Constant.CANDIDATE_FEEDBACK_URL.replace(Constant.CANDIDATE_ID_BRACES, candidateId),
        paramJSON.toString(), null);

  }

  @Override
  public List<CandidateFeedbackDTO> getCandidateFeedback(String candidateId) {
    String apiResponse = restUtil.newGetMethod(
        Constant.CANDIDATE_FEEDBACK_URL.replace(Constant.CANDIDATE_ID_BRACES, candidateId));
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

  @Override
  public CandidateFeedbackDTO addCandidateFeedbackReply(String candidateId, String commentId,
      String reply, String galaxyId) {
    List<CandidateFeedbackReplyDTO> candidateFeedbackReply = new ArrayList<>();
    JsonObject paramJSON = new JsonObject();
    paramJSON.addProperty("reply", reply);
    paramJSON.addProperty("created_by", galaxyId);

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
    return candidateFeedbackDTO;
  }

  @Override
  public CandidateFeedbackDTO getCandidateFeedbackByCommentId(String candidateId,
      String commentId) {
    List<CandidateFeedbackDTO> candidateFeedbackList = getCandidateFeedback(candidateId);
    return candidateFeedbackList.stream().filter(e -> e.getId().equals(commentId)).findFirst()
        .orElse(new CandidateFeedbackDTO());
  }
}
