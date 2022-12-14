package com.aspire.kgp.controller;


import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.dto.DocumentDTO;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.model.Contact;
import com.aspire.kgp.model.GdprConsent;
import com.aspire.kgp.model.Reference;
import com.aspire.kgp.repository.BoardHistoryRepository;
import com.aspire.kgp.repository.JobHistoryRepository;
import com.aspire.kgp.service.ContactService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Tag(name = "Contact", description = "Rest API For Contact")
public class ContactController {
  static Log log = LogFactory.getLog(ContactController.class.getName());

  @Autowired
  ContactService service;

  @Autowired
  BoardHistoryRepository boardHistoryRepository;

  @Autowired
  JobHistoryRepository jobHistoryRepository;

  @Operation(summary = "Get Contact Details")
  @GetMapping("/contact/{contactId}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "ContactDTO",
          example = "{\"id\": 0,\"createdDate\": \"yyyy-mm-dd HH:MM:SS\",\"modifyDate\": \"yyyy-mm-dd HH:MM:SS\",\"contactId\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\",\"city\": \"string\",\"state\": \"string\",\"company\": \"string\",\"currentJobTitle\": \"string\",\"mobilePhone\": \"string\",\"homePhone\": \"string\",\"workEmail\": \"string\",\"email\": \"string\",\"linkedInUrl\": \"string\",\"compensationNotes\": \"string\",\"compensationExpectation\": \"string\",\"equity\": \"string\",\"baseSalary\": \"string\",\"targetBonusValue\": \"string\",\"boardHistory\": [{\"id\": 0,\"createdDate\": \"yyyy-mm-dd HH:MM:SS\",\"modifyDate\": \"yyyy-mm-dd HH:MM:SS\",\"companyName\": \"string\",\"startYear\": \"string\",\"endYear\": \"string\",\"title\": \"string\",\"commitee\": \"string\"}],\"jobHistory\": [{\"id\": 0,\"createdDate\": \"yyyy-mm-dd HH:MM:SS\",\"modifyDate\": \"yyyy-mm-dd HH:MM:SS\",\"companyName\": \"string\",\"startYear\": \"string\",\"endYear\": \"string\",\"title\": \"string\"}],\"educationDetails\": [{\"id\": \"string\",\"schoolName\": \"string\",\"degreeName\": \"string\",\"major\": \"string\",\"degreeYear\": \"string\",\"position\": \"string\",\"verify\": \"string\"}]}")))})
  public Contact getCandidateDetails(@PathVariable("contactId") String contactId) {
    log.info("Get Contact Details API call, Request Param contactId: " + contactId);
    ContactDTO contactDTO = service.getContactDetails(contactId);
    if (contactDTO == null) {
      throw new APIException("Invalid Contact Id");
    }

    Contact contact = service.findByGalaxyId(contactId);

    if (contact == null)
      contact = service.saveOrUpdateContact(contactDTO);

    contact = service.setContactDetails(contactDTO, contact);

    log.info("Successfully send Contact Details");
    log.debug("Get Contact Details API Response : " + contact);
    return contact;
  }

  @Operation(summary = "Get contact GDPR consent")
  @GetMapping("/contact/gdpr-consent/{contactId}")
  public GdprConsent getGdprConsent(@PathVariable("contactId") String contactId) {
    log.info("Get getGdpr Consent API call, Request Param contactId: " + contactId);
    return service.getGdprConsent(contactId);
  }

  @PutMapping("/contact/gdpr-consent/update/{contactId}/{candidateId}")
  public ResponseEntity<Object> updateGdprConsent(@PathVariable("contactId") String contactId,
      @RequestBody String gdprConsentData, HttpServletRequest request,
      @PathVariable("candidateId") String candidateId) {
    log.info("Update Gdpr Consent Details API call, Request Param contactId: " + contactId
        + " Contact Data: " + gdprConsentData);
    return service.updateGdprConsent(contactId, candidateId, gdprConsentData, request);
  }

  @PutMapping("/contact/update/{contactId}/{candidateId}")
  public String updateContactDetails(@PathVariable("contactId") String contactId,
      @RequestBody String contactData, HttpServletRequest request,
      @PathVariable("candidateId") String candidateId) throws UnsupportedEncodingException {
    log.info("Update Contact Details API call, Request Param contactId: " + contactId
        + " Contact Data: " + contactData);
    Contact existContactObj = getCandidateDetails(contactId);
    return service.updateContactDetails(contactId, contactData, request, candidateId,
        existContactObj);
  }

  //Currently not call from frontend because every thing is update throw "/contact/update/{contactId}/{candidateId}"
  @Operation(summary = "Update Contact Education Details")
  @PutMapping("/contact/education/{contactId}")
  public String updateContactEducationDetails(@PathVariable("contactId") String contactId,
      @RequestBody String contactData) {
    log.info("Update Contact Details API call, Request Param contactId: " + contactId
        + " Contact Education Data: " + contactData);
    return service.updateContactEducationDetails(contactId, contactData);
  }

  //currently not call from front end because this delete functionality coverd in "/contact/update/{contactId}/{candidateId}"
  @Operation(summary = "delete Contact Job History Details")
  @DeleteMapping("/contact/jobHistory/{id}")
  public ResponseEntity<Object> deleteContactJobHistory(@PathVariable("id") String id) {
    log.info("delete Contact Job History Details API call, Request Param contactId: " + id);
    return service.deleteJobHistoryById(id);
  }

