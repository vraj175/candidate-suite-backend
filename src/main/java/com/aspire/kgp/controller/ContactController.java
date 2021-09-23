package com.aspire.kgp.controller;


import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import com.aspire.kgp.dto.ContactReferencesDTO;
import com.aspire.kgp.dto.DocumentDTO;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.util.ContactUtil;
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

  @Autowired
  ContactUtil contactUtil;

  @Operation(summary = "Get Contact Details")
  @GetMapping("/contact/{contactId}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "ContactDTO",
          example = "{\"workEmail\": \"string\",\"email\": \"string\",\"linkedinUrl\": \"string\",\"mobilePhone\": \"string\",\"currentJobTitle\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"},\"homePhone\": \"string\",\"baseSalary\": \"string\",\"targetBonusValue\": \"string\",\"equity\": \"string\",\"compensationExpectation\": \"string\",\"compensationNotes\": \"string\",\"jobHistory\": [{\"id\": \"string\",\"title\": \"string\",\"start_year\": \"string\",\"end_year\": \"string\",\"position\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"}}],\"educationDetails\": [{\"id\": \"string\",\"school_name\": \"string\",\"degree_name\": \"string\",\"major\": \"string\",\"degree_year\": \"string\",\"position\": \"string\"}], \"boardDetails\": [{\"id\": \"string\",\"title\": \"string\",\"startYear\": \"string\",\"endYear\": \"string\",\"position\": \"0\",\"company\": {\"id\": \"string\",\"name\": \"string\"},\"committee\": \"string\"}]}")))})
  public MappingJacksonValue getCandidateDetails(@PathVariable("contactId") String contactId) {
    ContactDTO contactDTO = contactUtil.getContactDetails(contactId);
    SimpleBeanPropertyFilter contactFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        Constant.CURRENT_JOB_TITLE, Constant.COMPANY, Constant.MOBILE_PHONE, "homePhone",
        Constant.WORK_EMAIL, Constant.EMAIL, Constant.LINKEDIN_URL, "baseSalary",
        "targetBonusValue", "equity", "compensationExpectation", "compensationNotes", "jobHistory",
        "educationDetails", "boardDetails");
    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "name");
    FilterProvider filters =
        new SimpleFilterProvider().addFilter(Constant.CONTACT_FILTER, contactFilter)
            .addFilter(Constant.COMPANY_FILTER, companyFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(contactDTO);
    mapping.setFilters(filters);

    return mapping;
  }

  @Operation(summary = "Get contact profile image")
  @GetMapping("/contact/{contactId}/profile-image")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
  content = @Content(mediaType = "application/json", schema = @Schema(
      type = "byte",
      example = "string")))})
  public byte[] getContactImage(@PathVariable("contactId") String contactId) {
    return contactUtil.getContactImage(contactId);
  }

  @Operation(summary = "Update Contact Details")
  @PutMapping("/contact/{contactId}")
  public String updateContactDetails(@PathVariable("contactId") String contactId,
      @RequestBody String contactData) throws UnsupportedEncodingException {
    return contactUtil.updateContactDetails(contactId, contactData);
  }

  @Operation(summary = "Get List of contact references")
  @GetMapping("/contact/{contactId}/references")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(
          type = "List<ContactReferencesDTO>",
          example = "[{\"id\": \"string\",\"searchId\": \"string\",\"relationship\": \"string\",\"contact\": {\"firstName\": \"string\",\"lastName\": \"string\",\"workEmail\": \"string\",\"email\": \"string\",\"mobilePhone\": \"string\",\"currentJobTitle\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"}}}]")))})
  public MappingJacksonValue getListOfReferences(@PathVariable("contactId") String contactId) {
    List<ContactReferencesDTO> contactReferenceDTO = contactUtil.getListOfReferences(contactId);
    SimpleBeanPropertyFilter contactReferenceFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        "id", "searchId", "relationship", "contact", "source", "type", "refContactId", "search");
    SimpleBeanPropertyFilter contactFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        Constant.FIRST_NAME, Constant.LAST_NAME, Constant.CURRENT_JOB_TITLE, Constant.MOBILE_PHONE,
        Constant.COMPANY, Constant.EMAIL, Constant.WORK_EMAIL);
    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "name");
    SimpleBeanPropertyFilter searchFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "jobTitle");
    FilterProvider filters =
        new SimpleFilterProvider().addFilter("contactReferenceFilter", contactReferenceFilter)
            .addFilter(Constant.CONTACT_FILTER, contactFilter)
            .addFilter(Constant.COMPANY_FILTER, companyFilter)
            .addFilter(Constant.SEARCH_FILTER, searchFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(contactReferenceDTO);
    mapping.setFilters(filters);

    return mapping;
  }

  @Operation(summary = "upload resume for contact")
  @PostMapping("/contact/{contactId}/resumes")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = Constant.FILE_UPLOADED_SUCCESSFULLY)})
  public String uploadResume(@PathVariable("contactId") String contactId,
      @RequestParam("file") MultipartFile file) {
    return contactUtil.uploadCandidateResume(file, contactId);
  }

  @Operation(summary = "Get contact Resumes")
  @GetMapping(value = {"/contact/{contactId}/resumes"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "Resume",
          example = "{ \"id\": \"string\",\"fileName\": \"string\",\"createdAt\": \"string\" }")))})
  public DocumentDTO getResumeDetails(@PathVariable("contactId") String contactId) {
    return contactUtil.getContactResumes(contactId);
  }

  @Operation(summary = "Download Documents")
  @GetMapping(value = {"contact/resumes/{attachmentId}/download"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  public void downloadDocument(@PathVariable("attachmentId") String attachmentId,
      @RequestParam String documentName, HttpServletResponse response) {
    contactUtil.downloadDocument(documentName, attachmentId, response);
  }

  @Operation(summary = "Get contact searches")
  @GetMapping(value = {"/contact/{contactId}/searches"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "List<SearchDTO>",
          example = "[{\"id\": \"string\",\"jobTitle\": \"string\",\"jobNumber\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"}}]")))})
  public MappingJacksonValue getListOfContactSearches(@PathVariable("contactId") String contactId) {
    List<SearchDTO> searchDTO = contactUtil.getListOfContactSearches(contactId);

    SimpleBeanPropertyFilter searchFilter = SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID,
        Constant.JOB_TITLE, Constant.COMPANY, Constant.JOB_NUMBER);

    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, "name");

    FilterProvider filters =
        new SimpleFilterProvider().addFilter(Constant.SEARCH_FILTER, searchFilter)
            .addFilter(Constant.COMPANY_FILTER, companyFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(searchDTO);
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
    List<ContactDTO> contactDTO = contactUtil.getListOfContactByName(contactName);

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

    return mapping;
  }

  @Operation(summary = "Add Contact Reference")
  @PostMapping("/contact/{contactId}/references")
  public String addContactReference(@PathVariable("contactId") String contactId,
      @RequestBody String referenceData) {
    return contactUtil.addContactReference(contactId, referenceData);
  }

  @Operation(summary = "Update Contact Reference")
  @PutMapping("/contact/reference/{referenceId}")
  public String updateContactReference(@PathVariable("referenceId") String referenceId,
      @RequestBody String referenceData) throws UnsupportedEncodingException {
    return contactUtil.updateContactReference(referenceId, referenceData);
  }
}
