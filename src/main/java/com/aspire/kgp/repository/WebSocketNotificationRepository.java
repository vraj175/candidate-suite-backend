package com.aspire.kgp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.User;
import com.aspire.kgp.model.WebSocketNotification;

@Repository
public interface WebSocketNotificationRepository
    extends JpaRepository<WebSocketNotification, Long> {

  List<WebSocketNotification> findByUserAndNotificationDestAndIsReadableFalse(User user,
      String partner);

  List<WebSocketNotification> findByContactIdAndNotificationDestAndIsReadableFalse(String galaxyId,
      String candidate);

  List<WebSocketNotification> findByUserAndNotificationDest(User user, String partner);

  List<WebSocketNotification> findByContactIdAndNotificationDest(String contactId,
      String candidate);

  @Modifying
  @Query("update WebSocketNotification webNotif set webNotif.isReadable = true where webNotif.user = :user and webNotif.notificationDest = 'partner'")
  void updateKgpTeamReadNotification(@Param("user") User user);

  @Modifying
  @Query("update WebSocketNotification webNotif set webNotif.isReadable = true where webNotif.contactId = :contactId and webNotif.notificationDest = 'contact'")
  void updateContactReadNotification(@Param("contactId") String contactId);
}
