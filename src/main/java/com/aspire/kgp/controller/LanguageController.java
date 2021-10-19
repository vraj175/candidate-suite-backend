package com.aspire.kgp.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.Language;
import com.aspire.kgp.service.LanguageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Tag(name = "Language", description = "REST API for Language")
public class LanguageController {
  static Log log = LogFactory.getLog(LanguageController.class.getName());

  @Autowired
  LanguageService service;

  @Operation(summary = "Initialize languages")
  @PostMapping(value = Constant.PUBLIC_API_URL + "/languages/initialize")
  public String initializeLanguages() {
    log.info("Initialize languages API call");
    String languages = service.initializeData();
    log.info("Successfully send initialize language response");
    log.debug("Initialize languages API Response: " + languages);
    return languages;
  }

  @Operation(summary = "Get list of Languages")
  @GetMapping(value = "/languages")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "List<Language>",
          example = "[{\"id\": 0,\"createdDate\": \"string\",\"modifyDate\": \"string\",\"name\": \"string\"}]")))})
  public List<Language> getLanguages() {
    log.info("Get list of Languages API call");
    List<Language> languageList = service.findAll();
    log.info("Successfully send list of Languages " + languageList.size());
    log.debug("Get list of Languages API Response: " + languageList);
    return languageList;
  }
}
