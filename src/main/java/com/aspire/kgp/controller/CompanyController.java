package com.aspire.kgp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.CompanyDTO;
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
          example = "{\"id\": \"string\",\"kgpInterviewDate1\": \"string\",\"kgpInterviewDate2\": \"string\",\"kgpInterviewDate3\": \"string\",\"interviews\": [{\"id\": \"string\",\"method\": \"string\",\"comments\": \"string\",\"position\": 0,\"interviewDate\": \"string\",\"client\": {\"id\": \"string\",\"name\": \"string\"}}],\"degreeVerification\": true,\"offerPresented\": true,\"athenaCompleted\": true,\"conatctId\": \"string\"}")))})
  public MappingJacksonValue getCompanyInfoDetails(
      @PathVariable("candidateId") String candidateId) {
    log.info("Get candidate Details API call, Request Param CandidateId : " + candidateId);
    CandidateDTO candidateDTO = service.getCompanyInfoDetails(candidateId);
    SimpleBeanPropertyFilter candidateFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        Constant.ID, "contactId", "kgpInterviewDate1", "kgpInterviewDate2", "kgpInterviewDate3",
        "interviews", "degreeVerification", "offerPresented", "athenaCompleted");
    SimpleBeanPropertyFilter interviewFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        Constant.ID, "method", "comments", "position", "interviewDate", "client");
    SimpleBeanPropertyFilter userFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, "name");
    FilterProvider filters =
        new SimpleFilterProvider().addFilter("candidateFilter", candidateFilter)
            .addFilter("interviewFilter", interviewFilter).addFilter("userFilter", userFilter);
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
}
