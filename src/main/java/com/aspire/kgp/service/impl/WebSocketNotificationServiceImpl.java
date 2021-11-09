package com.aspire.kgp.service.impl;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;
import com.aspire.kgp.repository.WebSocketNotificationRepository;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.WebSocketNotificationService;

@Service
public class WebSocketNotificationServiceImpl implements WebSocketNotificationService {
  private Set<String> loggedInUserSet = new HashSet<>();

  @Autowired
  UserService userService;

  @Autowired
  WebSocketNotificationRepository repository;

  @Override
  public List<WebSocketNotification> findByUser(User user) {
    return repository.findByUser(user);
  }

  @Override
  public void loginUser(User user) {
    loggedInUserSet.add(user.getGalaxyId());
  }

  @Override
  public void logOutUser(User user) {
    loggedInUserSet.remove(user.getGalaxyId());
  }


  @SendTo("response/user-notification")
  public WebSocketNotification addWebSocketNotification(String galaxyId, String notificationType) {
    User user = userService.findByGalaxyId(galaxyId);
    if (user != null) {
      WebSocketNotification webSocketNotification = new WebSocketNotification();
      webSocketNotification.setUser(user);
      webSocketNotification.setNotificationType(notificationType);
      webSocketNotification.setDate(new Timestamp(System.currentTimeMillis()));
      repository.save(webSocketNotification);
      if (loggedInUserSet.contains(user.getGalaxyId())) {
        return webSocketNotification;
      }
    } else {
      throw new NotFoundException("Partner Not Found");
    }
    return new WebSocketNotification();
  }
}
