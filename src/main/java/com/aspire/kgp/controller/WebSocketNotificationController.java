package com.aspire.kgp.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.WebSocketNotificationService;

@RestController
@RequestMapping("/api/v1.0")
public class WebSocketNotificationController {
  static Log log = LogFactory.getLog(WebSocketNotificationController.class.getName());

  @Autowired
  WebSocketNotificationService service;

  @Autowired
  UserService userService;

  @GetMapping(value = {"/socket/unread-notification"})
  public List<WebSocketNotification> getUnreadUserNotification(HttpServletRequest request) {
    User loginUser = (User) request.getAttribute("user");
    if (Constant.PARTNER.equalsIgnoreCase(loginUser.getRole().getName())) {
      User user = userService.findByGalaxyId(loginUser.getGalaxyId());
      return service.findByUserAndIsReadableFalse(user);
    } else {
      throw new NotFoundException("Partner Not Found");
    }
  }

  @GetMapping(value = {"/socket/all-notification"})
  public List<WebSocketNotification> getAllUserNotification(HttpServletRequest request) {
    User loginUser = (User) request.getAttribute("user");
    if (Constant.PARTNER.equalsIgnoreCase(loginUser.getRole().getName())) {
      User user = userService.findByGalaxyId(loginUser.getGalaxyId());
      return service.findByUser(user);
    } else {
      throw new NotFoundException("Partner Not Found");
    }
  }


  @MessageMapping("/webSocket-notification")
  @SendTo("/response/messages")
  public String send() throws Exception {
    String time = new SimpleDateFormat("HH:mm").format(new Date());
    return time;
  }
}
