package com.aspire.kgp.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.NotificationSchedulerDTO;
import com.aspire.kgp.dto.NotificationsDTO;
import com.aspire.kgp.dto.WebSocketNotificationDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.Notification;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;
import com.aspire.kgp.service.NotificationService;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.WebSocketNotificationService;
import com.aspire.kgp.service.impl.NotificationSchedulerServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1.0")
@Tag(name = "Notification", description = "Rest API For Notification")
public class NotificationController {

  static Log log = LogFactory.getLog(NotificationController.class.getName());

  @Autowired
  WebSocketNotificationService webSocketNotificationService;

  @Autowired
  UserService userService;

  @Autowired
  NotificationService service;

  @Autowired
  private NotificationSchedulerServiceImpl notificationSchedulerServiceImpl;

  @Operation(summary = "Get list of Notifications")
  @GetMapping(value = "/notification/all")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "200", description = "OK",
          content = @Content(mediaType = "application/json", schema = @Schema(
              type = "List<NotificationDTO>",
              example = "[{\"id\": \"String\",\"description\": \"String\",\"status\": false}]")))})
  public List<NotificationsDTO> getNotification(HttpServletRequest request) {
    User user = (User) request.getAttribute("user");
    if (user == null) {
      throw new APIException("Invalid User Id");
    }
    log.info("Get list of Notification API call");
    List<NotificationsDTO> notificationList = service.findByUser(user);
    log.info("Successfully send list of Notification " + notificationList.size());
    log.debug("Get list of Notification API Response: " + notificationList);
    return notificationList;
  }


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

  @PostMapping(value = "/notification/add")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = "application/json", schema = @Schema(type = "Json",
          example = "{  \"timestamp\": \"2021-09-06T08:53:39.690+00:00\", \"status\": \"OK\", \"message\": \"Notification Added Successfully\" }")))})
  public ResponseEntity<Object> addNotification(@Valid @RequestBody NotificationsDTO notifications,
      HttpServletRequest request) {
    User user = (User) request.getAttribute("user");
    if (user == null) {
      throw new APIException("Invalid User Id");
    }
    Notification result = service.addNotification(notifications, user);
    if (result != null) {
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, HttpStatus.OK);
      body.put(Constant.MESSAGE, "Notification Added Successfully");
      return new ResponseEntity<>(body, HttpStatus.OK);
    }
    throw new APIException("Error in add new Notification");
  }


  @Operation(summary = "Get KGP team Unread Notification")
  @GetMapping(value = {"/notification/socket/kgp-team-unread"})
  public List<WebSocketNotification> getUnreadTeamNotification(HttpServletRequest request) {
    User loginUser = (User) request.getAttribute("user");
    if (Constant.PARTNER.equalsIgnoreCase(loginUser.getRole().getName())) {
      User user = userService.findByGalaxyId(loginUser.getGalaxyId());
      return webSocketNotificationService.getKgpTeamUnreadNotification(user);
    } else {
      throw new NotFoundException("Partner Not Found");
    }
  }

  @Operation(summary = "Get KGP team ALL Notification")
  @GetMapping(value = {"/notification/socket/kgp-team-all"})
  public List<WebSocketNotification> getAllTeamNotification(HttpServletRequest request) {
    User loginUser = (User) request.getAttribute("user");
    if (Constant.PARTNER.equalsIgnoreCase(loginUser.getRole().getName())) {
      User user = userService.findByGalaxyId(loginUser.getGalaxyId());
      return webSocketNotificationService.getKgpTeamAllNotification(user);
    } else {
      throw new NotFoundException("Partner Not Found");
    }
  }

  @Operation(summary = "Get Contact Unread Notification")
  @GetMapping(value = {"/notification/socket/contact-unread/{contactId}"})
  public List<WebSocketNotification> getUnreadContactNotification(HttpServletRequest request,
      @PathVariable("contactId") String contactId) {
    return webSocketNotificationService.getContactUnreadNotification(contactId);
  }

  @Operation(summary = "Get Contact ALL Notification")
  @GetMapping(value = {"/notification/socket/contact-all/{contactId}"})
  public List<WebSocketNotification> getAllContactNotification(HttpServletRequest request,
      @PathVariable("contactId") String contactId) {
    return webSocketNotificationService.getContactAllNotification(contactId);
  }

  @MessageMapping("/notification/socket/read/{id}/{galaxyId}/{user_type}")
  public void readTeamNotification(HttpServletRequest request, @PathVariable("id") String id,
      @PathVariable("galaxyId") String galaxyId, @PathVariable("user_type") String userType) {
    // User loginUser = (User) request.getAttribute("user");
    webSocketNotificationService.updateReadNotification(id, galaxyId, userType);
  }


  @Operation(summary = "Athena report completed notification send")
  @PostMapping(value = Constant.PUBLIC_API_URL + "/add-notification-externalSource")
  public ResponseEntity<Object> externalNotification(
      @Valid @RequestBody WebSocketNotificationDTO webSocketNotificationDTO,
      HttpServletRequest request) {
    Set<String> kgpTeamSet = webSocketNotificationService
        .getContactKgpTeamDetails(webSocketNotificationDTO.getContactId());

    for (String galaxyId : kgpTeamSet) {
      webSocketNotificationService.sendWebSocketNotification(galaxyId,
          webSocketNotificationDTO.getContactId(), webSocketNotificationDTO.getNotificationType(),
          Constant.PARTNER);
    }

    Map<String, Object> body = new LinkedHashMap<>();
    body.put(Constant.TIMESTAMP, new Date());
    body.put(Constant.STATUS, HttpStatus.OK);
    body.put(Constant.MESSAGE, "Notification Send successfully");
    log.info("Successfully send external source notification for " + kgpTeamSet.size());
    return new ResponseEntity<>(body, HttpStatus.OK);
  }


}


