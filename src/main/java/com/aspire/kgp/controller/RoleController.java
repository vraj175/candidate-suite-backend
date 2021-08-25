package com.aspire.kgp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.Role;
import com.aspire.kgp.service.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Api(tags = {"Role"})
@SwaggerDefinition(tags = {@Tag(name = "Role", description = "REST API for Role")})
public class RoleController {

  @Autowired
  RoleService service;

  @ApiOperation(value = "Initialize Roles")
  @PostMapping(value = Constant.PUBLIC_API_URL + "/roles/initialize")
  public String initializeLanguages() {
    return service.initializeData();
  }

  @ApiOperation(value = "Get list of Roles")
  @GetMapping(value = "/roles")
  public List<Role> getRoles() {
    return service.findAll();
  }

}
