package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;

public interface WebSocketNotificationService {

  List<WebSocketNotification> findByUser(User user);

  List<WebSocketNotification> findByUserAndIsReadableFalse(User user);

  void addWebSocketNotification(String galaxyId, String candidateId, String notificationType);
}
