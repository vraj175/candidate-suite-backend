package com.aspire.kgp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aspire.kgp.model.Notification;
import com.aspire.kgp.model.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByUser(User user);

  List<Notification> findByUserAndId(Long id, User user);

  Notification save(Optional<Notification> notification);

}
