package com.aspire.kgp.controller;


import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
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
import com.aspire.kgp.model.Contact;
import com.aspire.kgp.model.Reference;
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

  @Operation(summary = "Get Contact Details")
  @GetMapping("/contact/{contactId}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "ContactDTO",
          example = "{\"firstName\": \"string\",\"lastName\": \"string\",\"city\": \"string\",\"state\": \"string\",\"workEmail\": \"string\",\"email\": \"string\",\"linkedinUrl\": \"string\",\"mobilePhone\": \"string\",\"currentJobTitle\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"},\"homePhone\": \"string\",\"baseSalary\": \"string\",\"targetBonusValue\": \"string\",\"equity\": \"string\",\"compensationExpectation\": \"string\",\"compensationNotes\": \"string\",\"jobHistory\": [{\"id\": \"string\",\"title\": \"string\",\"start_year\": \"string\",\"end_year\": \"string\",\"position\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"}}],\"educationDetails\": [{\"id\": \"string\",\"school_name\": \"string\",\"degree_name\": \"string\",\"major\": \"string\",\"degree_year\": \"string\",\"position\": \"string\"}], \"boardDetails\": [{\"id\": \"string\",\"title\": \"string\",\"startYear\": \"string\",\"endYear\": \"string\",\"position\": \"0\",\"company\": {\"id\": \"string\",\"name\": \"string\"},\"committee\": \"string\"}]}")))})
  public MappingJacksonValue getCandidateDetails(@PathVariable("contactId") String contactId) {
    log.info("Get Contact Details API call, Request Param contactId: " + contactId);
    ContactDTO contactDTO = service.getContactDetails(contactId);
    Contact contact = service.findByGalaxyId(contactId);
    //if (contact == null)
      contact = service.saveOrUpdateContact(contactDTO);
    contact.setEducationDetails(contactDTO.getEducationDetails());

    SimpleBeanPropertyFilter contactFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        Constant.FIRST_NAME, Constant.LAST_NAME, Constant.CITY, Constant.STATE,
        Constant.CURRENT_JOB_TITLE, Constant.COMPANY, Constant.MOBILE_PHONE, "homePhone",
        Constant.WORK_EMAIL, Constant.EMAIL, Constant.LINKEDIN_URL, "baseSalary",
        "targetBonusValue", "equity", "compensationExpectation", "compensationNotes", "jobHistory",
        "educationDetails", "boardDetails");

    FilterProvider filters =
        new SimpleFilterProvider().addFilter(Constant.CONTACT_FILTER, contactFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(contact);
    mapping.setFilters(filters);
    log.info("Successfully send Contact Details");
    log.debug("Get Contact Details API Response : " + mapping.getValue());
    return mapping;
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

  @Operation(summary = "Update Contact Details")
  @PutMapping("/contact/{contactId}")
  public String updateContactDetails(@PathVariable("contactId") String contactId,
      @RequestBody String contactData) throws UnsupportedEncodingException {
    log.info("Update Contact Details API call, Request Param contactId: " + contactId
        + " Contact Data: " + contactData);
    return service.updateContactDetails(contactId, contactData);
  }

  @Operation(summary = "Get List of contact references")
  @GetMapping("/contact/{contactId}/references")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(
          type = "List<ContactReferencesDTO>",
          example = "[{\"id\": \"string\",\"searchId\": \"string\",\"relationship\": \"string\",\"contact\": {\"firstName\": \"string\",\"lastName\": \"string\",\"workEmail\": \"string\",\"email\": \"string\",\"mobilePhone\": \"string\",\"currentJobTitle\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"}}}]")))})
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

  @Operation(summary = "Download Documents")
  @GetMapping(value = {"contact/resumes/{attachmentId}/download"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  public void downloadDocument(@PathVariable("attachmentId") String attachmentId,
      @RequestParam String documentName, HttpServletResponse response) {
    log.info("Download Documents API call, Request Param attachmentId: " + attachmentId
        + " documentName: " + documentName);
    service.downloadDocument(documentName, attachmentId, response);
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

  @Operation(summary = "Add Contact Refere''nce")
  @PostMapping("/contact/{contactId}/references")
  public String addContactReference(@PathVariable("contactId") String contactId,
      @RequestBody String referenceData) {
    log.info("Add Contact Reference API call, Request Param contactId: " + contactId
        + " referenceData: " + referenceData);
    return service.addContactReference(contactId, referenceData);
  }

  @Operation(summary = "Update Contact Reference")
  @PutMapping("/contact/reference/{referenceId}")
  public String updateContactReference(@PathVariable("referenceId") String referenceId,
      @RequestBody String referenceData) throws UnsupportedEncodingException {
    log.info("Update Contact Reference API call, Request Param referenceId: " + referenceId
        + " referenceData: " + referenceData);
    return service.updateContactReference(referenceId, referenceData);
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
