package com.aspire.kgp.service;

import java.util.List;

import com.aspire.kgp.dto.NotificationDTO;
import com.aspire.kgp.model.User;

public interface NotificationService {

  List<NotificationDTO> findByUser(User user);

}
