package com.aspire.kgp.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.kgp.model.NotificationSchedule;

@Repository
public interface NotificationSchedulerRepository extends JpaRepository<NotificationSchedule, Long> {

  @Transactional
  @Modifying
  @Query("delete from NotificationSchedule u where u.scheduleId = ?1 AND DATE(u.date) =?2")
  void deleteByScheduleIdAndDate(String scheduleId, Date currentDate);
}
