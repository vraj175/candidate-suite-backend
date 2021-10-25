package com.aspire.kgp.config;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.service.InterviewNotificationService;


/*
 * Scheduler call on Specific time which is available in properties file
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
public class NotificationSchedulerConfig {

  @Autowired
  InterviewNotificationService service;

  @Scheduled(cron = "${notification.reminder.beforeDay}")
  void excuteReminderJobBeforeOneDay() {
    List<CandidateDTO> list = new ArrayList<>();
    if (!list.isEmpty()) {
      service.sendNotification(list, Constant.BEFORE_ONE_DAY);
    }
  }

  @Scheduled(cron = "${notification.reminder.beforeHour}")
  void excuteReminderJobBeforeOneHour() {
    List<CandidateDTO> list = new ArrayList<>();
    if (!list.isEmpty()) {
      service.sendNotification(list, Constant.BEFORE_ONE_HOUR);
    }
  }

  @Scheduled(cron = "${notification.feedback.afterInterview}")
  void excuteFeedbackJobAfterInterview() {
    List<CandidateDTO> list = new ArrayList<>();
    if (!list.isEmpty()) {
      service.sendNotification(list, Constant.AFTER_INTERVIEW);
    }
  }


}
