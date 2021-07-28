package com.aspire.kgp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
  public User inviteUser(String contactId, String language,String email, String[] BCC, boolean removeDuplicate) {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    User user = (User) request.getAttribute("user");
    user = service.findById(user.getId());
    
    return service.InviteUser(contactId, language, email, BCC, user, request);
  }

}
