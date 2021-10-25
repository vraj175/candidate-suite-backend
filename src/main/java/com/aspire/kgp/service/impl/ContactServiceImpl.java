package com.aspire.kgp.service.impl;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.dto.ContactReferencesDTO;
import com.aspire.kgp.dto.DocumentDTO;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.service.CandidateService;
import com.aspire.kgp.service.ContactService;
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

@Service
public class ContactServiceImpl implements ContactService {
  static Log log = LogFactory.getLog(ContactServiceImpl.class.getName());

  @Autowired
  RestUtil restUtil;

  @Autowired
  MailService mailService;

  @Autowired
  CandidateService candidateService;

  @Override
  public ContactDTO getContactDetails(String contactId) {
    String apiResponse =
        restUtil.newGetMethod(Constant.CONTACT_URL.replace(Constant.CONTACT_ID, contactId));

    try {
      return new Gson().fromJson(apiResponse, new TypeToken<ContactDTO>() {

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
  public byte[] getContactImage(String contactId) {
    return restUtil
        .newGetImage(Constant.CONTACT_PROFILE_IMAGE_URL.replace(Constant.CONTACT_ID, contactId));
  }

  @Override
  public String updateContactDetails(String contactId, String contactData)
      throws UnsupportedEncodingException {
    return restUtil.putMethod(Constant.CONTACT_URL.replace("{contactId}", contactId), contactData);
  }

  @Override
  public String updateContactReference(String referenceId, String referenceData)
      throws UnsupportedEncodingException {
    return restUtil.putMethod(
        Constant.UPDATE_CONTACT_REFERENCE_URL.replace("{referenceId}", referenceId), referenceData);
  }

  @Override
  public final String addContactReference(String contactId, String referenceData) {
    return restUtil.postMethod(
        Constant.CONTACT_REFERENCE_URL.replace(Constant.CONTACT_ID, contactId), referenceData,
        null);
  }

  @Override
  public String uploadCandidateResume(MultipartFile multipartFile, String contactId, String type,
      String candidateId, HttpServletRequest request) {
    JsonObject paramJSON = new JsonObject();
    paramJSON.addProperty("description", "");
    paramJSON.addProperty("show_in_clientsuite", false);

    File file;
    try {
      String fileName = multipartFile.getOriginalFilename();
      if (fileName == null) {
        throw new APIException(Constant.FILE_UPLOAD_ERROR);
      }
      String extension = fileName.substring(fileName.lastIndexOf("."));
      log.info(extension);
      file = File.createTempFile(fileName.substring(0, fileName.lastIndexOf(".") - 1), extension);
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(multipartFile.getBytes());
      fos.close();
    } catch (IOException e1) {
      throw new APIException(Constant.FILE_UPLOAD_ERROR);
    }
    String response = "";
    if (Constant.RESUME.equalsIgnoreCase(type)) {
      response = restUtil.postMethod(Constant.RESUME_URL.replace(Constant.CONTACT_ID, contactId),
          paramJSON.toString(), file);
    } else if ((Constant.OFFER_LETTER.equalsIgnoreCase(type))) {
      response =
          restUtil.postMethod(Constant.OFFER_LETTER_URL.replace(Constant.CONTACT_ID, contactId),
              paramJSON.toString(), file);
    } else {
      response =
          restUtil.postMethod(Constant.ATTECHMENT_URL.replace(Constant.CONTACT_ID, contactId),
              paramJSON.toString(), file);
    }
    log.info(response);
    JsonObject responseJson = new Gson().fromJson(response, JsonObject.class);

    try {
      if (responseJson.get("id").getAsString() != null) {
        sentUploadNotification(contactId, request, candidateId, type);
        return Constant.FILE_UPLOADED_SUCCESSFULLY;
      }
    } catch (Exception e) {
      throw new APIException(Constant.FILE_UPLOAD_ERROR);
    }
    return Constant.FILE_UPLOAD_ERROR;
  }

  @Override
  public String uploadContactImage(MultipartFile multipartFile, String contactId) {
    JsonObject paramJSON = new JsonObject();

    File image;
    try {
      String imageName = multipartFile.getOriginalFilename();
      if (imageName == null) {
        throw new APIException(Constant.IMAGE_UPLOAD_ERROR);
      }
      String extension = imageName.substring(imageName.lastIndexOf("."));
      log.info(extension);
      image =
          File.createTempFile(imageName.substring(0, imageName.lastIndexOf(".") - 1), extension);
      FileOutputStream fos = new FileOutputStream(image);
      fos.write(multipartFile.getBytes());
      fos.close();
    } catch (IOException e1) {
      throw new APIException(Constant.IMAGE_UPLOAD_ERROR);
    }

    String response =
        restUtil.postMethod(Constant.IMAGE_UPLOAD_URL.replace(Constant.CONTACT_ID, contactId),
            paramJSON.toString(), image);
    log.info(response);
    JsonObject responseJson = new Gson().fromJson(response, JsonObject.class);

    try {
      if (responseJson.get("imageId").getAsString() != null) {
        return Constant.IMAGE_UPLOADED_SUCCESSFULLY;
      }
    } catch (Exception e) {
      throw new APIException(Constant.IMAGE_UPLOAD_ERROR);
    }
    return Constant.IMAGE_UPLOAD_ERROR;
  }

  @Override
  public final DocumentDTO getContactResumes(String contactId) {
    List<DocumentDTO> documentList = null;
    String apiResponse =
        restUtil.newGetMethod(Constant.RESUME_URL.replace(Constant.CONTACT_ID, contactId));
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
    if (documentList.isEmpty()) {
      return null;
    }
    return documentList.get(0);
  }

  @Override
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

  @Override
  public List<ContactReferencesDTO> getListOfReferences(String contactId) {
    String apiResponse = restUtil
        .newGetMethod(Constant.CONTACT_REFERENCE_URL.replace(Constant.CONTACT_ID, contactId));

    try {
      return new Gson().fromJson(apiResponse, new TypeToken<List<ContactReferencesDTO>>() {

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
  public List<SearchDTO> getListOfContactSearches(String contactId) {
    String apiResponse = restUtil
        .newGetMethod(Constant.CONTACT_SEARCHES_URL.replace(Constant.CONTACT_ID, contactId));
    List<SearchDTO> listSearch = new ArrayList<>();
    if (CommonUtil.checkNullString(apiResponse)) {
      log.error("Error while fetching contact search.");
      return listSearch;
    }
    JsonObject jsonObjects = (JsonObject) JsonParser.parseString(apiResponse);
    JsonArray jsonArray = jsonObjects.getAsJsonArray("data");

    if (jsonArray == null) {
      throw new NotFoundException("Invalid Contact Id");
    }
    try {
      return new Gson().fromJson(jsonArray, new TypeToken<List<SearchDTO>>() {

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
  public List<ContactDTO> getListOfContactByName(String contactName) {
    String apiResponse = restUtil
        .newGetMethod(Constant.GET_CONTACT_LIST_BY_NAME_URL.replace("{CONTACTNAME}", contactName));

    List<ContactDTO> listContact = new ArrayList<>();
    if (CommonUtil.checkNullString(apiResponse)) {
      log.error("Error while fetching contact.");
      return listContact;
    }
    JsonObject jsonObjects = (JsonObject) JsonParser.parseString(apiResponse);
    JsonArray jsonArray = jsonObjects.getAsJsonArray("data");

    if (jsonArray == null) {
      throw new NotFoundException("Invalid Contact Id");
    }
    try {
      return new Gson().fromJson(jsonArray, new TypeToken<List<ContactDTO>>() {

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
  public String addNewContact(String contactData) {
    return restUtil.postMethod(Constant.CONTACT_SAVE_URL, contactData, null);
  }

  @Override
  public DocumentDTO getContactOfferLetter(String contactId) {
    List<DocumentDTO> documentList = null;
    String apiResponse =
        restUtil.newGetMethod(Constant.OFFER_LETTER_URL.replace(Constant.CONTACT_ID, contactId));
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
    if (documentList.isEmpty()) {
      return null;
    }
    return documentList.get(0);
  }

  private void sentUploadNotification(String contactId, HttpServletRequest request,
      String candidateId, String type) {
    Set<String> kgpPartnerEmailList = new HashSet<>();
    HashMap<String, String> paramRequest = new HashMap<>();
    CandidateDTO apiResponse = candidateService.getCandidateDetails(candidateId);
    try {
      kgpPartnerEmailList =
          teamMemberList(apiResponse.getSearch().getPartners(), kgpPartnerEmailList);
      kgpPartnerEmailList =
          teamMemberList(apiResponse.getSearch().getRecruiters(), kgpPartnerEmailList);
      kgpPartnerEmailList =
          teamMemberList(apiResponse.getSearch().getResearchers(), kgpPartnerEmailList);
      kgpPartnerEmailList = teamMemberList(apiResponse.getSearch().getEas(), kgpPartnerEmailList);
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
            kgpTeamMeberDetails.split("##")[1], request, paramRequest);
      }
    } catch (Exception ex) {
      log.info(ex);
      throw new APIException("Error in send upload notification email");
    }

  }


  private void sendClientUploadNotificationMail(String email, String partnerName,
      HttpServletRequest request, HashMap<String, String> paramRequest) {
    log.info("sending client upload notification email");
    String locate = "en_US";
    email = "abhishek.jaiswal@aspiresoftserv.com";
    try {
      Map<String, String> staticContentsMap =
          StaticContentsMultiLanguageUtil.getStaticContentsMap(locate, Constant.EMAILS_CONTENT_MAP);
      String mailSubject = staticContentsMap.get("candidate.suite.upload.email.subject");
      mailService.sendEmail(email, null,
          mailSubject + " " + paramRequest.get("type") + " - " + "Uploaded from "
              + paramRequest.get("candidateName"),
          mailService.getUploadEmailContent(request, staticContentsMap,
              Constant.CANDIDATE_UPLOAD_EMAIL_TEMPLATE, partnerName, paramRequest),
          null);
    } catch (Exception e) {
      log.info(e);
      throw new APIException("Error in sending candidate upload email");
    }
    log.info("Client upload Mail sent to all partners successfully.");
  }

  private Set<String> teamMemberList(List<UserDTO> users, Set<String> partnerEmailList) {
    log.info("Creating Team member email and name set");
    for (UserDTO user : users) {
      if (user != null && CommonUtil.checkNotNullString(user.getId())) {
        partnerEmailList.add(user.getEmail() + "##" + user.getName());
      }
    }
    return partnerEmailList;
  }

}
