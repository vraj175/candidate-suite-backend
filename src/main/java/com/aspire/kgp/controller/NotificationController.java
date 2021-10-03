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

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.NotificationSchedulerDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.service.impl.NotificationSchedulerServiceImpl;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1.0")
public class NotificationController {

  @Autowired
  private NotificationSchedulerServiceImpl notificationSchedulerServiceImpl;

  @Operation(summary = "Set Interview notification Schedule")
  @PostMapping("/notification")
  public ResponseEntity<Object> setNotification(
      @Valid @RequestBody NotificationSchedulerDTO notificationDTO, HttpServletRequest request) {
    boolean result = notificationSchedulerServiceImpl.setcandidateInterview(notificationDTO);
    if (result) {
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, HttpStatus.OK);
      body.put(Constant.MESSAGE, "Successfully set Interview notification");
      return new ResponseEntity<>(body, HttpStatus.OK);
    }
    throw new APIException("Error in Set Notification Schedule");
  }

}
