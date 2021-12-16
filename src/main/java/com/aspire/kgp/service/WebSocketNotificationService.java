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

  void sendWebSocketNotification(String galaxyId, String contactId, String notificationType,
      String notificationSendTo);

  void updateKgpTeamReadNotification(User user);

  void updateContactReadNotification(String contactId);

  Set<String> getContactKgpTeamDetails(String contactId);
}