//currently not call from front end because this delete functionality coverd in "/contact/update/{contactId}/{candidateId}"
  @Operation(summary = "delete Contact Board History Details")
  @DeleteMapping("/contact/boardHistory/{id}")
  public ResponseEntity<Object> deleteContactBoardHistory(@PathVariable("id") String id) {
    log.info("delete Contact Board History Details API call, Request Param contactId: " + id);
    return service.deleteBoardHistoryById(id);
  }

  @Operation(summary = "Get contact profile image")
  @GetMapping("/contact/{contactId}/profile-image")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json",
          schema = @Schema(type = "byte", example = "string")))})
  public byte[] getContactImage(@PathVariable("contactId") String contactId) {
    log.info("Get contact profile image API call, Request Param contactId: " + contactId);
    return service.getContactImage(contactId);
  }


  @Operation(summary = "Get List of contact references")
  @GetMapping("/contact/{contactId}/get-references")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(
          type = "List<ContactReferencesDTO>",
          example = "[{\"id\": 0,\"createdDate\": \"yyyy-mm-dd HH:MM:SS\",\"modifyDate\": \"yyyy-mm-dd HH:MM:SS\",\"contactId\": \"string\",\"refContactName\": \"string\",\"searchName\": \"string\",\"searchId\": \"string\", \"phone\": \"string\",\"email\": \"string\",\"workEmail\": string,\"relationship\": \"string\",\"refType\": \"string\",\"companyName\": \"string\",\"title\": \"string\"}]")))})
  public List<Reference> getListOfReferences(@PathVariable("contactId") String contactId) {
    log.info("Get List of contact references API call, Request Param contactId: " + contactId);
    return service.getListOfReferences(contactId);
  }

  @Operation(summary = "upload resume for contact",
      description = "Document Type = Resume / Attechment / offerLetter")
  @PostMapping("/contact/{contactId}/{candidateId}/upload-resumes")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = Constant.FILE_UPLOADED_SUCCESSFULLY)})
  public String uploadResume(@PathVariable("contactId") String contactId,
      @PathVariable("candidateId") String candidateId, @RequestParam("file") MultipartFile file,
      @RequestParam("documentType") String type, HttpServletRequest request) {
    log.info("upload document for contact API call, Request Param contactId: " + contactId
        + " File: " + file.getName() + " documentType : " + type);
    return service.uploadCandidateResume(file, contactId, type, candidateId, request);
  }

  @Operation(summary = "Upload Profile Image For Contact")
  @PostMapping("/contact/{contactId}/upload-profile-image")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = Constant.IMAGE_UPLOADED_SUCCESSFULLY)})
  public String uploadContactProfileImage(@PathVariable("contactId") String contactId,
      @RequestParam("profile") MultipartFile profile) {
    log.info("Upload Profile Image For Contact API call, Request Param contactId: " + contactId
        + " MultipartFile: " + profile.getName());
    return service.uploadContactImage(profile, contactId);
  }

  @Operation(summary = "Get contact Resumes")
  @GetMapping(value = {"/contact/{contactId}/resumes"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "Resume",
          example = "{ \"id\": \"string\",\"fileName\": \"string\",\"createdAt\": \"string\" }")))})
  public DocumentDTO getResumeDetails(@PathVariable("contactId") String contactId) {
    log.info("Get contact Resumes API call, Request Param contactId: " + contactId);
    return service.getContactResumes(contactId);
  }

  @Operation(summary = "Get contact Offer Letter")
  @GetMapping(value = {"/contact/{contactId}/offerLetter"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "Offer Letter",
          example = "{ \"id\": \"string\",\"fileName\": \"string\",\"createdAt\": \"string\" }")))})
  public DocumentDTO getOfferLetterDetails(@PathVariable("contactId") String contactId) {
    log.info("Get contact Offer Letter API call, Request Param contactId: " + contactId);
    return service.getContactOfferLetter(contactId);
  }

  /*
   * This API only for resume download
   */
  @Operation(summary = "Download Resume Documents")
  @GetMapping(value = {"contact/resumes/{attachmentId}/download"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  public void downloadDocument(@PathVariable("attachmentId") String attachmentId,
      @RequestParam String documentName, HttpServletResponse response) {
    log.info("Download Documents API call, Request Param attachmentId: " + attachmentId
        + " documentName: " + documentName);
    service.downloadDocument(documentName, attachmentId, response);
  }

  /*
   * This is API for any type of document download
   */
  @Operation(summary = "Download Any Documents")
  @GetMapping(value = {"contact/document/{attachmentId}/download"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  public void downloadAnyDocument(@PathVariable("attachmentId") String attachmentId,
      @RequestParam String documentName, @RequestParam String documentType,
      HttpServletResponse response) {
    log.info("Download Any Documents API call, Request Param attachmentId: " + attachmentId
        + " documentName: " + documentName + " document Type: " + documentType);
    service.downloadAnyDocument(documentName, documentType, attachmentId, response);
  }


  @Operation(summary = "Get contact searches")
  @GetMapping(value = {"/contact/{contactId}/searches"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "List<SearchDTO>",
          example = "[{\"id\": \"string\",\"jobTitle\": \"string\",\"jobNumber\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"}}]")))})
  public MappingJacksonValue getListOfContactSearches(@PathVariable("contactId") String contactId) {
    log.info("Get contact searches API call, Request Param contactId: " + contactId);
    List<SearchDTO> searchDTO = service.getListOfContactSearches(contactId);

    SimpleBeanPropertyFilter searchFilter = SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID,
        Constant.JOB_TITLE, Constant.COMPANY, Constant.JOB_NUMBER);

    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, "name");

    FilterProvider filters =
        new SimpleFilterProvider().addFilter(Constant.SEARCH_FILTER, searchFilter)
            .addFilter(Constant.COMPANY_FILTER, companyFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(searchDTO);
    log.info("Successfully send List of contact searches " + searchDTO.size());
    log.debug("Get contact searches API Response : " + mapping.getValue());
    mapping.setFilters(filters);

    return mapping;
  }

  @Operation(summary = "Get all matching contacts")
  @GetMapping(value = {"/contactName"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "List<ContactDTO>",
          example = "[{\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\",\"workEmail\": \"string\",\"email\": \"string\",\"mobilePhone\": \"string\",\"workPhone\": \"string\",\"currentJobTitle\": \"string\",\"company\": {\"name\": \"string\"}}]")))})
  public MappingJacksonValue getListOfContactByName(
      @RequestParam(name = "name") String contactName) {
    log.info("Get all matching contacts API call, Request Param contactName: " + contactName);
    List<ContactDTO> contactDTO = service.getListOfContactByName(contactName);

    SimpleBeanPropertyFilter contactFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, Constant.FIRST_NAME,
            Constant.LAST_NAME, Constant.CURRENT_JOB_TITLE, Constant.COMPANY, Constant.WORK_EMAIL,
            Constant.EMAIL, Constant.MOBILE_PHONE, Constant.WORK_PHONE);

    SimpleBeanPropertyFilter companyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("name");

    FilterProvider filters =
        new SimpleFilterProvider().addFilter(Constant.CONTACT_FILTER, contactFilter)
            .addFilter(Constant.COMPANY_FILTER, companyFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(contactDTO);
    mapping.setFilters(filters);
    log.info("Successfully send all contacts matching " + contactDTO.size());
    log.debug("Get all matching contacts API Response : " + mapping.getValue());
    return mapping;
  }

  @Operation(summary = "Add Contact Reference")
  @PostMapping("/contact/{contactId}/references/{candidateId}")
  public ResponseEntity<Object> addContactReference(@PathVariable("contactId") String contactId,
      @RequestBody String referenceData, HttpServletRequest request,
      @PathVariable("candidateId") String candidateId) throws UnsupportedEncodingException {
    log.info("Add Contact Reference API call, Request Param contactId: " + contactId
        + " referenceData: " + referenceData);
    Reference reference =
        service.saveAndUpdateContactReference(null, referenceData, contactId, request, candidateId);
    if (reference != null) {
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, HttpStatus.OK);
      body.put(Constant.MESSAGE, "Reference data added successfully");
      return new ResponseEntity<>(body, HttpStatus.OK);
    }
    throw new APIException("Error in save reference data");
  }

  @Operation(summary = "Update Contact Reference")
  @PutMapping("/contact/reference/{referenceId}/{candidateId}")
  public ResponseEntity<Object> updateContactReference(
      @PathVariable("referenceId") String referenceId, @RequestBody String referenceData,
      HttpServletRequest request, @PathVariable("candidateId") String candidateId)
      throws UnsupportedEncodingException {
    log.info("Update Contact Reference API call, Request Param referenceId: " + referenceId
        + " referenceData: " + referenceData);
    Reference reference = service.saveAndUpdateContactReference(referenceId, referenceData, null,
        request, candidateId);
    if (reference != null) {
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, HttpStatus.OK);
      body.put(Constant.MESSAGE, "Reference Data updated successfully");
      return new ResponseEntity<>(body, HttpStatus.OK);
    }
    throw new APIException("Error in save reference data");
  }

  @Operation(summary = "Add New Contact")
  @PostMapping("/contact")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json",
          schema = @Schema(type = "ContactDTO", example = "{\"id\": \"string\"}")))})
  public String addNewContact(@RequestBody String contactData) {
    return service.addNewContact(contactData);
  }

}
