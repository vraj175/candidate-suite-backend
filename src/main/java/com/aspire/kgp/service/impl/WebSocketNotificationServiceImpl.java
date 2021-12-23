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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Service;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.MissingAuthTokenException;
import com.aspire.kgp.exception.UnauthorizedAccessException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;
import com.aspire.kgp.repository.WebSocketNotificationRepository;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.WebSocketNotificationService;
import com.aspire.kgp.util.CommonUtil;
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

  @Value("${spring.api.secret.key}")
  private String apiSecretKey;

  @Autowired
  UserService service;

  @Autowired
  private JwtTokenStore jwtTokenStore;

  @Override
  public void setSocketMap(String key, String value) {
    this.socketMap.put(key, value);
  }

  @Override
  public void removeFromSocketMap(String key) {
    this.socketMap.remove(key);
  }

  /*
   * Get Unread Notification for partner or contact. galaxy id is contact Id or galaxy Team Id
   * 
   */
  @Override
  public List<WebSocketNotification> getUnreadNotification(String galaxyId,
      String notificationUserType) {
    User user = userService.findByGalaxyId(galaxyId);
    log.info("Fetching.. Unread Notification");
    if (user != null && notificationUserType.equals(Constant.PARTNER)) {
      return repository.findByUserAndNotificationUserTypeAndIsReadableFalse(user,
          notificationUserType);
    } else if (user != null && notificationUserType.equals(Constant.CONTACT)) {
      return repository.findByUserAndNotificationUserTypeAndIsReadableFalse(user,
          notificationUserType);
    } else
      throw new APIException("Invalid galaxyId " + galaxyId);
  }

  /*
   * Get ALL Notification for partner or contact. galaxy id is contact Id or galaxy Team Id
   */
  @Override
  public List<WebSocketNotification> getAllNotification(String galaxyId,
      String notificationUserType) {
    User user = userService.findByGalaxyId(galaxyId);
    log.info("Fetching.. ALL Notification");
    if (user != null && notificationUserType.equals(Constant.PARTNER)) {
      return repository.findByUserAndNotificationUserType(user, notificationUserType);
    } else if (user != null && notificationUserType.equals(Constant.CONTACT)) {
      return repository.findByUserAndNotificationUserType(user, notificationUserType);
    } else
      throw new APIException("Invalid galaxyId " + galaxyId);
  }

  /*
   * Update read status
   */
  @Override
  @Transactional
  public void updateReadNotification(String id, String galaxyId, String userType) {
    try {
      Map<String, Object> body = new LinkedHashMap<>();
      body.put(Constant.TIMESTAMP, new Date());
      body.put(Constant.STATUS, "200");
      if (Constant.PARTNER.equals(userType)) {
        repository.updateReadNotification(Long.parseLong(id));
        body.put(Constant.MESSAGE, "KGP Team notification successfully Read By " + galaxyId);
        getSessionIdFromMap(galaxyId, Constant.PARTNER).stream()
            .forEach(e -> messagingTemplate.convertAndSendToUser(e, "/response/readNotification",
                new ResponseEntity<>(body, HttpStatus.OK)));
      } else if (Constant.CONTACT.equals(userType)) {
        repository.updateReadNotification(Long.parseLong(id));
        body.put(Constant.MESSAGE, "Contact notification successfully Read By" + galaxyId);
        getSessionIdFromMap(galaxyId, Constant.CONTACT).stream()
            .forEach(e -> messagingTemplate.convertAndSendToUser(e, "/response/readNotification",
                new ResponseEntity<>(body, HttpStatus.OK)));

      } else {
        log.error("Invalid User Type");
      }
    } catch (Exception e) {
      throw new APIException(
          "Error in update read status for KGP Team notification by " + galaxyId);
    }
    log.info("KGP team read notification status successfully update for galaxyId: " + galaxyId);
    log.debug(
        "Update read status for ID:" + id + "Galaxy Id:" + galaxyId + "User Type:" + userType);
  }

  /*
   * method call when want to send notification.Send notification if id is currently connected
   * 
   */
  @Override
  @Transactional
  public boolean sendWebSocketNotification(String kgpTeamId, String contactId,
      String notificationType, String notificationUserType) {
    log.info("Add web socket notification for " + notificationType + " to " + notificationUserType
        + "kgpTeam Id: " + kgpTeamId + " contact Id: " + contactId);
    WebSocketNotification webSocketNotification = new WebSocketNotification();
    webSocketNotification.setNotificationType(notificationType);
    webSocketNotification.setContactId(contactId);
    webSocketNotification.setDate(new Timestamp(System.currentTimeMillis()));
    webSocketNotification.setNotificationUserType(notificationUserType);

    try {
      if (notificationUserType.equals(Constant.CONTACT)) {
        User user = userService.findByGalaxyId(contactId);
        if (user != null) {
          webSocketNotification.setUser(user);
          repository.save(webSocketNotification);
          log.debug("Successfully add Notification for " + user.getGalaxyId());
          getSessionIdFromMap(contactId, Constant.CONTACT).stream().forEach(e -> messagingTemplate
              .convertAndSendToUser(e, "/queue/reply", webSocketNotification));
        } else {
          log.info("Contact is not available for : " + contactId);
          return false;
        }

      } else {
        User user = userService.findByGalaxyId(kgpTeamId);
        if (user != null) {
          webSocketNotification.setUser(user);
          repository.save(webSocketNotification);
          log.debug("Successfully add Notification for " + user.getGalaxyId());
          getSessionIdFromMap(kgpTeamId, Constant.PARTNER).stream().forEach(e -> messagingTemplate
              .convertAndSendToUser(e, "/queue/reply", webSocketNotification));
        } else {
          log.info("Kgp Team Member is not available for : " + kgpTeamId);
          return false;
        }
      }
    } catch (Exception e) {
      log.error("Error While Sending socket notification " + e.getMessage());
      return false;
    }
    log.info("Notification Send Successfully");
    return true;
  }

  /*
   * Get connected Id
   */
  private Set<String> getSessionIdFromMap(String id, String notificationUserType) {
    log.info("Get sessionId from Map...");
    Set<String> socketIdSet = new HashSet<>();

    if (notificationUserType.equals(Constant.CONTACT)) {
      socketMap.entrySet().forEach(entry -> {
        if (entry.getValue().equals(id + "-" + Constant.CONTACT)) {
          socketIdSet.add(entry.getKey());
        }
      });
    } else {
      socketMap.entrySet().forEach(entry -> {
        if (entry.getValue().equals(id + "-" + Constant.PARTNER)) {
          socketIdSet.add(entry.getKey());
        }
      });
    }
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

  public void jwtTokenCheck(String accessToken)
      throws UnauthorizedAccessException, MissingAuthTokenException {
    if (accessToken == null) {
      throw new MissingAuthTokenException(Constant.MISSING_REQUEST_HEADER);
    }
    if (!accessToken.startsWith("Bearer ")) {
      throw new UnauthorizedAccessException(Constant.INVALID_AUTHENTICATION);
    }
    accessToken = accessToken.split(" ")[1].trim();

    try {
      OAuth2Authentication authentication = jwtTokenStore.readAuthentication(accessToken);
      if (authentication.getUserAuthentication() == null) {
        throw new UnauthorizedAccessException("Malformed Token");
      } else {
        if (jwtTokenStore.readAccessToken(accessToken).getExpiresIn() <= 0) {
          throw new UnauthorizedAccessException("Token Expired");
        }
      }
      String userId = authentication.getUserAuthentication().getName();
      User user = service.findByEmail(userId);
      UserDTO userDTO = null;
      if (user.getRole().getName().equalsIgnoreCase(Constant.PARTNER)) {
        userDTO = service.getGalaxyUserDetails(user.getGalaxyId());
      } else {
        userDTO = service.getContactDetails(user.getGalaxyId());
      }
      if (userDTO.getFirstName() == null) {
        throw new UnauthorizedAccessException("Invalid User or User Delete from Galaxy");
      }
    } catch (UnauthorizedAccessException e) {
      throw new UnauthorizedAccessException(e.getMessage());
    } catch (Exception e) {
      throw new UnauthorizedAccessException("Malformed Token");
    }
  }



  public void apiKeyCheck(String apiKey)
      throws UnauthorizedAccessException, MissingAuthTokenException {
    if (apiKey == null) {
      throw new MissingAuthTokenException(Constant.MISSING_API_KEY);
    }
    if (!CommonUtil.verifyHash(apiSecretKey, apiKey)) {
      throw new UnauthorizedAccessException(Constant.INVALID_API_KEY);
    }
  }
}
