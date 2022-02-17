package com.aspire.kgp.config;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.service.InterviewNotificationService;


/*
 * Scheduler call on Specific time which is available in properties file
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
public class NotificationSchedulerConfig {
  static Log log = LogFactory.getLog(NotificationSchedulerConfig.class.getName());

  @Autowired
  InterviewNotificationService service;

  @Scheduled(cron = "${notification.reminder.beforeDay}")
  void excuteReminderJobBeforeOneDay() {
    log.info("Scheduler call for sending reminder mail before Day");
    service.sendNotification(Constant.BEFORE_ONE_DAY);
  }

  @Scheduled(cron = "${notification.reminder.beforeHour}")
  void excuteReminderJobBeforeOneHour() {
    log.info("Scheduler call for sending reminder mail before Hour");
    service.sendNotification(Constant.BEFORE_ONE_HOUR);
  }

  @Scheduled(cron = "${notification.feedback.afterInterview}")
  void excuteFeedbackJobAfterInterview() {
    log.info("Scheduler call for sending feedback mail after interview");
    service.sendNotification(Constant.AFTER_INTERVIEW);
  }
}
