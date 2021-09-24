package com.aspire.kgp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.dto.PickListDTO;
import com.aspire.kgp.util.PickListUtil;

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

  @Autowired
  PickListUtil pickListUtil;

  @Operation(summary = "Get Education Degree List")
  @GetMapping("/picklists/educations/degrees")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  public List<String> getEducationDegrees() {
    return pickListUtil.getEducationDegrees();
  }

  @Operation(summary = "Get Reference type List")
  @GetMapping("/picklists/reference-types")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json",
          schema = @Schema(type = "List<PickListDTO>", example = "[{\"name\": \"string\"}]")))})
  public List<PickListDTO> getReferencesType() {
    return pickListUtil.getReferencesType();
  }
}
