package com.aspire.kgp.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.dto.NotificationsDTO;
import com.aspire.kgp.model.Notification;
import com.aspire.kgp.model.User;
import com.aspire.kgp.repository.NotificationRepository;
import com.aspire.kgp.service.NotificationService;



@Service
public class NotificationServiceImpl implements NotificationService {

  @Autowired
  NotificationRepository repository;

  @Override
  public List<NotificationsDTO> findByUser(User user) {
    List<NotificationsDTO> notificationDTOList = new ArrayList<>();
    List<Notification> notificationList = repository.findByUser(user);
    if (!notificationList.isEmpty()) {
      for (Notification notification : notificationList) {
        NotificationsDTO notificationDTO = new NotificationsDTO();
        notificationDTO.setId(String.valueOf(notification.getId()));
        notificationDTO.setDescription(notification.getDescription());
        notificationDTO.setStatus(notification.isStatus());
        notificationDTO.setCreatedDate(notification.getCreatedDate());
        notificationDTO.setModifyDate(notification.getModifyDate());
        notificationDTOList.add(notificationDTO);
      }
    }
    return notificationDTOList;
  }

  @Override
  public Notification addNotification(NotificationsDTO notificationDTO, User user) {
    Notification notification = new Notification();
    notification.setModifyDate(new Timestamp(System.currentTimeMillis()));
    notification.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    notification.setStatus(Boolean.FALSE);
    notification.setDescription(notificationDTO.getDescription());
    notification.setUser(user);
    return saveorUpdate(notification);
  }

  public Notification saveorUpdate(Notification notification) {
    return repository.save(notification);
  }
}
