package com.aspire.kgp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;

@Repository
public interface WebSocketNotificationRepository
    extends JpaRepository<WebSocketNotification, Long> {

  List<WebSocketNotification> findByUser(User user);

  List<WebSocketNotification> findByUserAndIsReadableFalse(User user);
}
