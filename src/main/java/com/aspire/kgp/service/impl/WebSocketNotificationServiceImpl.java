package com.aspire.kgp.service.impl;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;
import com.aspire.kgp.repository.WebSocketNotificationRepository;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.WebSocketNotificationService;

@Service
public class WebSocketNotificationServiceImpl implements WebSocketNotificationService {
  public static Set<String> socketIdSet = new HashSet<>();

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
      // messagingTemplate.convertAndSend("/response/webSocketNotification", webSocketNotification);

      socketIdSet.stream().forEach(e -> messagingTemplate.convertAndSendToUser(e,
          "/response/webSocketNotification", webSocketNotification));


    } else {
      throw new NotFoundException("Partner Not Found");
    }
  }

}
