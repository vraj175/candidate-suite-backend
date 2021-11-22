package com.aspire.kgp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CompanyDTO;
import com.aspire.kgp.dto.DocumentDTO;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.CompanyService;
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
@Tag(name = "Company", description = "Rest API For Company")
public class CompanyController {
  static Log log = LogFactory.getLog(CompanyController.class.getName());

  @Autowired
  CompanyService service;

  @Operation(summary = "Get Client list")
  @GetMapping("/companies/{stage}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "List<CompanyDTO>",
          example = "[{\"id\": \"string\",\"name\": \"string\"}]")))})
  public MappingJacksonValue getCompanyList(HttpServletRequest request,
      @PathVariable("stage") String stage) {
    User user = (User) request.getAttribute("user");
    log.info("Get Client list API call, Request Param stage: " + stage + "User is galaxy Id "
        + user.getGalaxyId());
    List<CompanyDTO> companyList;
    if (Constant.PARTNER.equalsIgnoreCase(user.getRole().getName())) {
      companyList = service.getCompanyList(stage);
    } else {
      throw new NotFoundException("Partner Not Found");
    }

    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "name");

    FilterProvider filters =
        new SimpleFilterProvider().addFilter(Constant.COMPANY_FILTER, companyFilter);
    MappingJacksonValue mapping = new MappingJacksonValue(companyList);
    mapping.setFilters(filters);
    log.info("Successfully send Client list " + companyList.size());
    log.debug("Get Client list API Response : " + mapping.getValue());
    return mapping;
  }

  @Operation(summary = "Get candidate Details")
  @GetMapping("/companyInfo/{candidateId}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "CandidateDTO",
          example = "{\"id\": \"string\",\"contact\": {\"athenaCompletionDate\": \"string\",\"athenaInvitationSentOn\": \"string\"},\"kgpInterviewDate1\": \"string\",\"kgpInterviewDate2\": \"string\",\"kgpInterviewDate3\": \"string\",\"interviews\": [{\"id\": \"string\",\"method\": \"string\",\"comments\": \"string\",\"position\": 0,\"interviewDate\": \"string\",\"client\": {\"id\": \"string\",\"name\": \"string\"}}],\"degreeVerification\": true,\"offerPresented\": true,\"athenaCompleted\": true,\"contactId\": \"string\",\"stage\": \"string\",\"kgpInterviewMethod1\": \"string\",\"kgpInterviewMethod2\": \"string\",\"kgpInterviewMethod3\": \"string\",\"kgpInterviewClient1\": {\"id\": \"string\",\"name\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\"},\"kgpInterviewClient2\": {\"id\": \"string\",\"name\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\"},\"kgpInterviewClient3\": {\"id\": \"string\",\"name\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\"},\"screenedDate\": \"string\",\"isOfferLetterUploaded\": true}")))})
  public MappingJacksonValue getCompanyInfoDetails(
      @PathVariable("candidateId") String candidateId) {
    log.info("Get candidate Details API call, Request Param CandidateId : " + candidateId);
    CandidateDTO candidateDTO = service.getCompanyInfoDetails(candidateId);
    SimpleBeanPropertyFilter candidateFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        Constant.ID, Constant.STAGE, Constant.CONTACTID, Constant.KGP_INTERVIEW_DATE_1,
        Constant.KGP_INTERVIEW_DATE_2, Constant.KGP_INTERVIEW_DATE_3, Constant.INTERVIEWS,
        Constant.DEGREE_VERIFICATION, Constant.OFFER_PRESENTED, Constant.ATHENA_COMPLETED,
        Constant.CONTACT, Constant.KGP_INTERVIEW_METHOD_1, Constant.KGP_INTERVIEW_METHOD_2,
        Constant.KGP_INTERVIEW_METHOD_3, Constant.KGP_INTERVIEW_CLIENT_1,
        Constant.KGP_INTERVIEW_CLIENT_2, Constant.KGP_INTERVIEW_CLIENT_3, Constant.SCREENED_DATE,
        "isOfferLetterUploaded");
    SimpleBeanPropertyFilter interviewFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, Constant.METHOD, Constant.COMMENTS,
            Constant.POSITION, Constant.INTERVIEW_DATE, Constant.CLIENT);
    SimpleBeanPropertyFilter userFilter = SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID,
        Constant.NAME, Constant.FIRST_NAME, Constant.LAST_NAME);
    SimpleBeanPropertyFilter contactFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        Constant.ATHENA_INVITATION_DATE, Constant.ATHENA_COMPLETION_DATE, "educationDetails");
    FilterProvider filters =
        new SimpleFilterProvider().addFilter(Constant.CANDIDATE_FILTER, candidateFilter)
            .addFilter(Constant.INTERVIEW_FILTER, interviewFilter)
            .addFilter(Constant.USER_FILTER, userFilter)
            .addFilter(Constant.CONTACT_FILTER, contactFilter);
    MappingJacksonValue mapping = new MappingJacksonValue(candidateDTO);
    mapping.setFilters(filters);
    log.info("Successfully send candidate Details");
    log.debug("Get candidate Details API Response : " + mapping.getValue());
    return mapping;
  }

  @Operation(summary = "Get all matching companies")
  @GetMapping("/companyName")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "List<CompanyDTO>",
          example = "[{\"id\": \"string\",\"name\": \"string\"}]")))})
  public MappingJacksonValue getListOfCompany(@RequestParam(name = "name") String companyName) {
    log.info("Get all matching companies API call, Request Param companyName : " + companyName);
    List<CompanyDTO> companyDTO = service.getListOfCompany(companyName);
    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, "name");
    FilterProvider filters =
        new SimpleFilterProvider().addFilter(Constant.COMPANY_FILTER, companyFilter);
    MappingJacksonValue mapping = new MappingJacksonValue(companyDTO);
    mapping.setFilters(filters);
    log.info("Successfully send all matching companies");
    log.debug("Get all matching companies API Response : " + mapping.getValue());
    return mapping;
  }

  @Operation(summary = "Get all document attachment")
  @GetMapping("/company/{companyId}/attachments")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(
          type = "List<DocumentDTO>",
          example = "[{\"id\": \"string\",\"fileName\": \"string\",\"createdAt\": \"string\"}]")))})
  public List<DocumentDTO> getDocumentAttchment(@PathVariable("companyId") String companyId) {
    log.info("Get document attachment API call, Request Param companyId : " + companyId);
    List<DocumentDTO> listOfDocument = service.getDocumentAttchment(companyId);

    log.info("Successfully send document attachment list" + listOfDocument.size());
    return listOfDocument;
  }

  @Operation(summary = "upload Attachment for company")
  @PostMapping("/company/{companyId}/upload/attachment")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = Constant.FILE_UPLOADED_SUCCESSFULLY)})
  public String uploadCompanyAttachment(@PathVariable("companyId") String companyId,
      @RequestParam("file") MultipartFile file, HttpServletRequest request) {
    log.info("upload Attachment for company API call, Request Param companyId: " + companyId
        + " File: " + file.getName());
    return service.uploadCompanyAttachment(file, companyId, request);
  }

  @Operation(summary = "Add New Company")
  @PostMapping("/companies")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json",
          schema = @Schema(type = "CompanyDTO", example = "[{\"id\": \"string\"}]")))})
  public String addNewCompany(@RequestBody String companyData) {
    return service.addNewCompany(companyData);
  }
}
