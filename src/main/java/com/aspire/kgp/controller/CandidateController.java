package com.aspire.kgp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.util.CandidateUtil;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Api(tags = {"Candidate"})
@SwaggerDefinition(tags = {@Tag(name = "Candidate", description = "REST API for Candidate")})
public class CandidateController {

  @Autowired
  CandidateUtil candidateUtil;

  @ApiOperation(value = "Get Candidate Details")
  @GetMapping("/candidates/{candidateId}")
  public MappingJacksonValue getCandidateDetails(@PathVariable("candidateId") String candidateId) {
    CandidateDTO candidateDTO = candidateUtil.getCandidateDetails(candidateId);

    SimpleBeanPropertyFilter candidateFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.CONTACT, Constant.SEARCH);

    SimpleBeanPropertyFilter userAndContactFilter = SimpleBeanPropertyFilter
        .filterOutAllExcept(Constant.ID, Constant.FIRST_NAME, Constant.LAST_NAME);

    SimpleBeanPropertyFilter searchFilter = SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID,
        Constant.JOB_TITLE, Constant.JOB_NUMBER, Constant.COMPANY, Constant.PARTNERS,
        Constant.RECRUITERS, Constant.RESEARCHERS, Constant.EAS);

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("candidateFilter", candidateFilter).addFilter("userFilter", userAndContactFilter)
        .addFilter("searchFilter", searchFilter).addFilter("contactFilter", userAndContactFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(candidateDTO);
    mapping.setFilters(filters);

    return mapping;
  }

  @ApiOperation(value = "Get Team Member Details")
  @GetMapping(value = {"/candidates/{candidateId}/team-members"})
  public MappingJacksonValue getBiosDetails(@PathVariable("candidateId") String candidateId) {
    CandidateDTO candidateDTO = candidateUtil.getCandidateDetails(candidateId);
    SimpleBeanPropertyFilter candidateFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.SEARCH);

    SimpleBeanPropertyFilter searchFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.CLIENT_TEAM, Constant.PARTNERS,
            Constant.RECRUITERS, Constant.RESEARCHERS, Constant.EAS);

    SimpleBeanPropertyFilter userFilter = SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID,
        Constant.FIRST_NAME, Constant.LAST_NAME, Constant.EMAIL, Constant.TITLE, Constant.COUNTRY,
        Constant.LINKEDIN_URL, Constant.BIO);

    SimpleBeanPropertyFilter clientTeamFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, Constant.CONTACT);

    SimpleBeanPropertyFilter contactFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID, Constant.FIRST_NAME,
            Constant.LAST_NAME, Constant.CURRENT_JOB_TITLE, Constant.MOBILE_PHONE,
            Constant.PUBLISHED_BIO, Constant.COMPANY,Constant.LINKEDIN_URL);

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("candidateFilter", candidateFilter).addFilter("searchFilter", searchFilter)
        .addFilter("userFilter", userFilter).addFilter("clientTeamFilter", clientTeamFilter)
        .addFilter("contactFilter", contactFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(candidateDTO);
    mapping.setFilters(filters);

    return mapping;
  }

}
