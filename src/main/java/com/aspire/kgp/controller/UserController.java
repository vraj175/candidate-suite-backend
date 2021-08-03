package com.aspire.kgp.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<Object> inviteUser(@Valid @RequestBody InviteDTO invite, HttpServletRequest request) {
    User user = (User) request.getAttribute("user");
    boolean result = service.InviteUser(invite.getCandidateId(), invite.getLanguage(), invite.getEmail(),
        invite.getBcc(), user, invite.isRemoveDuplicate(), request);
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", new Date());
    
    if(result) {
      body.put("status", HttpStatus.OK);
      body.put("message", "User invited successfully");

      return new ResponseEntity<>(body, HttpStatus.OK);
    }
    
    body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
    body.put("message", "Error in send invite");

    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
