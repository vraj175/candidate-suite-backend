package com.aspire.kgp.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.InviteDTO;
import com.aspire.kgp.dto.ResetPasswordDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.util.CommonUtil;
import com.aspire.kgp.util.RestUtil;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Tag(name = "User", description = "REST API for User")
public class UserController {

  @Autowired
  UserService service;

  @Autowired
  RestUtil restUtil;

  @Operation(summary = "Invite User as Candidates")
  @PostMapping(value = Constant.PUBLIC_API_URL + "/user/invite")
  public ResponseEntity<Object> inviteUser(@Valid @RequestBody InviteDTO invite,
      HttpServletRequest request) {
    User user = service.findByGalaxyId(invite.getPartnerId());
    if (user == null) {
      UserDTO userDTO = service.getGalaxyUserDetails(invite.getPartnerId());
      if (CommonUtil.checkNotNullString(userDTO.getId()))
        user = service.saveOrUpdatePartner(userDTO.getId(), userDTO.getEmail(), userDTO.getEmail(),
            false);
    }

    if (user == null) {
      throw new NotFoundException("Invalid Partner Id");
    }

    boolean result = service.inviteUser(invite.getCandidateId(), invite.getLanguage(),
        invite.getEmail(), invite.getBcc(), user, invite.isRemoveDuplicate(), request);
    if (result) {
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, HttpStatus.OK);
      body.put(Constant.MESSAGE, "User invited successfully");
      return new ResponseEntity<>(body, HttpStatus.OK);
    }
    throw new APIException("Error in send invite");
  }

  @Operation(summary = "get user profile details ")
  @GetMapping(value = "/user/profile")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "UserDTO",
          example = "{\"id\": \"string\",\"firstName\": \"string\",\"lastName\": \"string\",\"email\": \"string\",\"role\": \"string\",\"passwordReset\": true}")))})
  public MappingJacksonValue getUserProfile(HttpServletRequest request) {
    User user = (User) request.getAttribute("user");
    UserDTO userDTO = null;
    String role = user.getRole().getName();
    if (Constant.PARTNER.equalsIgnoreCase(role)) {
      userDTO = service.getGalaxyUserDetails(user.getGalaxyId());
    } else {
      userDTO = service.getContactDetails(user.getGalaxyId());
    }
    if (userDTO.getFirstName() == null) {
      throw new APIException("Something went wrong to fetch the user data");
    }
    userDTO.setEmail(user.getEmail());
    userDTO.setRole(role);
    userDTO.setPasswordReset(user.isPasswordReset());

    SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName",
        "lastName", "email", "role", "passwordReset");

    FilterProvider filters = new SimpleFilterProvider().addFilter("userFilter", filter);
    MappingJacksonValue mapping = new MappingJacksonValue(userDTO);
    mapping.setFilters(filters);
    return mapping;
  }

  @Operation(summary = "Forgot password for candidate")
  @PostMapping(value = Constant.PUBLIC_API_URL + "/user/forgotPassword")
  public ResponseEntity<Object> forgotUserPassword(@RequestBody String email,
      HttpServletRequest request) {

    boolean result = service.forgotPassword(request, email);
    if (result) {
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, HttpStatus.OK);
      body.put(Constant.MESSAGE, "Forgot password e-mail is sent to the provided user.");
      return new ResponseEntity<>(body, HttpStatus.OK);
    }
    throw new APIException("Something went wrong");
  }

  @Operation(summary = "Reset Password for User")
  @PostMapping(value = Constant.PUBLIC_API_URL + "/user/resetPassword")
  public ResponseEntity<Object> resetUserPassword(
      @Valid @RequestBody ResetPasswordDTO resetPassword, HttpServletRequest request) {
    boolean result = service.resetPassword(request, resetPassword);
    if (result) {
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, HttpStatus.OK);
      body.put(Constant.MESSAGE, "User Password reset successfully.");
      return new ResponseEntity<>(body, HttpStatus.OK);
    }
    throw new APIException("Something went wront");
  }

  @Operation(summary = "Verify recaptcha response from google")
  @GetMapping(value = Constant.PUBLIC_API_URL + "/user/verify/recaptcha/{response}")
  public String performVerifyGoogleCaptchaRequest(@PathVariable("response") String response) {
    return restUtil.performVerifyGoogleCaptchaRequest(response);
  }
}
