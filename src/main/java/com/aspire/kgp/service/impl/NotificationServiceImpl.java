package com.aspire.kgp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.kgp.dto.NotificationDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.model.Notification;
import com.aspire.kgp.model.User;
import com.aspire.kgp.repository.NotificationRepository;
import com.aspire.kgp.service.NotificationService;



@Service
public class NotificationServiceImpl implements NotificationService {

  @Autowired
  NotificationRepository repository;

  @Override
  public List<NotificationDTO> findByUser(User user) {
    List<NotificationDTO> notificationDTOList = new ArrayList<>();
    List<Notification> notificationList = repository.findByUser(user);
    if (!notificationList.isEmpty()) {
      for (Notification notification : notificationList) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(String.valueOf(notification.getId()));
        notificationDTO.setDescription(notification.getDescription());
        notificationDTO.setStatus(notification.isStatus());
        notificationDTOList.add(notificationDTO);
      }
    }
    return notificationDTOList;
  }

}
