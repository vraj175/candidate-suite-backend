package com.aspire.kgp.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.dto.PickListDTO;
import com.aspire.kgp.service.PickListService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Tag(name = "PickList", description = "Rest API For PickList")
public class PickListController {
  static Log log = LogFactory.getLog(PickListController.class.getName());

  @Autowired
  PickListService service;

  @Operation(summary = "Get Education Degree List")
  @GetMapping("/picklists/educations/degrees")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  public List<String> getEducationDegrees() {
    log.info("Get Education Degree List API call");
    List<String> degreeList = service.getEducationDegrees();
    log.info("Successfully send Education Degree List " + degreeList.size());
    log.debug("Get Education Degree List Response: " + degreeList);
    return degreeList;
  }

  @Operation(summary = "Get Reference type List")
  @GetMapping("/picklists/reference-types")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json",
          schema = @Schema(type = "List<PickListDTO>", example = "[{\"name\": \"string\"}]")))})
  public List<PickListDTO> getReferencesType() {
    log.info("Get Reference type List API call");
    List<PickListDTO> referenceTypeList = service.getReferencesType();
    log.info("Successfully send Reference type List " + referenceTypeList.size());
    log.debug("Get Reference type List API Response: " + referenceTypeList);
    return referenceTypeList;
  }

  @Operation(summary = "Get Countries")
  @GetMapping("/picklists/countries")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json",
          schema = @Schema(type = "List<PickListDTO>",
              example = "[{\"id\": \"string\",\"name\": \"string\"}]")))})
  public List<PickListDTO> getCountries() {
    log.info("Get Countries List API call");
    List<PickListDTO> countriesList = service.getCountries();
    log.info("Successfully send Countries List " + countriesList.size());
    log.debug("Get Countries List API Response: " + countriesList);
    return countriesList;
  }

  @Operation(summary = "Get Industries")
  @GetMapping("/picklists/industries")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json",
          schema = @Schema(type = "List<PickListDTO>",
              example = "[{\"id\": \"string\",\"name\": \"string\"}]")))})
  public List<PickListDTO> getListOfIndustries() {
    log.info("Get industries List API call");
    List<PickListDTO> industriesList = service.getListOfIndustries();
    log.info("Successfully send industries List " + industriesList.size());
    log.debug("Get Countries List API Response: " + industriesList);
    return industriesList;
  }
}
