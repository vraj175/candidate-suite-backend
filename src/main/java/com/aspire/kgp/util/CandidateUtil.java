package com.aspire.kgp.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.DocumentDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@Component
public class CandidateUtil {
  static Log log = LogFactory.getLog(CandidateUtil.class.getName());

  @Autowired
  RestUtil restUtil;

  public final CandidateDTO getCandidateDetails(String candidateId) {
    String apiResponse =
        restUtil.newGetMethod(Constant.CONDIDATE_URL.replace("{candidateId}", candidateId));
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
    candidateDTO.getSearch().setPartners(addJsonArraytoList(json, "partners"));
    candidateDTO.getSearch().setRecruiters(addJsonArraytoList(json, "recruiters"));
    candidateDTO.getSearch().setResearchers(addJsonArraytoList(json, "researchers"));
    candidateDTO.getSearch().setEas(addJsonArraytoList(json, "eas"));

    return candidateDTO;
  }

  public final DocumentDTO getCandidateResumes(String candidateId) {
    List<DocumentDTO> documentList = null;
    String apiResponse =
        restUtil.newGetMethod(Constant.RESUME_URL.replace("{candidateId}", candidateId));
    try {
      documentList = new Gson().fromJson(apiResponse, new TypeToken<List<DocumentDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      log.error("Oops! error while fetching document list details");
      documentList = Collections.emptyList();
    }
    log.info("End of getDocuments method");
    if(documentList.isEmpty()) {
      return null;
    }
    return documentList.get(0);
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

  public void downloadDocument(String documentName, String attachmentId,
      HttpServletResponse response) {
    try {
      OutputStream os = response.getOutputStream();
      response.setContentType("application/octet-stream; charset=ISO-8859-1");

      ContentDisposition contentDisposition = ContentDisposition.builder("inline")
          .filename(documentName.replaceAll("[?:,!%#\"]", "")).build();

      response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
      // write document to output stream get data from API
      restUtil.newGetMethod(Constant.DOWNLOAD_ATTACHMENT.replace("{attachmentId}", attachmentId),
          os);
      os.flush();
      os.close();
    } catch (IOException e) {
      throw new APIException("error in download document");
    }

  }

}
