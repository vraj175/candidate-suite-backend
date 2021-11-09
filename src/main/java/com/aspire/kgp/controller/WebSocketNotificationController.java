package com.aspire.kgp.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.WebSocketNotificationService;

@Controller
public class WebSocketNotificationController {
  static Log log = LogFactory.getLog(WebSocketNotificationController.class.getName());

  @Autowired
  WebSocketNotificationService service;

  @Autowired
  UserService userService;

  @MessageMapping("user-notification")
  @SendTo("response/user-notification")
  public List<WebSocketNotification> getUserNotification(User loginUser) {
    if (Constant.PARTNER.equalsIgnoreCase(loginUser.getRole().getName())) {
      User user = userService.findByGalaxyId(loginUser.getGalaxyId());
      return service.findByUser(user);
    } else {
      throw new NotFoundException("Partner Not Found");
    }
  }

  @MessageMapping("user-login")
  public void loginUser(User loginUser) {
    if (Constant.PARTNER.equalsIgnoreCase(loginUser.getRole().getName())) {
      service.loginUser(loginUser);
    } else {
      throw new NotFoundException("Partner Not Found");
    }
  }

  @MessageMapping("user-logout")
  public void logOutUser(User loginUser) {
    if (Constant.PARTNER.equalsIgnoreCase(loginUser.getRole().getName())) {
      service.logOutUser(loginUser);
    } else {
      throw new NotFoundException("Partner Not Found");
    }
  }
}
