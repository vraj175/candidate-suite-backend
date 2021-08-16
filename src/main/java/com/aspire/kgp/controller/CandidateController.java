package com.aspire.kgp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    SimpleBeanPropertyFilter userFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName", "lastName");

    SimpleBeanPropertyFilter searchFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "jobTitle", "jobNumber", "company");

    FilterProvider filters = new SimpleFilterProvider().addFilter("userFilter", userFilter)
        .addFilter("searchFilter", searchFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(candidateDTO);
    mapping.setFilters(filters);

    return mapping;
  }

}
