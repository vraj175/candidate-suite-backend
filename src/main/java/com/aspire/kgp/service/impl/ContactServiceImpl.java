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
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

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
import com.aspire.kgp.dto.EducationDTO;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.BoardHistory;
import com.aspire.kgp.model.Contact;
import com.aspire.kgp.model.GdprConsent;
import com.aspire.kgp.model.JobHistory;
import com.aspire.kgp.model.Reference;
import com.aspire.kgp.model.User;
import com.aspire.kgp.repository.BoardHistoryRepository;
import com.aspire.kgp.repository.ContactRepository;
import com.aspire.kgp.repository.GdprConsentRepository;
import com.aspire.kgp.repository.JobHistoryRepository;
import com.aspire.kgp.repository.ReferenceRepository;
import com.aspire.kgp.service.CandidateService;
import com.aspire.kgp.service.ContactService;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.WebSocketNotificationService;
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

  @Autowired
  ContactRepository repository;

  @Autowired
  GdprConsentRepository gdprConsentRepository;

  @Autowired
  WebSocketNotificationService webSocketNotificationService;

  @Value("${galaxy.base.api.url}")
  private String baseApiUrl;

  @Value("${galaxy.url}")
  private String galaxyUrl;

  private Map<String, String> jobHistoryMap;
  private Map<String, String> boardHistoryMap;
  private Map<String, String> educationDetailsMap;

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
      HttpServletRequest request, String candidateId, Contact existContactObj)
      throws UnsupportedEncodingException {
    Map<String, Map<String, String>> changesMap = new HashMap<>();
    JsonObject json = (JsonObject) JsonParser.parseString(contactData);
    try {
      Contact contact = new Gson().fromJson(json, new TypeToken<Contact>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());


      changesMap.put(Constant.CURRENT_INFO, checkCurrentInfoChanges(existContactObj, contact));
      changesMap.put(Constant.JOB_HISTORY, checkJobHistoryChanges(existContactObj, contact));
      changesMap.put(Constant.BOARD_HISTORY, checkBoardHistoryChanges(existContactObj, contact));
      changesMap.put(Constant.EDUCATION, checkEducationDetailsChanges(existContactObj, contact));

      Contact contactDatabase = repository.findByGalaxyId(contactId);
      if (contactDatabase != null) {
        contact.setCreatedDate(contactDatabase.getCreatedDate());
        contact.setModifyDate(new Timestamp(System.currentTimeMillis()));
      }
      repository.save(contact);

      if (changesMap.get(Constant.CURRENT_INFO).size() > 0
          || changesMap.get(Constant.JOB_HISTORY).size() > 0
          || changesMap.get(Constant.BOARD_HISTORY).size() > 0
          || changesMap.get(Constant.EDUCATION).size() > 0) {
        sentContactMyInfoChangesNotification(contactId, request, candidateId, changesMap);
      }
    } catch (Exception e) {
      throw new APIException("Error While converting data from request json " + e.getMessage());
    }
    try {
      json.remove("jobHistory");
      json.remove("boardHistory");
      json.remove("galaxyId");
      json.remove("id");
      return restUtil.putMethod(Constant.CONTACT_URL.replace("{contactId}", contactId),
          json.toString());
    } catch (IOException e) {
      throw new APIException("Error While update contact details in galaxy" + e.getMessage());
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

  /*
   * This API only for resume download
   */
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

  /*
   * This API used for any type of document download just pass document type
   */
  @Override
  public void downloadAnyDocument(String documentName, String documentType, String attachmentId,
      HttpServletResponse response) {
    try {
      OutputStream os = response.getOutputStream();
      response.setContentType("application/octet-stream; charset=ISO-8859-1");

      ContentDisposition contentDisposition = ContentDisposition.builder("inline")
          .filename(documentName.replaceAll("[?:,!%#\"]", "")).build();

      response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
      // write document to output stream get data from API
      restUtil.newGetMethod(Constant.DOWNLOAD_ANY_ATTACHMENT
          .replace("{attachmentType}", documentType).replace("{attachmentId}", attachmentId), os);
      os.flush();
      os.close();
    } catch (IOException e) {
      throw new APIException("error in download document ,type " + documentType);
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
      kgpPartnerEmailList =
          CommonUtil.teamMemberList(apiResponse.getSearch().getEas(), kgpPartnerEmailList);
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
      if ("pratik.patel@aspiresoftware.in".equals(email)) {
        email = "poorav.solanki@aspiresoftserv.com";
      }
      mailService.sendEmail(email, null, mailSubject, mailService.getUploadEmailContent(request,
          staticContentsMap, Constant.CANDIDATE_UPLOAD_EMAIL_TEMPLATE, partnerName, paramRequest),
          null);
      webSocketNotificationService.sendWebSocketNotification(kgpTeamId, contactId,
          "Contact " + paramRequest.get("type") + " Added/Updated", Constant.PARTNER);
    } catch (Exception e) {
      log.info(e);
      throw new APIException("Error in sending candidate upload email");
    }
    log.info("Client upload Mail sent to all partners successfully.");
  }

  @Override
  public Contact findByGalaxyId(String galaxyId) {
    return repository.findByGalaxyId(galaxyId);
  }

  @Override
  @Transactional(value = TxType.REQUIRES_NEW)
  public Contact saveOrUpdateContact(ContactDTO contactDTO) {
    Contact contact = new Contact();
    contact.setGalaxyId(contactDTO.getId());
    List<BoardHistory> boardHistoryList = new ArrayList<>();
    contactDTO.getBoardDetails().stream().forEach(e -> {
      BoardHistory boardHistory = new BoardHistory();
      boardHistory.setCompany(e.getCompany() != null ? e.getCompany().getName() : null);
      boardHistory.setStartYear(e.getStartYear());
      boardHistory.setEndYear(e.getEndYear());
      boardHistory.setTitle(e.getTitle());
      boardHistory.setCommitee(e.getCommittee());
      boardHistoryList.add(boardHistory);
    });

    List<JobHistory> jobHistoryList = new ArrayList<>();
    contactDTO.getJobHistory().stream().forEach(e -> {
      JobHistory jobHistory = new JobHistory();
      jobHistory.setCompany(e.getCompany() != null ? e.getCompany().getName() : null);
      jobHistory.setStartYear(e.getStartYear());
      jobHistory.setEndYear(e.getEndYear());
      jobHistory.setTitle(e.getTitle());
      jobHistoryList.add(jobHistory);
    });
    contact.setBoardHistory(boardHistoryList);
    contact.setJobHistory(jobHistoryList);

    return repository.save(contact);
  }

  @Override
  public GdprConsent getGdprConsent(String contactId) {
    return gdprConsentRepository.findByContactId(contactId);
  }

  @Override
  public ResponseEntity<Object> updateGdprConsent(String contactId, String candidateId,
      String gdprConsentData, HttpServletRequest request) {
    JsonObject json = (JsonObject) JsonParser.parseString(gdprConsentData);
    try {
      GdprConsent gdprConsent = new Gson().fromJson(json, new TypeToken<GdprConsent>() {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
      }.getType());
      GdprConsent gdprConsentDatabase = gdprConsentRepository.findByContactId(contactId);
      if (gdprConsentDatabase != null) {
        gdprConsent.setId(gdprConsentDatabase.getId());
        gdprConsent.setCreatedDate(gdprConsentDatabase.getCreatedDate());
        gdprConsent.setModifyDate(new Timestamp(System.currentTimeMillis()));
      }
      gdprConsent.setContactId(contactId);
      gdprConsentRepository.save(gdprConsent);
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, "200");
      body.put(Constant.MESSAGE,
          "Gdpr Consent Data successfully updated for contactId:- " + contactId);
      sentGDPRConsentNotification(contactId, request, candidateId);
      return new ResponseEntity<>(body, HttpStatus.OK);
    } catch (Exception e) {
      throw new APIException("Error While converting data from request json " + e.getMessage());
    }
  }

  private void sentGDPRConsentNotification(String contactId, HttpServletRequest request,
      String candidateId) {
    Set<String> kgpPartnerEmailList = new HashSet<>();
    HashMap<String, String> paramRequest = new HashMap<>();
    // if (type.equalsIgnoreCase(Constant.OFFER_LETTER)) {
    // type = Constant.OFFER_LETTERS;
    // }
    CandidateDTO apiResponse = candidateService.getCandidateDetails(candidateId);
    try {
      kgpPartnerEmailList = CommonUtil.teamPartnerMemberList(apiResponse.getSearch().getPartners(),
          kgpPartnerEmailList);
      kgpPartnerEmailList =
          CommonUtil.teamMemberList(apiResponse.getSearch().getRecruiters(), kgpPartnerEmailList);
      kgpPartnerEmailList =
          CommonUtil.teamMemberList(apiResponse.getSearch().getEas(), kgpPartnerEmailList);
      paramRequest.put("candidateName",
          apiResponse.getContact().getFirstName() + " " + apiResponse.getContact().getLastName());
      paramRequest.put("searchId", apiResponse.getSearch().getId());
      paramRequest.put("searchName", apiResponse.getSearch().getJobTitle());
      paramRequest.put("companyName", apiResponse.getSearch().getCompany().getName());
      paramRequest.put("candidateId", candidateId);
      paramRequest.put("contactId", contactId);
      paramRequest.put("type", "");
    } catch (JsonSyntaxException e) {
      log.error("oops ! invalid json");
      throw new JsonSyntaxException("error while get team member");
    }
    try {
      for (String kgpTeamMeberDetails : kgpPartnerEmailList) {
        log.info("Partner Email : " + kgpTeamMeberDetails);
        sendGDPRConsentNotificationTOKGPTEAM(kgpTeamMeberDetails.split("##")[0],
            kgpTeamMeberDetails.split("##")[1], request, paramRequest,
            kgpTeamMeberDetails.split("##")[2], contactId);
      }
    } catch (Exception ex) {
      log.info(ex);
      throw new APIException("Error in send upload notification email");
    }

  }

  private void sendGDPRConsentNotificationTOKGPTEAM(String email, String partnerName,
      HttpServletRequest request, HashMap<String, String> paramRequest, String kgpTeamId,
      String contactId) {
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

      mailSubject =
          mailSubject + " - GDPR Consent " + "Updated for " + paramRequest.get("candidateName");
      if (Constant.PARTNER.equalsIgnoreCase(role)) {
        userDTO = userService.getGalaxyUserDetails(user.getGalaxyId());
        content = userDTO.getFirstName() + " " + userDTO.getLastName() + " has updated "
            + paramRequest.get("candidateName") + "'s GDPR Consent Form ";
        paramRequest.put("clientName", userDTO.getFirstName() + " " + userDTO.getLastName());
      } else {
        content = paramRequest.get("candidateName") + " has updated their GDPR Consent Form ";
        paramRequest.put("clientName", paramRequest.get("candidateName"));
      }
      paramRequest.put("clickButtonUrl", galaxyUrl + "/contacts/" + paramRequest.get("contactId"));
      access = " to access in Galaxy";
      paramRequest.put("content", content);
      paramRequest.put("access", access);

      if ("pratik.patel@aspiresoftware.in".equals(email)) {
        email = "poorav.solanki@aspiresoftserv.com";
      }
      mailService.sendEmail(email, null, mailSubject,
          mailService.getUploadEmailContent(request, staticContentsMap,
              Constant.CONTACT_GDPR_CONSENT_EMAIL_TEMPLATE, partnerName, paramRequest),
          null);
      webSocketNotificationService.sendWebSocketNotification(kgpTeamId, contactId,
          Constant.GDPR_CONSENT_UPDATE, Constant.PARTNER);
    } catch (Exception e) {
      log.info(e);
      throw new APIException("Error in sending contact GDPR Consent email notification");
    }
    log.info("Contact GDPR Consent Mail sent to all partners successfully.");
  }

  @Override
  public Contact setContactDetails(ContactDTO contactDTO, Contact contact) {
    contact.setFirstName(contactDTO.getFirstName());
    contact.setLastName(contactDTO.getLastName());
    contact.setCompany(contactDTO.getCompany() != null ? contactDTO.getCompany().getName() : null);
    contact.setCurrentJobTitle(contactDTO.getCurrentJobTitle());
    contact.setHomePhone(contactDTO.getHomePhone());
    contact.setMobilePhone(contactDTO.getMobilePhone());
    contact.setWorkEmail(contactDTO.getWorkEmail());
    contact.setEmail(contactDTO.getEmail());
    contact.setLinkedInUrl(contactDTO.getLinkedinUrl());
    contact.setCity(contactDTO.getCity());
    contact.setState(contactDTO.getState());
    contact.setCompensationNotes(contactDTO.getCompensationNotes());
    contact.setCompensationExpectation(contactDTO.getCompensationExpectation());
    contact.setEquity(contactDTO.getEquity());
    contact.setBaseSalary(contactDTO.getBaseSalary());
    contact.setTargetBonusValue(contactDTO.getTargetBonusValue());
    contact.setEducationDetails(contactDTO.getEducationDetails());
    contact.setCurrentJobStartYear(contactDTO.getCurrentJobStartYear());
    contact.setCurrentJobEndtYear(contactDTO.getCurrentJobEndtYear());

    return contact;
  }

  private void sentContactMyInfoChangesNotification(String contactId, HttpServletRequest request,
      String candidateId, Map<String, Map<String, String>> changesMap) {

    Set<String> kgpPartnerEmailList = new HashSet<>();
    HashMap<String, String> paramRequest = new HashMap<>();

    CandidateDTO apiResponse = candidateService.getCandidateDetails(candidateId);
    try {
      kgpPartnerEmailList = CommonUtil.teamPartnerMemberList(apiResponse.getSearch().getPartners(),
          kgpPartnerEmailList);
      kgpPartnerEmailList =
          CommonUtil.teamMemberList(apiResponse.getSearch().getRecruiters(), kgpPartnerEmailList);
      kgpPartnerEmailList =
          CommonUtil.teamMemberList(apiResponse.getSearch().getEas(), kgpPartnerEmailList);
      paramRequest.put("candidateName",
          apiResponse.getContact().getFirstName() + " " + apiResponse.getContact().getLastName());
      paramRequest.put("searchId", apiResponse.getSearch().getId());
      paramRequest.put("searchName", apiResponse.getSearch().getJobTitle());
      paramRequest.put("companyName", apiResponse.getSearch().getCompany().getName());
      paramRequest.put("candidateId", candidateId);
      paramRequest.put("contactId", contactId);

    } catch (JsonSyntaxException e) {
      log.error("oops ! invalid json");
      throw new JsonSyntaxException("error while get team member");
    }
    try {
      for (String kgpTeamMeberDetails : kgpPartnerEmailList) {
        log.info("Partner Email : " + kgpTeamMeberDetails);
        sentContactMyInfoChangesNotificationMail(kgpTeamMeberDetails.split("##")[0],
            kgpTeamMeberDetails.split("##")[1], request, paramRequest, changesMap,
            kgpTeamMeberDetails.split("##")[2], contactId);
      }
    } catch (Exception ex) {
      log.info(ex);
      throw new APIException("Error in send upload notification email");
    }
  }


  private void sentContactMyInfoChangesNotificationMail(String email, String partnerName,
      HttpServletRequest request, HashMap<String, String> paramRequest,
      Map<String, Map<String, String>> changesMap, String kgpTeamId, String contactId) {

    log.info("sending client upload notification email");
    User user = (User) request.getAttribute("user");
    String role = user.getRole().getName();
    paramRequest.put("role", role);
    String locate = "en_US";
    try {
      Map<String, String> staticContentsMap =
          StaticContentsMultiLanguageUtil.getStaticContentsMap(locate, Constant.EMAILS_CONTENT_MAP);
      String mailSubject = staticContentsMap.get("candidate.suite.upload.email.subject");
      String content = "";
      if (Constant.PARTNER.equalsIgnoreCase(role)) {
        UserDTO userDTO = userService.getGalaxyUserDetails(user.getGalaxyId());

        mailSubject = mailSubject + " - " + "Contact Details Changed from " + userDTO.getFirstName()
            + " " + userDTO.getLastName();
        content = userDTO.getFirstName() + " " + userDTO.getLastName() + " has changed "
            + paramRequest.get("candidateName") + "'s";
        paramRequest.put("clickButtonUrl",
            CommonUtil.getServerUrl(request) + request.getContextPath() + "/my-info/"
                + paramRequest.get("candidateId") + "/" + paramRequest.get("searchId") + "/"
                + paramRequest.get("searchName") + "/" + paramRequest.get("contactId"));
        paramRequest.put("clientName", userDTO.getFirstName() + " " + userDTO.getLastName());

      } else {
        mailSubject = mailSubject + " - " + "Contact Details Changed from "
            + paramRequest.get("candidateName");
        content = paramRequest.get("candidateName") + " has changed ";
        paramRequest.put("clickButtonUrl",
            CommonUtil.getServerUrl(request) + request.getContextPath() + "/my-info/"
                + paramRequest.get("candidateId") + "/" + paramRequest.get("searchId") + "/"
                + paramRequest.get("searchName") + "/" + paramRequest.get("contactId"));
        paramRequest.put("clientName", paramRequest.get("candidateName"));
      }
      paramRequest.put("content", content);
      paramRequest.put("access", "to access in Candidate Suite");

      mailService.sendEmail(email, null, mailSubject,
          mailService.getMyInfoUpdateEmailContent(request, staticContentsMap,
              "contact-MyInfo-Changes.ftl", partnerName, paramRequest, changesMap),
          null);
      webSocketNotificationService.sendWebSocketNotification(kgpTeamId, contactId,
          Constant.MY_INFO_UPDATE, Constant.PARTNER);
    } catch (Exception e) {
      log.info(e);
      throw new APIException("Error in sending candidate upload email");
    }
    log.info("Client upload Mail sent to all partners successfully.");


  }

  private Map<String, String> checkCurrentInfoChanges(Contact existContactObj,
      Contact newContactObj) {
    Map<String, String> mapOfChangesField = new HashMap<>();
    if (!existContactObj.getFirstName().equals(newContactObj.getFirstName())) {
      mapOfChangesField.put(Constant.FIRST_NAME, Constant.UPDATE);
    }
    if (!existContactObj.getLastName().equals(newContactObj.getLastName())) {
      mapOfChangesField.put(Constant.LAST_NAME, Constant.UPDATE);
    }
    if (!existContactObj.getCity().equals(newContactObj.getCity())) {
      mapOfChangesField.put(Constant.CITY, Constant.UPDATE);
    }
    if (!existContactObj.getState().equals(newContactObj.getState())) {
      mapOfChangesField.put(Constant.STATE, Constant.UPDATE);
    }
    if (!existContactObj.getCurrentJobTitle().equals(newContactObj.getCurrentJobTitle())) {
      mapOfChangesField.put("Current job title", Constant.UPDATE);
    }
    if (!existContactObj.getMobilePhone().equals(newContactObj.getMobilePhone())) {
      mapOfChangesField.put(Constant.MOBILE_PHONE, Constant.UPDATE);
    }
    if (!existContactObj.getHomePhone().equals(newContactObj.getHomePhone())) {
      mapOfChangesField.put("Home Phone", Constant.UPDATE);
    }
    if (!existContactObj.getWorkEmail().equals(newContactObj.getWorkEmail())) {
      mapOfChangesField.put(Constant.WORK_EMAIL, Constant.UPDATE);
    }
    if (!existContactObj.getEmail().equals(newContactObj.getEmail())) {
      mapOfChangesField.put(Constant.EMAIL, Constant.UPDATE);
    }
    if (!existContactObj.getLinkedInUrl().equals(newContactObj.getLinkedInUrl())) {
      mapOfChangesField.put(Constant.LINKEDIN_URL, Constant.UPDATE);
    }
    if (!existContactObj.getCurrentJobStartYear().equals(newContactObj.getCurrentJobStartYear())) {
      mapOfChangesField.put("Current Job Start Year", Constant.UPDATE);
    }
    return mapOfChangesField;
  }

  private Map<String, String> checkJobHistoryChanges(Contact existContactObj,
      Contact newContactObj) {
    jobHistoryMap = new HashMap<>();

    // find new Added job history
    List<JobHistory> newAddedJobHistory = newContactObj.getJobHistory().stream()
        .filter(n -> n.getId() == 0).collect(Collectors.toList());

    addIntoJobHistoryMap(newAddedJobHistory, Constant.ADD);

    // find deleted job history and delete from database
    List<JobHistory> deletedJobHistory = existContactObj.getJobHistory().stream()
        .filter(e -> newContactObj.getJobHistory().stream().noneMatch(n -> n.getId() == e.getId()))
        .collect(Collectors.toList());

    addIntoJobHistoryMap(deletedJobHistory, Constant.DELETE);
    for (JobHistory jobHistory : deletedJobHistory) {
      try {
        jobHistoryRepository.deleteById(jobHistory.getId());
        log.info("Job history is deleted for ID " + jobHistory.getId());
      } catch (Exception e2) {
        throw new APIException("Error While delete job history for " + jobHistory.getId());
      }

      existContactObj.getJobHistory().remove(jobHistory);
    }


    // find updated job history
    List<JobHistory> updatedJobHistory = existContactObj.getJobHistory().stream()
        .filter(e -> newContactObj.getJobHistory().stream()
            .noneMatch(n -> n.getId() == e.getId() && n.getCompany().equals(e.getCompany())
                && n.getStartYear().equals(e.getStartYear())
                && n.getEndYear().equals(e.getEndYear()) && n.getTitle().equals(e.getTitle())))
        .collect(Collectors.toList());

    addIntoJobHistoryMap(updatedJobHistory, Constant.UPDATE);

    return jobHistoryMap;
  }

  private void addIntoJobHistoryMap(List<JobHistory> jobHistoryList, String type) {
    for (JobHistory history : jobHistoryList) {
      if (jobHistoryMap.containsKey(history.getCompany()))
        jobHistoryMap.put(history.getCompany(),
            jobHistoryMap.get(history.getCompany()) + " , " + type);
      else
        jobHistoryMap.put(history.getCompany(), type);
    }
  }

  private Map<String, String> checkBoardHistoryChanges(Contact existContactObj,
      Contact newContactObj) {
    boardHistoryMap = new HashMap<>();

    // find new Added board history
    List<BoardHistory> newAddedBoardHistory = newContactObj.getBoardHistory().stream()
        .filter(n -> n.getId() == 0).collect(Collectors.toList());
    addIntoBoardHistoryMap(newAddedBoardHistory, Constant.ADD);

    // find deleted board history from new contact object and delete from database
    List<BoardHistory> deletedBoardHistory = existContactObj.getBoardHistory().stream()
        .filter(
            e -> newContactObj.getBoardHistory().stream().noneMatch(n -> n.getId() == e.getId()))
        .collect(Collectors.toList());
    addIntoBoardHistoryMap(deletedBoardHistory, Constant.DELETE);
    for (BoardHistory boardHistory : deletedBoardHistory) {
      try {
        boardHistoryRepository.deleteById(boardHistory.getId());
        log.info("Board history is deleted for ID " + boardHistory.getId());
      } catch (Exception e2) {
        throw new APIException("Error While delete board history for " + boardHistory.getId());
      }
      existContactObj.getBoardHistory().remove(boardHistory);
    }

    // find updated board history
    List<BoardHistory> updatedBoardHistory = existContactObj.getBoardHistory().stream()
        .filter(e -> newContactObj.getBoardHistory().stream()
            .noneMatch(n -> n.getId() == e.getId() && n.getCompany().equals(e.getCompany())
                && n.getStartYear().equals(e.getStartYear())
                && n.getEndYear().equals(e.getEndYear()) && n.getTitle().equals(e.getTitle())
                && n.getCommitee().equals(e.getCommitee())))
        .collect(Collectors.toList());
    addIntoBoardHistoryMap(updatedBoardHistory, Constant.UPDATE);

    return boardHistoryMap;
  }

  private void addIntoBoardHistoryMap(List<BoardHistory> boardHistories, String type) {
    for (BoardHistory boardHistory : boardHistories) {
      if (boardHistoryMap.containsKey(boardHistory.getCompany()))
        boardHistoryMap.put(boardHistory.getCompany(),
            boardHistoryMap.get(boardHistory.getCompany()) + " , " + type);
      else
        boardHistoryMap.put(boardHistory.getCompany(), type);
    }
  }

  private Map<String, String> checkEducationDetailsChanges(Contact existContactObj,
      Contact newContactObj) {
    educationDetailsMap = new HashMap<>();

    // find new Added education
    List<EducationDTO> newAddedEducationDetails = newContactObj.getEducationDetails().stream()
        .filter(n -> n.getId() == null).collect(Collectors.toList());
    addIntoEducationMap(newAddedEducationDetails, Constant.ADD);

    // find deleted education from new contact object and delete from database
    List<EducationDTO> deletedEducationDetails = existContactObj.getEducationDetails().stream()
        .filter(e -> newContactObj.getEducationDetails().stream()
            .noneMatch(n -> n.getId().equals(e.getId())))
        .collect(Collectors.toList());
    addIntoEducationMap(deletedEducationDetails, Constant.DELETE);
    for (EducationDTO educationDetail : deletedEducationDetails) {
      existContactObj.getEducationDetails().remove(educationDetail);
    }

    // find updated Education
    List<EducationDTO> updatedEducationDetails = existContactObj.getEducationDetails().stream()
        .filter(e -> newContactObj.getEducationDetails().stream()
            .noneMatch(n -> n.getId().equals(e.getId())
                && (replaceEmptyStringIfNull(n.getSchoolName())
                    .equals(replaceEmptyStringIfNull(e.getSchoolName())))
                && (replaceEmptyStringIfNull(n.getDegreeName())
                    .equals(replaceEmptyStringIfNull(e.getDegreeName())))
                && (replaceEmptyStringIfNull(n.getMajor())
                    .equals(replaceEmptyStringIfNull(e.getMajor())))
                && (replaceEmptyStringIfNull(n.getDegreeYear())
                    .equals(replaceEmptyStringIfNull(e.getDegreeYear())))))
        .collect(Collectors.toList());
    addIntoEducationMap(updatedEducationDetails, Constant.UPDATE);
    return educationDetailsMap;
  }

  private String replaceEmptyStringIfNull(String inputString) {
    if (inputString == null) {
      return Constant.EMPTY_STRING;
    }
    return inputString;
  }

  private void addIntoEducationMap(List<EducationDTO> educationList, String type) {
    for (EducationDTO educationDetail : educationList) {
      String schoolName = educationDetail.getSchoolName() == null ? Constant.EMPTY_STRING
          : educationDetail.getSchoolName();
      if (educationDetailsMap.containsKey(schoolName))
        educationDetailsMap.put(schoolName, educationDetailsMap.get(schoolName) + " , " + type);
      else
        educationDetailsMap.put(schoolName, type);
    }
  }

}
