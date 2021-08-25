package com.aspire.kgp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.Language;
import com.aspire.kgp.service.LanguageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Api(tags = {"Language"})
@SwaggerDefinition(tags = {@Tag(name = "Language", description = "REST API for Language")})
public class LanguageController {

  @Autowired
  LanguageService service;

  @ApiOperation(value = "Initialize languages")
  @PostMapping(value = Constant.PUBLIC_API_URL + "/languages/initialize")
  public String initializeLanguages() {
    return service.initializeData();
  }

  @ApiOperation(value = "Get list of Languages")
  @GetMapping(value = "/languages")
  public List<Language> getLanguages() {
    return service.findAll();
  }
}
