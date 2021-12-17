package com.aspire.kgp.service;

import java.util.List;
import java.util.Set;

import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;

public interface WebSocketNotificationService {

  List<WebSocketNotification> getKgpTeamUnreadNotification(User user);

  List<WebSocketNotification> getContactUnreadNotification(String galaxyId);

  List<WebSocketNotification> getKgpTeamAllNotification(User user);

  List<WebSocketNotification> getContactAllNotification(String galaxyId);

  boolean sendWebSocketNotification(String galaxyId, String contactId, String notificationType,
      String notificationSendTo);

  Set<String> getContactKgpTeamDetails(String contactId);

  void updateReadNotification(String id, String galaxyId, String userType);
}
