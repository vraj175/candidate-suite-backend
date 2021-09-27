package com.aspire.kgp.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.service.CandidateService;
import com.aspire.kgp.util.CommonUtil;
import com.aspire.kgp.util.RestUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class CandidateServiceImpl implements CandidateService {
  static Log log = LogFactory.getLog(CandidateServiceImpl.class.getName());

  @Autowired
  RestUtil restUtil;

  @Value("${clientsuite.url}")
  private String clientsuiteUrl;

  @Override
  public CandidateDTO getCandidateDetails(String candidateId) {
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
}
