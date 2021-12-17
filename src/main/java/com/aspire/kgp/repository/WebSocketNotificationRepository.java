package com.aspire.kgp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;

@Repository
public interface WebSocketNotificationRepository
    extends JpaRepository<WebSocketNotification, Long> {

  List<WebSocketNotification> findByUserAndNotificationUserTypeAndIsReadable(User user,
      String partner, boolean b);

  List<WebSocketNotification> findByUserAndNotificationUserType(User user, String partner);

  @Modifying
  @Query("update WebSocketNotification webNotif set webNotif.isReadable = true where webNotif.id = :id")
  void updateReadNotification(String id);
}
