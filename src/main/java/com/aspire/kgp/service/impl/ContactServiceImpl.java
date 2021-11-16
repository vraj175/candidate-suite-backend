package com.aspire.kgp.service.impl;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.aspire.kgp.model.Contact;
import com.aspire.kgp.model.Reference;
import com.aspire.kgp.model.User;
import com.aspire.kgp.repository.BoardHistoryRepository;
import com.aspire.kgp.repository.JobHistoryRepository;
import com.aspire.kgp.repository.ReferenceRepository;
import com.aspire.kgp.service.CandidateService;
import com.aspire.kgp.service.ContactService;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.util.CommonUtil;
import com.aspire.kgp.util.RestUtil;
import com.aspire.kgp.util.StaticContentsMultiLanguageUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

  @Autowired
  UserService userService;

  @Autowired
  ReferenceRepository referenceRepository;

  @Autowired
  BoardHistoryRepository boardHistoryRepository;

  @Autowired
  JobHistoryRepository jobHistoryRepository;

  @Value("${galaxy.base.api.url}")
  private String baseApiUrl;

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
  public String updateContactDetails(String contactId, String contactData,
      HttpServletRequest request, String candidateId) throws UnsupportedEncodingException {
    JsonObject json = (JsonObject) JsonParser.parseString(contactData);
    try {
      Contact contact = new Gson().fromJson(json, new TypeToken<Contact>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
      contact.getJobHistory().stream().forEach(e -> {
        jobHistoryRepository.save(e);
      });
      contact.getBoardHistory().stream().forEach(e -> {
        boardHistoryRepository.save(e);
      });
      sentUploadNotification(contactId, request, candidateId, "Contact Details");
    } catch (Exception e) {
      throw new APIException("Error While converting data from request json " + e.getMessage());
    }
    try {
      // JsonArray eductionArray = json.getAsJsonObject().getAsJsonArray("education_details");
      json.remove("jobHistory");
      json.remove("boardHistory");
      // JsonObject educationObj = new JsonObject();
      // educationObj.add("education_details", eductionArray);
      return restUtil.putMethod(Constant.CONTACT_URL.replace("{contactId}", contactId),
          json.toString());
    } catch (IOException e) {
      throw new APIException("Error While update education details in galaxy" + e.getMessage());
    }
  }

  @Override
  public String updateContactEducationDetails(String contactId, String contactData) {
    try {
      Gson gson = new Gson();
      JsonElement element = gson.fromJson(contactData, JsonElement.class);
      JsonArray jsonObj = element.getAsJsonArray();
      JsonObject educationObj = new JsonObject();
      educationObj.add("education_details", jsonObj);
      return restUtil.putMethod(Constant.CONTACT_URL.replace("{contactId}", contactId),
          educationObj.toString());
    } catch (UnsupportedEncodingException e) {
      throw new APIException("Error While update education details in galaxy" + e.getMessage());
    }
  }

  @Override
  public ResponseEntity<Object> deleteJobHistoryById(String id) {
    try {
      jobHistoryRepository.deleteById(Long.parseLong(id));
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, "200");
      body.put(Constant.MESSAGE, "Job History data deleted successfully with id:- " + id);
      return new ResponseEntity<>(body, HttpStatus.OK);
    } catch (Exception e) {
      throw new APIException("Error in delete Job History data with id:- " + id);
    }
  }

  @Override
  public ResponseEntity<Object> deleteBoardHistoryById(String id) {
    try {
      boardHistoryRepository.deleteById(Long.parseLong(id));
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, "200");
      body.put(Constant.MESSAGE, "Board History data deleted successfully with id:- " + id);
      return new ResponseEntity<>(body, HttpStatus.OK);
    } catch (Exception e) {
      throw new APIException("Error in delete Board History data with id:- " + id);
    }
  }



  @Override
  public Reference saveAndUpdateContactReference(String referenceId, String referenceData,
      String contactId, HttpServletRequest request, String candidateId)
      throws UnsupportedEncodingException {
    Reference reference = new Reference();
    try {
      reference = new Gson().fromJson(referenceData, new TypeToken<Reference>() {

        /**
         * 
         * //
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
    if (referenceId != null && !referenceId.isEmpty() && contactId == null) {
      Reference referenceDatas = referenceRepository.findByIdAndContactId(Long.valueOf(referenceId),
          reference.getContactId());
      if (referenceDatas != null) {
        referenceDatas.setCompanyName(reference.getCompanyName());
        referenceDatas.setContactId(reference.getContactId());
        referenceDatas.setPhone(reference.getPhone());
        referenceDatas.setRefContactName(reference.getRefContactName());
        referenceDatas.setRefType(reference.getRefType());
        referenceDatas.setRelationship(reference.getRelationship());
        referenceDatas.setSearchName(reference.getSearchName());
        referenceDatas.setTitle(reference.getTitle());
        referenceDatas.setEmail(reference.getEmail());
        referenceDatas.setSearchId(reference.getSearchId());
        referenceDatas.setWorkEmail(reference.getWorkEmail());
        referenceDatas.setModifyDate(new Timestamp(System.currentTimeMillis()));
        referenceRepository.save(referenceDatas);
        sentUploadNotification(reference.getContactId(), request, candidateId, "Reference Details");
      } else {
        throw new APIException(
            "Reference Details not found for this reference Id: - " + referenceId);
      }
    } else {
      referenceRepository.save(reference);
      sentUploadNotification(reference.getContactId(), request, candidateId, "Reference Details");
    }
    return reference;
  }



  @Override
  public final String addContactReference(String contactId, String referenceData)
      throws UnsupportedEncodingException {
    Reference reference = new Reference();
    try {
      reference = new Gson().fromJson(referenceData, new TypeToken<Reference>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
    } catch (JsonSyntaxException e) {
      throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
    }
    if (!contactId.isEmpty() && reference != null) {
      referenceRepository.save(reference);
    }
    return "Data Added Successfully";

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
      File parent = new File(System.getProperty("java.io.tmpdir"));
      file = new File(parent, fileName);
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
  public DocumentDTO getContactResumes(String contactId) {
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
  public List<Reference> getListOfReferences(String contactId) {
    List<ContactReferencesDTO> contactReferencesDTOList = new ArrayList<>();
    List<Reference> referenceList = new ArrayList<>();
    List<Reference> referenceLists = new ArrayList<>();
    referenceList = referenceRepository.findByContactId(contactId);
    if (!referenceList.isEmpty()) {
      return referenceList;
    } else {
      String apiResponse = restUtil
          .newGetMethod(Constant.CONTACT_REFERENCE_URL.replace(Constant.CONTACT_ID, contactId));

      try {
        contactReferencesDTOList =
            new Gson().fromJson(apiResponse, new TypeToken<List<ContactReferencesDTO>>() {

              /**
               * 
               */
              private static final long serialVersionUID = 1L;
            }.getType());
      } catch (JsonSyntaxException e) {
        throw new APIException(Constant.JSON_PROCESSING_EXCEPTION + e.getMessage());
      }
      if (!contactReferencesDTOList.isEmpty()) {
        addContactReferenceDataIntDB(contactReferencesDTOList);
      }
      referenceList = referenceRepository.findByContactId(contactId);
      if (!referenceList.isEmpty()) {
        return referenceList;
      } else {

        return referenceLists;
      }

    }
  }

  private void addContactReferenceDataIntDB(List<ContactReferencesDTO> contactReferencesDTOList) {
    for (int i = 0; i < contactReferencesDTOList.size(); i++) {
      Reference reference = new Reference();
      if (contactReferencesDTOList.get(i) != null) {
        reference.setPhone(contactReferencesDTOList.get(i).getContact().getMobilePhone());
        reference.setRefType(contactReferencesDTOList.get(i).getType());
        reference.setSearchName(contactReferencesDTOList.get(i).getSearch().getJobTitle());
        reference.setRelationship(contactReferencesDTOList.get(i).getRelationship());
        reference.setEmail(contactReferencesDTOList.get(i).getContact().getEmail());
        reference.setContactId(contactReferencesDTOList.get(i).getContactId());
        reference.setRefContactName(contactReferencesDTOList.get(i).getContact().getFirstName()
            + " " + contactReferencesDTOList.get(i).getContact().getLastName());
        reference
            .setCompanyName(contactReferencesDTOList.get(i).getContact().getCompany().getName());
        reference.setTitle(contactReferencesDTOList.get(i).getContact().getCurrentJobTitle());
        reference.setSearchId(contactReferencesDTOList.get(i).getSearch().getId());
        reference.setWorkEmail(contactReferencesDTOList.get(i).getContact().getWorkEmail());
        referenceRepository.save(reference);
      }
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
    if (type.equalsIgnoreCase(Constant.OFFER_LETTER)) {
      type = Constant.OFFER_LETTERS;
    }
    CandidateDTO apiResponse = candidateService.getCandidateDetails(candidateId);
    try {
      kgpPartnerEmailList = CommonUtil.teamPartnerMemberList(apiResponse.getSearch().getPartners(),
          kgpPartnerEmailList);
      kgpPartnerEmailList =
          CommonUtil.teamMemberList(apiResponse.getSearch().getRecruiters(), kgpPartnerEmailList);
      paramRequest.put("candidateName",
          apiResponse.getContact().getFirstName() + " " + apiResponse.getContact().getLastName());
      paramRequest.put("searchId", apiResponse.getSearch().getId());
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
    User user = (User) request.getAttribute("user");
    String role = user.getRole().getName();
    paramRequest.put("role", role);
    String locate = "en_US";
    try {
      Map<String, String> staticContentsMap =
          StaticContentsMultiLanguageUtil.getStaticContentsMap(locate, Constant.EMAILS_CONTENT_MAP);
      String mailSubject = staticContentsMap.get("candidate.suite.upload.email.subject");
      UserDTO userDTO = null;
      String content = "";
      String access = "";
      if (Constant.PARTNER.equalsIgnoreCase(role)) {
        userDTO = userService.getGalaxyUserDetails(user.getGalaxyId());
        if (paramRequest.get("type").equalsIgnoreCase("Reference Details")) {
          mailSubject = mailSubject + " - " + paramRequest.get("type") + " " + "Added/Updated from "
              + userDTO.getFirstName() + " " + userDTO.getLastName();
          content = userDTO.getFirstName() + " " + userDTO.getLastName() + " has added or updated "
              + paramRequest.get("candidateName") + "'s";
          paramRequest.put("clickButtonUrl",
              CommonUtil.getServerUrl(request) + request.getContextPath() + "/my-info/"
                  + paramRequest.get("candidateId") + "/" + paramRequest.get("searchId") + "/"
                  + paramRequest.get("searchName") + "/" + paramRequest.get("contactId")
                  + "?activeTab=references-tab");
          access = " to access in Candidate Suite";
        }

        else if (paramRequest.get("type").equalsIgnoreCase("Contact Details")) {
          mailSubject = mailSubject + " - " + paramRequest.get("type") + " " + "Added/Updated from "
              + userDTO.getFirstName() + " " + userDTO.getLastName();
          content = userDTO.getFirstName() + " " + userDTO.getLastName() + " has added or updated "
              + paramRequest.get("candidateName") + "'s";
          paramRequest.put("clickButtonUrl",
              CommonUtil.getServerUrl(request) + request.getContextPath() + "/my-info/"
                  + paramRequest.get("candidateId") + "/" + paramRequest.get("searchId") + "/"
                  + paramRequest.get("searchName") + "/" + paramRequest.get("contactId"));
          access = " to access in Candidate Suite";
        }

        else {
          mailSubject = mailSubject + " - " + paramRequest.get("type") + " " + "Uploaded from "
              + userDTO.getFirstName() + " " + userDTO.getLastName();
          content = userDTO.getFirstName() + " " + userDTO.getLastName() + " has uploaded "
              + paramRequest.get("candidateName") + "'s";

          paramRequest.put("clickButtonUrl",
              baseApiUrl.replace("api", "contacts") + "/" + paramRequest.get("contactId"));
          access = " to access in Galaxy";
        }
        paramRequest.put("content", content);
        paramRequest.put("access", access);
        paramRequest.put("clientName", userDTO.getFirstName() + " " + userDTO.getLastName());

      } else {
        userDTO = userService.getContactDetails(user.getGalaxyId());
        if (paramRequest.get("type").equalsIgnoreCase("Reference Details")) {
          mailSubject = mailSubject + " - " + paramRequest.get("type") + " " + "Added/Updated from "
              + paramRequest.get("candidateName");
          content = paramRequest.get("candidateName") + " has added or updated their ";
          paramRequest.put("clickButtonUrl",
              CommonUtil.getServerUrl(request) + request.getContextPath() + "/my-info/"
                  + paramRequest.get("candidateId") + "/" + paramRequest.get("searchId") + "/"
                  + paramRequest.get("searchName") + "/" + paramRequest.get("contactId")
                  + "?activeTab=references-tab");
          access = " to access in Candidate Suite";
        } else if (paramRequest.get("type").equalsIgnoreCase("Contact Details")) {
          mailSubject = mailSubject + " - " + paramRequest.get("type") + " " + "Added/Updated from "
              + paramRequest.get("candidateName");
          content = paramRequest.get("candidateName") + " has added or updated their ";
          paramRequest.put("clickButtonUrl",
              CommonUtil.getServerUrl(request) + request.getContextPath() + "/my-info/"
                  + paramRequest.get("candidateId") + "/" + paramRequest.get("searchId") + "/"
                  + paramRequest.get("searchName") + "/" + paramRequest.get("contactId"));
          access = " to access in Candidate Suite";
        } else {
          mailSubject = mailSubject + " - " + paramRequest.get("type") + " " + "Uploaded from "
              + paramRequest.get("candidateName");
          content = paramRequest.get("candidateName") + " has uploaded their ";
          paramRequest.put("clickButtonUrl",
              baseApiUrl.replace("api", "contacts") + "/" + paramRequest.get("contactId"));
          access = " to access in Galaxy";
        }
        paramRequest.put("content", content);
        paramRequest.put("access", access);
        paramRequest.put("clientName", paramRequest.get("candidateName"));
      }
      mailService.sendEmail(email, null, mailSubject, mailService.getUploadEmailContent(request,
          staticContentsMap, Constant.CANDIDATE_UPLOAD_EMAIL_TEMPLATE, partnerName, paramRequest),
          null);
    } catch (Exception e) {
      log.info(e);
      throw new APIException("Error in sending candidate upload email");
    }
    log.info("Client upload Mail sent to all partners successfully.");
  }

}
