package com.aspire.kgp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.dto.InviteDTO;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Api(tags = {"User"})
@SwaggerDefinition(tags = {@Tag(name = "User", description = "REST API for User")})
public class UserController {

  @Autowired
  UserService service;

  @ApiOperation(value = "Invite User as Candidates")
  @PostMapping(value = "/user/invite")
  public User inviteUser(@Valid @RequestBody InviteDTO invite, HttpServletRequest request) {
    User user = (User) request.getAttribute("user");
    return service.InviteUser(invite.getCandidateId(), invite.getLanguage(), invite.getEmail(),
        invite.getBcc(), user, invite.isRemoveDuplicate(), request);
  }

}
