package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;

public interface WebSocketNotificationService {

  List<WebSocketNotification> findByUser(User user);

  void loginUser(User user);

  void logOutUser(User user);
}
