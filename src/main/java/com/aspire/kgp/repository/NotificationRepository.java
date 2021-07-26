package com.aspire.kgp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aspire.kgp.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
