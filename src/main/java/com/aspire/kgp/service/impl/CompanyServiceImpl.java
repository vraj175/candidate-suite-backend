package com.aspire.kgp.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CompanyDTO;
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.dto.DocumentDTO;
import com.aspire.kgp.dto.TimeZoneDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.model.UserSearch;
import com.aspire.kgp.service.CompanyService;
import com.aspire.kgp.service.UserSearchService;
import com.aspire.kgp.util.CommonUtil;
import com.aspire.kgp.util.RestUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


@Service
public class CompanyServiceImpl implements CompanyService {
  static Log log = LogFactory.getLog(CompanyServiceImpl.class.getName());


  @Autowired
  RestUtil restUtil;

  @Autowired
  UserSearchService searchService;

  @Override
  public final List<CompanyDTO> getCompanyList(String stage) {
    List<UserSearch> invitedCompanies = searchService.findByIsDeletedFalse();
    if (invitedCompanies.isEmpty()) {
      return Collections.emptyList();
    }

    String companyListResponse =
        restUtil.newGetMethod(Constant.COMPANY_LIST.replace("{STAGE}", stage));
    try {
      List<CompanyDTO> allCompanies =
          new Gson().fromJson(companyListResponse, new TypeToken<List<CompanyDTO>>() {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;
          }.getType());
      return getInvitedCompaniesFromAllCompanies(allCompanies, invitedCompanies);
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
  }

  public List<CompanyDTO> getInvitedCompaniesFromAllCompanies(List<CompanyDTO> allCompanies,
      List<UserSearch> invitedCompanies) {
    // We create a stream of elements from the first list.
    return allCompanies.stream()
        // We select any elements such that in the stream of elements from the second
        // list
        .filter(two -> invitedCompanies.stream()
            // there is an element that has the same name and school as this element,
            .anyMatch(one -> one.getCompanyId().equals(two.getId())))
        // and collect all matching elements from the first list into a new list.
        .collect(Collectors.toList());
    // We return the collected list.
  }

  @Override
  public CandidateDTO getCompanyInfoDetails(String candidateId, String timeZone) {
    timeZone = CommonUtil.charCodeStringToString(timeZone);

    JsonObject timeZoneObj = getCurrentLocalTimeZone(timeZone);
    timeZoneObj.addProperty("format", "DD MMM YYYY");
    JsonObject paramJSON = new JsonObject();
    if (!timeZone.isEmpty() && !candidateId.isEmpty()) {
      paramJSON.add("timeZone", timeZoneObj);
      paramJSON.addProperty("id", candidateId);
    }
    paramJSON.addProperty("clientInterviewDescOrder", true);
    String apiResponse =
        restUtil.postMethod(Constant.REPORT_URL.replace(Constant.CANDIDATE_ID_BRACES, candidateId),
            paramJSON.toString(), null);
    JsonObject json = (JsonObject) JsonParser.parseString(apiResponse);
    JsonObject contact = json.getAsJsonObject("contact");
    JsonObject jsonObjects = json.getAsJsonObject("candidate");
    CandidateDTO candidateDTO;
    CandidateDTO candidateContactDTO;
    ContactDTO contactDTO;
    try {
      candidateDTO = new Gson().fromJson(jsonObjects, new TypeToken<CandidateDTO>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
      candidateContactDTO = new Gson().fromJson(contact, new TypeToken<CandidateDTO>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());

      contactDTO = new Gson().fromJson(contact, new TypeToken<ContactDTO>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());

      if (candidateDTO == null && candidateContactDTO == null) {
        throw new APIException(Constant.INVALID_CANDIDATE_ID);
      }
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
    if (candidateDTO != null) {
      candidateDTO.setDegreeVerification(Boolean.TRUE);
      mergeName(candidateDTO);
    }
    if (candidateContactDTO.getAthenaStatus() != null
        && candidateContactDTO.getAthenaStatus().equalsIgnoreCase(Constant.COMPLETED)
        && candidateDTO != null) {
      candidateDTO.setContactId(candidateContactDTO.getId());
      candidateDTO.setAthenaCompleted(Boolean.TRUE);
    }
    if (candidateDTO != null && contactDTO != null) {
      candidateDTO.setContact(contactDTO);
    }
    return candidateDTO;
  }


  private CandidateDTO mergeName(CandidateDTO candidateDTO) {
    for (int i = candidateDTO.getInterviews().size() - 1; i >= 0; i--) {
      if (candidateDTO.getInterviews().get(i).getInterviewDate() != null
          && !candidateDTO.getInterviews().get(i).getInterviewDate().isEmpty()) {
        if (candidateDTO.getInterviews().get(i).getClient() != null
            && candidateDTO.getInterviews().get(i).getClient().getFirstName() != null
            && !candidateDTO.getInterviews().get(i).getClient().getFirstName().isEmpty()
            && candidateDTO.getInterviews().get(i).getClient().getLastName() != null
            && !candidateDTO.getInterviews().get(i).getClient().getLastName().isEmpty()) {
          candidateDTO.getInterviews().get(i).getClient()
              .setName(candidateDTO.getInterviews().get(i).getClient().getFirstName() + " "
                  + candidateDTO.getInterviews().get(i).getClient().getLastName());
        } else {
          candidateDTO.getInterviews().remove(i);
        }
      } else {
        candidateDTO.getInterviews().remove(i);
      }
    }
    return candidateDTO;
  }

  @Override
  public List<CompanyDTO> getListOfCompany(String companyName) {
    String apiResponse =
        restUtil.newGetMethod(Constant.GET_COMPANY_LIST_URL.replace("{COMPANYNAME}", companyName));
    try {
      return new Gson().fromJson(apiResponse, new TypeToken<List<CompanyDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
  }

  @Override
  public String addNewCompany(String companyData) {
    return restUtil.postMethod(Constant.COMPANY_SAVE_URL, companyData, null);
  }

  @Override
  public List<DocumentDTO> getDocumentAttchment(String companyId) {
    try {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("show_in_candidatesuite", "true");
      String apiResponse = restUtil.postMethod(
          Constant.GET_DOCUMENT_ATTCHMENT_LIST_URL.replace("{COMPANYID}", companyId),
          jsonObject.toString(), null);

      if (apiResponse.contains("{\"message\":")) {
        throw new APIException(Constant.INVALID_COMPANY_ID);
      }
      return new Gson().fromJson(apiResponse, new TypeToken<List<DocumentDTO>>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (Exception e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
  }

  @Override
  public ResponseEntity<Object> uploadCompanyAttachment(MultipartFile multipartFile,
      String companyId, HttpServletRequest request) {

    File file;
    try {
      String fileName = multipartFile.getOriginalFilename();
      if (fileName == null || fileName.trim().isEmpty()) {
        throw new APIException(Constant.FILE_UPLOAD_ERROR);
      }
      String extension = fileName.substring(fileName.lastIndexOf("."));
      log.info(extension);
      File parent = new File(System.getProperty("java.io.tmpdir"));
      file = new File(parent, fileName);
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(multipartFile.getBytes());
      fos.close();
    } catch (IOException e1) {
      throw new APIException(Constant.FILE_UPLOAD_ERROR);
    }
    String response = "";

    response = restUtil.postMethod(
        Constant.COMPANY_ATTECHMENT_URL.replace(Constant.COMPANY_ID_BRACES, companyId), null, file);

    log.info(response);
    JsonObject responseJson = new Gson().fromJson(response, JsonObject.class);

    try {
      if (responseJson.get("id").getAsString() != null) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(Constant.TIMESTAMP, new Date());
        body.put(Constant.STATUS, "200");
        body.put(Constant.MESSAGE, Constant.FILE_UPLOADED_SUCCESSFULLY);
        return new ResponseEntity<>(body, HttpStatus.OK);
      }
    } catch (Exception e) {
      throw new APIException(Constant.FILE_UPLOAD_ERROR);
    }
    Map<String, Object> body = new LinkedHashMap<>();
    body.put(Constant.TIMESTAMP, new Date());
    body.put(Constant.STATUS, "500");
    body.put(Constant.MESSAGE, Constant.FILE_UPLOAD_ERROR);
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public JsonObject getCurrentLocalTimeZone(String currentTimeZone) {
    String listOfTimeZones = restUtil.newGetMethod(Constant.GET_TIMEZONE);
    List<TimeZoneDTO> timeZoneList = new ArrayList<>();

    String timeZoneName = currentTimeZone.split("@")[0];
    String timeZoneType = currentTimeZone.split("@")[1];

    log.info("timeZoneName :: " + timeZoneName);
    log.info("timeZoneType :: " + timeZoneType);

    TimeZoneDTO timeZoneObj = new TimeZoneDTO();
    JsonObject timeZonePicklistObj = new JsonObject();
    timeZonePicklistObj.addProperty("id", "");
    timeZonePicklistObj.addProperty("name", "");
    timeZonePicklistObj.addProperty("type", "");
    timeZonePicklistObj.addProperty("abbreviation", "");
    if (CommonUtil.checkNullString(listOfTimeZones)) {
      log.error("Error while fetching contact search.");
      return timeZonePicklistObj;
    }
    JsonArray jsonArray = (JsonArray) JsonParser.parseString(listOfTimeZones);
    try {
      timeZoneList = new Gson().fromJson(jsonArray, new TypeToken<List<TimeZoneDTO>>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      log.error("Oops! error while fetching active candidate details");
      timeZoneList = Collections.emptyList();
    }

    int temp = 0;
    for (TimeZoneDTO timeZone : timeZoneList) {
      String time = timeZone.getName().replaceAll("[/[():]/g]", "").split(" ")[0];
      log.info("time :: " + time + " timeZoneName :: " + timeZoneName);
      if (time.equals(timeZoneName) && timeZoneType.equals(timeZone.getType())) {
        log.info("latest time zone is :::: " + timeZone.getName());
        temp = 1;
        timeZoneObj = timeZone;
      }
    }
    if (temp == 0) {
      for (TimeZoneDTO timeZone : timeZoneList) {
        String time = timeZone.getName().replaceAll("[/[():]/g]", "").split(" ")[0];
        log.info("time :: " + time + " timeZoneName :: " + timeZoneName);
        if (time.equals(timeZoneName)) {
          log.info("latest time zone is :::: " + timeZone.getName());
          timeZoneObj = timeZone;
        }
      }
    }

    timeZonePicklistObj.addProperty("id", timeZoneObj.getId());
    timeZonePicklistObj.addProperty("name", timeZoneObj.getName());
    timeZonePicklistObj.addProperty("type", timeZoneObj.getType());
    timeZonePicklistObj.addProperty("abbreviation", timeZoneObj.getAbbreviation());

    return timeZonePicklistObj;
  }
}
