package com.aspire.kgp.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;

public interface WebSocketNotificationService {

  List<WebSocketNotification> getKgpTeamUnreadNotification(User user, String partner);

  List<WebSocketNotification> getcandidateUnreadNotification(String galaxyId, String candidate);

  List<WebSocketNotification> getKgpTeamAllNotification(User user, String partner);

  List<WebSocketNotification> getcandidateAllNotification(String galaxyId, String candidate);

  void sendWebSocketNotification(String galaxyId, String candidateId, String notificationType,
      String notificationSendTo);

  ResponseEntity<Object> updateKgpTeamReadNotification(User user);

  ResponseEntity<Object> updatecandidateReadNotification(String candidateId);
}
