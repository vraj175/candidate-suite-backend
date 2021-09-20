package com.aspire.kgp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.util.CompanyUtil;
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

  @Autowired
  CompanyUtil companyUtil;

  @Operation(summary = "Get Client list")
  @GetMapping("/companies/{stage}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "List<CompanyDTO>",
          example = "[{\"id\": \"string\",\"name\": \"string\"}]")))})
  public MappingJacksonValue getCompanyList(HttpServletRequest request,
      @PathVariable("stage") String stage) {
    User user = (User) request.getAttribute("user");
    List<CompanyDTO> companyList;
    if (Constant.PARTNER.equalsIgnoreCase(user.getRole().getName())) {
      companyList = companyUtil.getCompanyList(stage);
    } else {
      throw new NotFoundException("Partner Not Found");
    }

    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "name");

    FilterProvider filters =
        new SimpleFilterProvider().addFilter(Constant.COMPANY_FILTER, companyFilter);
    MappingJacksonValue mapping = new MappingJacksonValue(companyList);
    mapping.setFilters(filters);

    return mapping;
  }

  @Operation(summary = "Get Company Info Details")
  @GetMapping("/company/{candidateId}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "ContactDTO",
          example = "{\"id\": \"string\",\"country\": \"string\",\"currentJobTitle\": \"string\",\"company\": {\"id\": \"string\",\"name\": \"string\"}}")))})
  public MappingJacksonValue getCompanyDetails(@PathVariable("candidateId") String candidateId) {
    ContactDTO contactDTO = companyUtil.getCompanyDetails(candidateId);

    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "name");
    SimpleBeanPropertyFilter contactFilter = SimpleBeanPropertyFilter
        .filterOutAllExcept(Constant.ID, Constant.CURRENT_JOB_TITLE, "company", Constant.COUNTRY);
    FilterProvider filters = new SimpleFilterProvider().addFilter("contactFilter", contactFilter)
        .addFilter(Constant.COMPANY_FILTER, companyFilter);
    MappingJacksonValue mapping = new MappingJacksonValue(contactDTO);
    mapping.setFilters(filters);

    return mapping;
  }

  @Operation(summary = "Get candidate Details")
  @GetMapping("/companyInfo/{candidateId}")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "CandidateDTO",
          example = "{\"id\": \"string\",\"kgpInterviewDate1\": \"string\",\"kgpInterviewDate2\": \"string\",\"kgpInterviewDate3\": \"string\",\"interviews\": [{\"id\": \"string\",\"method\": \"string\",\"comments\": \"string\",\"position\": 0,\"interviewDate\": \"string\",\"client\": {\"id\": \"string\",\"name\": \"string\"}}],\"degreeVerification\": true,\"offerPresented\": true,\"athenaCompleted\": true,\"conatctId\": \"string\"}")))})
  public MappingJacksonValue getCompanyInfoDetails(
      @PathVariable("candidateId") String candidateId) {
    CandidateDTO candidateDTO = companyUtil.getCompanyInfoDetails(candidateId);
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

    return mapping;
  }

  @Operation(summary = "Get all matching companies")
  @GetMapping("/companyName")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "List<CompanyDTO>",
          example = "[{\"id\": \"string\",\"name\": \"string\"}]")))})
  public MappingJacksonValue getListOfCompany(@RequestParam(name = "name") String companyName) {
    List<CompanyDTO> companyDTO = companyUtil.getListOfCompany(companyName);
    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, "name");
    FilterProvider filters =
        new SimpleFilterProvider().addFilter(Constant.COMPANY_FILTER, companyFilter);
    MappingJacksonValue mapping = new MappingJacksonValue(companyDTO);
    mapping.setFilters(filters);

    return mapping;
  }
}
