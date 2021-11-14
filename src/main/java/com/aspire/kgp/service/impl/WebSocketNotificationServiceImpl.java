package com.aspire.kgp.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;
import com.aspire.kgp.repository.WebSocketNotificationRepository;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.WebSocketNotificationService;

@Service
public class WebSocketNotificationServiceImpl implements WebSocketNotificationService {
  static Log log = LogFactory.getLog(WebSocketNotificationServiceImpl.class.getName());

  private Map<String, String> socketMap = new HashMap<>();

  private SimpMessagingTemplate messagingTemplate;

  @Autowired
  public WebSocketNotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @Autowired
  UserService userService;

  @Autowired
  WebSocketNotificationRepository repository;

  public void setSocketMap(String key, String value) {
    this.socketMap.put(key, value);
  }

  @Override
  public List<WebSocketNotification> getKgpTeamUnreadNotification(User user, String partner) {
    return repository.findByUserAndNotificationDestAndIsReadableFalse(user, partner);
  }

  @Override
  public List<WebSocketNotification> getcandidateUnreadNotification(String candidateId,
      String candidate) {
    return repository.findByCandidateIdAndNotificationDestAndIsReadableFalse(candidateId,
        candidate);
  }

  @Override
  public List<WebSocketNotification> getKgpTeamAllNotification(User user, String partner) {
    return repository.findByUserAndNotificationDest(user, partner);
  }

  @Override
  public List<WebSocketNotification> getcandidateAllNotification(String candidateId,
      String candidate) {
    return repository.findByCandidateIdAndNotificationDest(candidateId, candidate);
  }

  @Override
  public ResponseEntity<Object> updateKgpTeamReadNotification(User user) {
    try {
      repository.updateKgpTeamReadNotification(user);
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, "200");
      body.put(Constant.MESSAGE,
          "KGP Team notification successfully Read By " + user.getGalaxyId());
      return new ResponseEntity<>(body, HttpStatus.OK);
    } catch (Exception e) {
      throw new APIException(
          "Error in update read status for KGP Team notification by " + user.getGalaxyId());
    }
  }

  @Override
  public ResponseEntity<Object> updatecandidateReadNotification(String candidateId) {
    try {
      repository.updatecandidateReadNotification(candidateId);
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, "200");
      body.put(Constant.MESSAGE, "Candidate notification successfully Read By" + candidateId);
      return new ResponseEntity<>(body, HttpStatus.OK);
    } catch (Exception e) {
      throw new APIException(
          "Error in update read status for Candidate notification by " + candidateId);
    }
  }

  @Override
  @Transactional
  public void sendWebSocketNotification(String galaxyId, String candidateId,
      String notificationType, String notificationDest) {
    log.info("Add web socket notification for " + notificationType + " to " + notificationDest
        + "Galaxy Id: " + galaxyId + " Candidate Id: " + candidateId);
    WebSocketNotification webSocketNotification = new WebSocketNotification();
    webSocketNotification.setNotificationType(notificationType);
    webSocketNotification.setCandidateId(candidateId);
    webSocketNotification.setDate(new Timestamp(System.currentTimeMillis()));
    webSocketNotification.setNotificationDest(notificationDest);

    if (notificationDest.equals(Constant.CANDIDATE)) {
      webSocketNotification.setUser(null);
      repository.save(webSocketNotification);
    } else {
      User user = userService.findByGalaxyId(galaxyId);
      if (user != null) {
        webSocketNotification.setUser(user);
        repository.save(webSocketNotification);
      } else {
        throw new NotFoundException("Partner Not Found");
      }

    }
    getSessionIdFromMap(candidateId, notificationDest).stream().forEach(e -> messagingTemplate
        .convertAndSendToUser(e, "/response/webSocketNotification", webSocketNotification));
    log.info("Notification Send Successfully");
  }

  private Set<String> getSessionIdFromMap(String candidateId, String notificationDest) {
    log.info("Get Session Id from Map...");
    Set<String> socketIdSet = new HashSet<>();

    if (notificationDest.equals(Constant.CANDIDATE)) {
      socketMap.entrySet().forEach(entry -> {
        if (entry.getValue().equals(candidateId + "-" + Constant.CANDIDATE)) {
          socketIdSet.add(entry.getKey());
        }
      });
    } else {
      socketMap.entrySet().forEach(entry -> {
        if (entry.getValue().split("-")[1].equals(Constant.PARTNER)) {
          socketIdSet.add(entry.getKey());
        }
      });
    }
    return socketIdSet;
  }
}
