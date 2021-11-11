package com.aspire.kgp.service.impl;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;
import com.aspire.kgp.repository.WebSocketNotificationRepository;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.WebSocketNotificationService;

import springfox.documentation.service.ResponseMessage;

@Service
public class WebSocketNotificationServiceImpl implements WebSocketNotificationService {
  private Set<String> loggedInUserSet = new HashSet<>();
  private SimpMessagingTemplate messagingTemplate;

  @Autowired
  public WebSocketNotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @Autowired
  UserService userService;

  @Autowired
  WebSocketNotificationRepository repository;

  @Override
  public List<WebSocketNotification> findByUser(User user) {
    return repository.findByUser(user);
  }

  @Override
  public List<WebSocketNotification> findByUserAndIsReadableFalse(User user) {
    return repository.findByUserAndIsReadableFalse(user);
  }

  @Override
  public void loginUser(User user) {
    loggedInUserSet.add(user.getGalaxyId());
  }

  @Override
  public void logOutUser(User user) {
    loggedInUserSet.remove(user.getGalaxyId());
  }

  @Override
  public void addWebSocketNotification(String galaxyId, String candidateId,
      String notificationType) {
    User user = userService.findByGalaxyId(galaxyId);
    if (user != null) {
      WebSocketNotification webSocketNotification = new WebSocketNotification();
      webSocketNotification.setUser(user);
      webSocketNotification.setNotificationType(notificationType);
      webSocketNotification.setCandidateId(candidateId);
      webSocketNotification.setDate(new Timestamp(System.currentTimeMillis()));
      repository.save(webSocketNotification);
      if (loggedInUserSet.contains(user.getGalaxyId())) {
        messagingTemplate.convertAndSendToUser(galaxyId, notificationType, user);
      }
    } else {
      throw new NotFoundException("Partner Not Found");
    }
  }

}
