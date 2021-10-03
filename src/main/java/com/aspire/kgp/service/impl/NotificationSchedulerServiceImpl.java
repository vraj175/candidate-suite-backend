package com.aspire.kgp.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.aspire.kgp.dto.NotificationSchedulerDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.model.NotificationSchedule;
import com.aspire.kgp.repository.NotificationSchedulerRepository;
import com.aspire.kgp.service.NotificationSchedulerService;


@Service
public class NotificationSchedulerServiceImpl implements NotificationSchedulerService {
  static Log log = LogFactory.getLog(NotificationSchedulerServiceImpl.class.getName());

  private final SimpleDateFormat notificationDateFormatter =
      new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

  @Autowired
  NotificationSchedulerRepository notificationSchedulerRepository;

  @Value("${notificationScheduler.beforeDay}")
  private String beforeDay;

  @Value("${notificationScheduler.lastHour}")
  private String lastHour;

  TaskScheduler scheduler;

  public NotificationSchedulerServiceImpl(TaskScheduler scheduler) {
    this.scheduler = scheduler;
  }

  public boolean setcandidateInterview(NotificationSchedulerDTO notificationDTO) {
    log.info("Set Interview Notification...");
    Runnable task = excuteNotification(notificationDTO.getCandidateId(),
        notificationDTO.getMessage(), notificationDTO.getScheduleId());
    Date scheduledDate = null;
    try {
      scheduledDate = notificationDateFormatter.parse(notificationDTO.getDate());
      candidateInterviewSchedule(notificationDTO, task,
          getScheduleDate(scheduledDate, Calendar.HOUR), "HOUR");
      candidateInterviewSchedule(notificationDTO, task,
          getScheduleDate(scheduledDate, Calendar.DATE), "DAY");
      return true;
    } catch (Exception e) {
      log.info(e);
      throw new APIException("Error in Set Interview Notification");
    }
  }

  private Date getScheduleDate(Date scheduledDate, int hour2) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(scheduledDate);
    if (hour2 == Calendar.HOUR)
      cal.add(Calendar.HOUR, -Integer.parseInt(lastHour));
    else
      cal.add(Calendar.DATE, -Integer.parseInt(beforeDay));
    return cal.getTime();
  }

  private Runnable excuteNotification(String candidateId, String message, String scheduledId) {
    log.info("Excute Notification..");
    return () -> {
      Date currentDate =
          Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
      sendNotification(candidateId, message);
      deleteFromDataBase(scheduledId, currentDate);
    };
  }

  private boolean candidateInterviewSchedule(NotificationSchedulerDTO notificationDTO,
      Runnable task, Date date, String taskType) {
    try {
      scheduler.schedule(task, date);
      NotificationSchedule notificationSchedule = new NotificationSchedule();
      notificationSchedule.setCandidateId(notificationDTO.getCandidateId());
      notificationSchedule.setMessage(notificationDTO.getMessage());
      notificationSchedule.setTaskType(taskType);
      notificationSchedule.setDate(date);
      notificationSchedule.setScheduleId(notificationDTO.getScheduleId());
      notificationSchedulerRepository.save(notificationSchedule);
      log.info("Interview notification scheduled for " + notificationSchedule.getCandidateId()
          + " On " + date);
      log.debug("Interview notification scheduled details: ScheduleId "
          + notificationSchedule.getScheduleId() + " Type " + taskType);
      return true;
    } catch (Exception e) {
      log.error("Error while interview Schedule or save in to database" + e.getMessage());
      return false;
    }



  }

  protected void deleteFromDataBase(String scheduleId, Date currentDate) {
    log.info("Schedule Id: " + scheduleId + " is deleted after excuted on " + currentDate);
    try {
      notificationSchedulerRepository.deleteByScheduleIdAndDate(scheduleId, currentDate);
    } catch (Exception e) {
      log.error("Error While delete scheduler " + e.getMessage());
    }

  }

  private void sendNotification(String candidateId, String message) {
    System.out.println("Hello " + candidateId + " " + message);

  }

  @EventListener({ContextRefreshedEvent.class})
  void contextRefreshedEvent() {
    List<NotificationSchedule> schedulerList = notificationSchedulerRepository.findAll();
    log.info(
        "Server Restart and Total Task: " + schedulerList.size() + " are available for schedule");
    for (NotificationSchedule notificationSchedule : schedulerList) {
      Runnable task = excuteNotification(notificationSchedule.getCandidateId(),
          notificationSchedule.getMessage(), notificationSchedule.getScheduleId());
      scheduler.schedule(task, notificationSchedule.getDate());
      log.info("From database Interview notification scheduled for "
          + notificationSchedule.getCandidateId() + " On " + notificationSchedule.getDate()
          + " And Type: " + notificationSchedule.getTaskType());
    }
  }

}
