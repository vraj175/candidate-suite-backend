package com.aspire.kgp.service;

import java.util.List;
import java.util.Set;

import com.aspire.kgp.model.WebSocketNotification;

public interface WebSocketNotificationService {



  boolean sendWebSocketNotification(String galaxyId, String contactId, String notificationType,
      String notificationSendTo);

  Set<String> getContactKgpTeamDetails(String contactId);

  void updateReadNotification(String id, String galaxyId, String userType);

  List<WebSocketNotification> getUnreadNotification(String galaxyId, String notificationUserType);

  List<WebSocketNotification> getAllNotification(String galaxyId, String notificationUserType);
}
