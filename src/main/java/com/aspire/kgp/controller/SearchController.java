package com.aspire.kgp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.model.User;
import com.aspire.kgp.util.SearchUtil;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Api(tags = {"Search"})
@SwaggerDefinition(tags = {@Tag(name = "Search", description = "Rest API For Search")})
public class SearchController {

  @Autowired
  SearchUtil searchUtil;

  @ApiOperation(value = "Get Search list for user")
  @GetMapping("/searches/stage/{stage}")
  public MappingJacksonValue getCompanyList(HttpServletRequest request,
      @PathVariable("stage") String stage) {
    User user = (User) request.getAttribute("user");
    return searchUtil.applySearchFilter(searchUtil.getSearchListForUser(user, stage));
  }

  @ApiOperation(value = "Get Search list for company")
  @GetMapping("/searches/companies/{companyId}/stage/{stage}")
  public MappingJacksonValue getCompanyList(@PathVariable("companyId") String companyId,
      @PathVariable("stage") String stage) {
    return searchUtil.applySearchFilter(searchUtil.getSearchList(companyId, stage));
  }

  @ApiOperation(value = "Get Candidate list")
  @GetMapping(value = {"/searches/{searchId}/candidates"})
  public MappingJacksonValue getCandidateList(@PathVariable("searchId") String searchId) {
    List<CandidateDTO> listCandidate = searchUtil.getCandidateList(searchId);

    SimpleBeanPropertyFilter userFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id",
        "firstName", "lastName", "workEmail", "currentJobTitle", "company");

    SimpleBeanPropertyFilter candidateFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "contact");

    FilterProvider filters = new SimpleFilterProvider().addFilter("userFilter", userFilter)
        .addFilter("candidateFilter", candidateFilter);

    MappingJacksonValue mapping = new MappingJacksonValue(listCandidate);
    mapping.setFilters(filters);
    return mapping;
  }
}
