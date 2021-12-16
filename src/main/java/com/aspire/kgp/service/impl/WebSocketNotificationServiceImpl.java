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
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;
import com.aspire.kgp.repository.WebSocketNotificationRepository;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.WebSocketNotificationService;
import com.aspire.kgp.util.RestUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class WebSocketNotificationServiceImpl implements WebSocketNotificationService {
  static Log log = LogFactory.getLog(WebSocketNotificationServiceImpl.class.getName());

  private Map<String, String> socketMap = new HashMap<>();

  private Set<String> kgpPartnerIdList;

  private SimpMessagingTemplate messagingTemplate;

  @Autowired
  public WebSocketNotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @Autowired
  RestUtil restUtil;

  @Autowired
  UserService userService;

  @Autowired
  WebSocketNotificationRepository repository;

  public void setSocketMap(String key, String value) {
    this.socketMap.put(key, value);
  }

  @Override
  public List<WebSocketNotification> getKgpTeamUnreadNotification(User user) {
    return repository.findByUserAndNotificationDestAndIsReadableFalse(user, Constant.PARTNER);
  }

  @Override
  public List<WebSocketNotification> getKgpTeamAllNotification(User user) {
    return repository.findByUserAndNotificationDest(user, Constant.PARTNER);
  }

  @Override
  public List<WebSocketNotification> getContactUnreadNotification(String contactId) {
    return repository.findByContactIdAndNotificationDestAndIsReadableFalse(contactId,
        Constant.CONTACT);
  }

  @Override
  public List<WebSocketNotification> getContactAllNotification(String contactId) {
    return repository.findByContactIdAndNotificationDest(contactId, Constant.PARTNER);
  }

  @Override
  public void updateKgpTeamReadNotification(User user) {
    try {
      repository.updateKgpTeamReadNotification(user);
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, "200");
      body.put(Constant.MESSAGE,
          "KGP Team notification successfully Read By " + user.getGalaxyId());
      getKgpTeamUserWiseSessionIdFromMap(user.getGalaxyId()).stream()
          .forEach(e -> messagingTemplate.convertAndSendToUser(e,
              "/response/readGalaxyUserNotification", new ResponseEntity<>(body, HttpStatus.OK)));
      log.info("KGP team read notification status successfully update for galaxyId: "
          + user.getGalaxyId());
    } catch (Exception e) {
      throw new APIException(
          "Error in update read status for KGP Team notification by " + user.getGalaxyId());
    }
  }

  @Override
  public void updateContactReadNotification(String contactId) {
    try {
      repository.updateContactReadNotification(contactId);
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, "200");
      body.put(Constant.MESSAGE, "Contact notification successfully Read By" + contactId);
      getContactWiseSessionIdFromMap(contactId).stream()
          .forEach(e -> messagingTemplate.convertAndSendToUser(e,
              "/response/readContactNotification", new ResponseEntity<>(body, HttpStatus.OK)));
      log.info("Contact read notification status successfully update for contactId: " + contactId);
    } catch (Exception e) {
      throw new APIException(
          "Error in update read status for contact notification by " + contactId);
    }
  }

  @Override
  @Transactional
  public void sendWebSocketNotification(String galaxyId, String contactId, String notificationType,
      String notificationDest) {
    log.info("Add web socket notification for " + notificationType + " to " + notificationDest
        + "Galaxy Id: " + galaxyId + " contact Id: " + contactId);
    WebSocketNotification webSocketNotification = new WebSocketNotification();
    webSocketNotification.setNotificationType(notificationType);
    webSocketNotification.setContactId(contactId);
    webSocketNotification.setDate(new Timestamp(System.currentTimeMillis()));
    webSocketNotification.setNotificationDest(notificationDest);

    if (notificationDest.equals(Constant.CONTACT)) {
      webSocketNotification.setUser(null);
      repository.save(webSocketNotification);
      getContactWiseSessionIdFromMap(contactId).stream().forEach(e -> messagingTemplate
          .convertAndSendToUser(e, "/response/webSocketNotification", webSocketNotification));
    } else {
      User user = userService.findByGalaxyId(galaxyId);
      if (user != null) {
        webSocketNotification.setUser(user);
        repository.save(webSocketNotification);
        getKgpTeamUserWiseSessionIdFromMap(galaxyId).stream().forEach(e -> messagingTemplate
            .convertAndSendToUser(e, "/response/webSocketNotification", webSocketNotification));
      } else {
        log.info("User is not available for : " + galaxyId);
      }
    }
    log.info("Notification Send Successfully");
  }

  private Set<String> getContactWiseSessionIdFromMap(String contactId) {
    log.info("Get contact wise sessionId from Map...");
    Set<String> socketIdSet = new HashSet<>();

    socketMap.entrySet().forEach(entry -> {
      if (entry.getValue().equals(contactId + "-" + Constant.CONTACT)) {
        socketIdSet.add(entry.getKey());
      }
    });
    return socketIdSet;
  }

  private Set<String> getKgpTeamUserWiseSessionIdFromMap(String galaxyId) {
    log.info("Get KGP team userwise sessionId from Map...");
    Set<String> socketIdSet = new HashSet<>();

    socketMap.entrySet().forEach(entry -> {
      if (entry.getValue().equals(galaxyId + "-" + Constant.PARTNER)) {
        socketIdSet.add(entry.getKey());
      }
    });
    return socketIdSet;
  }

  @Override
  public Set<String> getContactKgpTeamDetails(String contactId) {
    String apiResponse = restUtil
        .newGetMethod(Constant.CONTACT_KGP_TEAM_URL.replace(Constant.CONTACT_ID, contactId));
    JsonArray jsonArray = (JsonArray) JsonParser.parseString(apiResponse);
    kgpPartnerIdList = new HashSet<>();
    for (JsonElement jsonElement : jsonArray) {
      addJsonArraytoList(jsonElement.getAsJsonObject(), "partners");
      addJsonArraytoList(jsonElement.getAsJsonObject(), "recruiters");
    }
    return kgpPartnerIdList;
  }

  private void addJsonArraytoList(JsonObject json, String listfor) {
    JsonArray partnerArray = json.getAsJsonArray(listfor);
    if (partnerArray != null) {
      partnerArray.forEach(e -> {
        UserDTO userDTO =
            new Gson().fromJson(e.getAsJsonObject().get("user"), new TypeToken<UserDTO>() {

              /**
               * 
               */
              private static final long serialVersionUID = 1L;
            }.getType());
        kgpPartnerIdList.add(userDTO.getId());
      });
    }
  }
}
