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
import com.aspire.kgp.model.Role;
import com.aspire.kgp.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Tag(name = "Role", description = "REST API for Role")
public class RoleController {
  static Log log = LogFactory.getLog(RoleController.class.getName());

  @Autowired
  RoleService service;

  @Operation(summary = "Initialize Roles")
  @PostMapping(value = Constant.PUBLIC_API_URL + "/roles/initialize")
  public String initializeRoles() {
    log.info("Initialize Roles API call");
    String roles = service.initializeData();
    log.info("Successfully Initialize Roles");
    log.debug("Initialize Roles List API Response: " + roles);
    return roles;
  }

  @Operation(summary = "Get list of Roles")
  @GetMapping(value = "/roles")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "List<Role>",
          example = "[{\"id\": 0,\"createdDate\": \"string\",\"modifyDate\": \"string\",\"name\": \"string\"}]")))})
  public List<Role> getRoles() {
    log.info("Get list of Roles API call");
    List<Role> roleList = service.findAll();
    log.info("Successfully send list of Roles " + roleList.size());
    log.debug("Get list of Roles API Response: " + roleList);
    return roleList;
  }

}
