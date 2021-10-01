package com.aspire.kgp.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.aspire.kgp.dto.NotificationSchedulerDTO;
import com.aspire.kgp.model.NotificationSchedule;
import com.aspire.kgp.repository.NotificationSchedulerRepository;
import com.aspire.kgp.service.NotificationSchedulerService;


@Service
public class NotificationSchedulerServiceImpl implements NotificationSchedulerService {
  private final SimpleDateFormat notificationDateFormatter =
      new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

  @Autowired
  NotificationSchedulerRepository notificationSchedulerRepository;

  @Value("${notificationScheduler.beforeDay}")
  private String day;

  @Value("${notificationScheduler.hour}")
  private String hour;

  TaskScheduler scheduler;

  public NotificationSchedulerServiceImpl(TaskScheduler scheduler) {
    this.scheduler = scheduler;
  }

  public boolean setcandidateInterview(NotificationSchedulerDTO notificationDTO) {
    Runnable task = createRunnable(notificationDTO.getCandidateId(), notificationDTO.getMessage(),
        notificationDTO.getScheduleId());
    Date scheduledDate = null;
    try {
      scheduledDate = notificationDateFormatter.parse(notificationDTO.getDate());
    } catch (ParseException e) {
      e.printStackTrace();
    }

    candidateInterviewSchedule(notificationDTO, task, getScheduleDate(scheduledDate, Calendar.HOUR),
        "HOUR");
    candidateInterviewSchedule(notificationDTO, task, getScheduleDate(scheduledDate, Calendar.DATE),
        "DAY");
    return true;
  }

  private Date getScheduleDate(Date scheduledDate, int hour2) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(scheduledDate);
    if (hour2 == Calendar.HOUR)
      cal.add(Calendar.HOUR, -Integer.parseInt(hour));
    else
      cal.add(Calendar.DATE, -Integer.parseInt(day));
    return cal.getTime();
  }

  private Runnable createRunnable(String candidateId, String message, String scheduledId) {
    return () -> {
      Date currentDate =
          Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
      sendNotification(candidateId, message);
      deleteFromDataBase(scheduledId, currentDate);
    };
  }

  private boolean candidateInterviewSchedule(NotificationSchedulerDTO notificationDTO, Runnable task,
      Date date, String taskType) {
    scheduler.schedule(task, date);
    NotificationSchedule notificationSchedule = new NotificationSchedule();
    notificationSchedule.setCandidateId(notificationDTO.getCandidateId());
    notificationSchedule.setMessage(notificationDTO.getMessage());
    notificationSchedule.setTaskType(taskType);
    notificationSchedule.setDate(date);
    notificationSchedule.setScheduleId(notificationDTO.getScheduleId());
    notificationSchedulerRepository.save(notificationSchedule);
    return true;
  }

  protected void deleteFromDataBase(String scheduleId, Date currentDate) {
    notificationSchedulerRepository.deleteByScheduleIdAndDate(scheduleId, currentDate);
  }

  private void sendNotification(String candidateId, String message) {
    System.out.println("Hello " + candidateId + " " + message);

  }

  @EventListener({ContextRefreshedEvent.class})
  void contextRefreshedEvent() {
    List<NotificationSchedule> schedulerList = notificationSchedulerRepository.findAll();
    for (NotificationSchedule notificationSchedule : schedulerList) {
      Runnable task = createRunnable(notificationSchedule.getCandidateId(),
          notificationSchedule.getMessage(), notificationSchedule.getScheduleId());
      scheduler.schedule(task, notificationSchedule.getDate());
    }
  }

}
