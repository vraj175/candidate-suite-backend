package com.aspire.kgp.controller;

import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CandidateFeedbackDTO;
import com.aspire.kgp.dto.CandidateFeedbackReplyDTO;
import com.aspire.kgp.dto.CandidateFeedbackRequestDTO;
import com.aspire.kgp.dto.CandidateFeedbackRequestDTO.CandidateFeedbackReplyReq;
import com.aspire.kgp.dto.CandidateFeedbackRequestDTO.CandidateFeedbackReq;
import com.aspire.kgp.dto.CandidateFeedbackRequestDTO.CandidateFeedbackStatusUpdateReq;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.CandidateService;
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
@Tag(name = "Candidate", description = "REST API for Candidate")
public class CandidateController {
  static Log log = LogFactory.getLog(CandidateController.class.getName());

  @Autowired
  CandidateService service;

  @Operation(summary = "Get Candidate Details")
  @GetMapping("/candidates/{candidateId}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "CandidateDTO",
          example = "{\"contact\": {\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\"},\"search\": {\"id\": \"string\",\"jobTitle\": \"string\",\"jobNumber\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"},\"partners\": [{\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\"}],\"recruiters\": [{\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\"}],\"researchers\": [{\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\"}],\"eas\": [{\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\"}]}}")))})
  public MappingJacksonValue getCandidateDetails(@PathVariable("candidateId") String candidateId) {
    log.info("Get Candidate Details API call, Request Param CandidateId : " + candidateId);
    CandidateDTO candidateDTO = service.getCandidateDetails(candidateId);

    SimpleBeanPropertyFilter candidateFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.CONTACT, Constant.SEARCH);

    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, Constant.NAME);

    SimpleBeanPropertyFilter userAndContactFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        Constant.ID, Constant.FIRST_NAME, Constant.LAST_NAME, Constant.TITLE, Constant.LOCATION,
        Constant.EXRCUTION_CREDIT, Constant.CITY, Constant.STATE);

    SimpleBeanPropertyFilter searchFilter = SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID,
        Constant.JOB_TITLE, Constant.JOB_NUMBER, Constant.COMPANY, Constant.PARTNERS,
        Constant.RECRUITERS, Constant.RESEARCHERS, Constant.EAS, Constant.IS_APPROVED_BY_PARTNER);

    FilterProvider filters =
        new SimpleFilterProvider().addFilter(Constant.CANDIDATE_FILTER, candidateFilter)
            .addFilter("userFilter", userAndContactFilter)
            .addFilter(Constant.SEARCH_FILTER, searchFilter)
            .addFilter(Constant.CONTACT_FILTER, userAndContactFilter)
            .addFilter(Constant.COMPANY_FILTER, companyFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(candidateDTO);
    mapping.setFilters(filters);
    log.info("Successfully send Candidate Details");
    log.debug("Get Candidate Details API Response : " + mapping.getValue());
    return mapping;
  }

  @Operation(summary = "Get Team Member Details")
  @GetMapping(value = {"/candidates/{candidateId}/team-members"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "CandidateDTO",
          example = "{\"search\": {\"partners\": [{\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\",\"email\": \"string\",\"title\": \"string\",\"country\": \"string\",\"linkedinUrl\": \"string\",\"bio\": \"string\",\"mobilePhone\": \"string\",\"workPhone\": \"string\"}],\"recruiters\": [{\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\",\"email\": \"string\",\"title\": \"string\",\"country\": \"string\",\"linkedinUrl\": \"string\",\"bio\": \"string\",\"mobilePhone\": \"string\",\"workPhone\": \"string\"}],\"researchers\": [{\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\",\"email\": \"string\",\"title\": \"string\",\"country\": \"string\",\"linkedinUrl\": \"string\",\"bio\": \"string\",\"mobilePhone\": \"string\",\"workPhone\": \"string\"}],\"eas\": [{\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\",\"email\": \"string\",\"title\": \"string\",\"country\": \"string\",\"linkedinUrl\": \"string\",\"bio\": \"string\",\"mobilePhone\": \"string\",\"workPhone\": \"string\"}],\"clienTeam\": [{\"id\": \"string\",\"contact\": {\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\",\"country\": \"string\",\"linkedinUrl\": \"string\",\"mobilePhone\": \"string\",\"currentJobTitle\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"},\"publishedBio\": \"string\"}}]}}")))})
  public MappingJacksonValue getBiosDetails(@PathVariable("candidateId") String candidateId) {
    log.info("Get Team Member Details API call, Request Param CandidateId : " + candidateId);
    CandidateDTO candidateDTO = service.getCandidateDetails(candidateId);
    SimpleBeanPropertyFilter candidateFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.SEARCH);

    SimpleBeanPropertyFilter searchFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.CLIENT_TEAM, Constant.PARTNERS,
            Constant.RECRUITERS, Constant.RESEARCHERS, Constant.EAS);

    SimpleBeanPropertyFilter userFilter = SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID,
        Constant.FIRST_NAME, Constant.LAST_NAME, Constant.EMAIL, Constant.TITLE, Constant.COUNTRY,
        Constant.LINKEDIN_URL, Constant.BIO, Constant.MOBILE_PHONE, Constant.WORK_PHONE,
        Constant.LOCATION, Constant.EXRCUTION_CREDIT, Constant.CITY, Constant.STATE);

    SimpleBeanPropertyFilter clientTeamFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, Constant.CONTACT);

    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, Constant.NAME);

    SimpleBeanPropertyFilter contactFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        Constant.ID, Constant.FIRST_NAME, Constant.LAST_NAME, Constant.CURRENT_JOB_TITLE,
        Constant.MOBILE_PHONE, Constant.PUBLISHED_BIO, Constant.COMPANY, Constant.LINKEDIN_URL,
        Constant.COUNTRY, Constant.STATE, Constant.CITY);

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter(Constant.CANDIDATE_FILTER, candidateFilter)
        .addFilter(Constant.SEARCH_FILTER, searchFilter).addFilter(Constant.USER_FILTER, userFilter)
        .addFilter(Constant.CLIENT_TEAM_FILTER, clientTeamFilter)
        .addFilter(Constant.CONTACT_FILTER, contactFilter)
        .addFilter(Constant.COMPANY_FILTER, companyFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(candidateDTO);
    mapping.setFilters(filters);
    log.info("Successfully send Team Member Details");
    log.debug("Get Team Member Details API Response : " + mapping.getValue());
    return mapping;
  }

  @Operation(summary = "Download Athena Report",
      description = "Page Size = USLetter / A4 <br> Locale = en_US / es_ES / pt_BR ")
  @GetMapping(value = {"candidates/AthenaReport/{pageSize}/{locale}/{contactId}"})
  public ResponseEntity<byte[]> getAthenaReport(@PathVariable("pageSize") String pageSize,
      @PathVariable("locale") String locale, @PathVariable("contactId") String contactId) {
    log.info("Download Athena Report API call, Request Param pageSize: " + pageSize + " locale: "
        + locale + " contactId: " + contactId);
    return service.getAthenaReport(pageSize, locale, contactId);
  }

  @Operation(summary = "Get Candidate Feedback")
  @GetMapping(value = {"/candidates/{candidateId}/candidate-feedback"})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "CandidateDTO",
          example = "[{\"id\": \"string\",\"candidateId\": \"string\",\"createdBy\": \"string\",\"createdAt\": \"string\",\"updatedAt\": \"string\",\"type\": \"string\",\"createdName\": \"string\",\"comments\": \"string\",\"status\": false,\"replies\": [{\"id\": \"string\",\"candidateId\": \"string\",\"createdBy\": \"string\",\"createdAt\": \"string\",\"updatedAt\": \"string\",\"commentId\": \"string\",\"reply\": \"string\",\"type\": \"string\",\"createdName\": \"string\"}]}]\r\n"
              + "")))})
  public MappingJacksonValue getCandidateFeedback(@PathVariable("candidateId") String candidateId) {
    log.info("Get Candidate Feedback API call, Request Param CandidateId : " + candidateId);
    List<CandidateFeedbackDTO> candidateFeedbackList = service.getCandidateFeedback(candidateId);


    SimpleBeanPropertyFilter candidateFeedbackFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, Constant.CANDIDATE_ID,
            Constant.COMMENTS, Constant.STATUS, Constant.TYPE, Constant.CREATED_NAME,
            Constant.CREATED_BY, Constant.CREATED_AT, Constant.UPDATED_AT, Constant.REPLIES);
    SimpleBeanPropertyFilter candidateFeedbackReplyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, Constant.CANDIDATE_ID,
            Constant.COMMENT_ID, Constant.REPLY, Constant.TYPE, Constant.CREATED_NAME,
            Constant.CREATED_BY, Constant.CREATED_AT, Constant.UPDATED_AT);

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter(Constant.CANDIDATE_FEEDBACK_FILTER, candidateFeedbackFilter)
        .addFilter(Constant.CANDIDATE_FEEDBACK_REPLY_FILTER, candidateFeedbackReplyFilter);

    candidateFeedbackList.stream().forEach(candidateFeedbackDTO -> candidateFeedbackDTO.getReplies()
        .sort(Comparator.comparing(CandidateFeedbackReplyDTO::getCreatedAt).reversed()));
    candidateFeedbackList.sort(Comparator.comparing(CandidateFeedbackDTO::getCreatedAt).reversed());

    MappingJacksonValue mapping = new MappingJacksonValue(candidateFeedbackList);
    mapping.setFilters(filters);
    log.info("Successfully send Candidate Feedback list " + candidateFeedbackList.size());
    log.debug("Get Candidate Feedback API Response : " + candidateFeedbackList);
    return mapping;
  }

  @PostMapping(value = {"/candidates/candidate-feedback"})
  public String addCandidateFeedback(
      @Validated(CandidateFeedbackReq.class) @RequestBody CandidateFeedbackRequestDTO candidateFeedback,
      HttpServletRequest request) {
    User user = (User) request.getAttribute("user");
    log.info("Candidate Feedback SAVE API call, Request Param CandidateId : "
        + candidateFeedback.getCandidateId());
    String id = service.addCandidateFeedback(candidateFeedback.getCandidateId(),
        candidateFeedback.getComments(), user.getGalaxyId(), request, false, null,
        user.getRole().getName());
    log.info("Successfully ADD Candidate Feedback and it's id: " + id);
    return id;
  }

  @PostMapping(value = {"/candidates/candidate-feedback/reply"})
  public MappingJacksonValue addCandidateFeedbackReply(
      @Validated(CandidateFeedbackReplyReq.class) @RequestBody CandidateFeedbackRequestDTO candidateFeedReqback,
      HttpServletRequest request) {
    User user = (User) request.getAttribute("user");
    log.info("Candidate Feedback Reply API call for CandidateId : "
        + candidateFeedReqback.getCandidateId() + " Created By: " + user.getGalaxyId());
    CandidateFeedbackDTO candidateFeedback = service.addCandidateFeedbackReply(
        candidateFeedReqback.getCandidateId(), candidateFeedReqback.getCommentId(),
        candidateFeedReqback.getReply(), user.getGalaxyId(), request, user.getRole().getName());

    SimpleBeanPropertyFilter candidateFeedbackFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, Constant.CANDIDATE_ID,
            Constant.COMMENTS, Constant.STATUS, Constant.TYPE, Constant.CREATED_NAME,
            Constant.CREATED_BY, Constant.CREATED_AT, Constant.UPDATED_AT, Constant.REPLIES);
    SimpleBeanPropertyFilter candidateFeedbackReplyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, Constant.CANDIDATE_ID,
            Constant.COMMENT_ID, Constant.REPLY, Constant.TYPE, Constant.CREATED_NAME,
            Constant.CREATED_BY, Constant.CREATED_AT, Constant.UPDATED_AT);

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter(Constant.CANDIDATE_FEEDBACK_FILTER, candidateFeedbackFilter)
        .addFilter(Constant.CANDIDATE_FEEDBACK_REPLY_FILTER, candidateFeedbackReplyFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(candidateFeedback);
    mapping.setFilters(filters);
    return mapping;
  }

  @PutMapping(value = {"/candidates/candidate-feedback/status-update"})
  public String candidateFeedbackStatusUpdate(
      @Validated(CandidateFeedbackStatusUpdateReq.class) @RequestBody CandidateFeedbackRequestDTO candidateFeedback,
      HttpServletRequest request) {
    log.info("Candidate Feedback status update API call for comment id "
        + candidateFeedback.getCommentId());
    String id =
        service.updateCommentStatus(candidateFeedback.getCommentId(), candidateFeedback.isStatus());
    log.info("Successfully status update for comment Id " + candidateFeedback.getCommentId()
        + " status is " + candidateFeedback.isStatus());
    return id;
  }
}
