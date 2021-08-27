package com.aspire.kgp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Api(tags = {"Company"})
@SwaggerDefinition(tags = {@Tag(name = "Company", description = "Rest API For Company")})
public class CompanyController {

  @Autowired
  CompanyUtil companyUtil;

  @ApiOperation(value = "Get Client list")
  @GetMapping("/companies/{stage}")
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

    FilterProvider filters = new SimpleFilterProvider().addFilter("companyFilter", companyFilter);
    MappingJacksonValue mapping = new MappingJacksonValue(companyList);
    mapping.setFilters(filters);

    return mapping;
  }

  @ApiOperation(value = "Get Company Info Details")
  @GetMapping("/company/{candidateId}")
  public MappingJacksonValue getCompanyDetails(@PathVariable("candidateId") String candidateId) {
    ContactDTO contactDTO = companyUtil.getCompanyDetails(candidateId);

    SimpleBeanPropertyFilter companyFilter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "name");
    SimpleBeanPropertyFilter contactFilter = SimpleBeanPropertyFilter
        .filterOutAllExcept(Constant.ID, Constant.CURRENT_JOB_TITLE, "company", Constant.COUNTRY);
    FilterProvider filters = new SimpleFilterProvider().addFilter("contactFilter", contactFilter)
        .addFilter("companyFilter", companyFilter);
    MappingJacksonValue mapping = new MappingJacksonValue(contactDTO);
    mapping.setFilters(filters);

    return mapping;
  }

  @ApiOperation(value = "Get candidate Details")
  @GetMapping("/companyInfo/{candidateId}")
  public MappingJacksonValue getCompanyInfoDetails(
      @PathVariable("candidateId") String candidateId) {
    CandidateDTO candidateDTO = companyUtil.getCompanyInfoDetails(candidateId);
    SimpleBeanPropertyFilter candidateFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        Constant.ID, "kgpInterviewDate1", "kgpInterviewDate2", "kgpInterviewDate3", "interviews");
    SimpleBeanPropertyFilter interviewFilter = SimpleBeanPropertyFilter.filterOutAllExcept(
        Constant.ID, "method", "comments", "position", "interviewDate", "client");
    SimpleBeanPropertyFilter userFilter = SimpleBeanPropertyFilter.filterOutAllExcept(Constant.ID,
        Constant.FIRST_NAME, Constant.LAST_NAME);
    FilterProvider filters =
        new SimpleFilterProvider().addFilter("candidateFilter", candidateFilter)
            .addFilter("interviewFilter", interviewFilter).addFilter("userFilter", userFilter);
    MappingJacksonValue mapping = new MappingJacksonValue(candidateDTO);
    mapping.setFilters(filters);

    return mapping;
  }
}
