package com.aspire.kgp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.model.User;
import com.aspire.kgp.util.SearchUtil;

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
}
