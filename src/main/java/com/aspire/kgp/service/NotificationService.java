package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.dto.NotificationsDTO;
import com.aspire.kgp.model.Notification;
import com.aspire.kgp.model.User;

public interface NotificationService {

  List<NotificationsDTO> findByUser(User user);

  Notification addNotification(NotificationsDTO notifications, User user);

}
