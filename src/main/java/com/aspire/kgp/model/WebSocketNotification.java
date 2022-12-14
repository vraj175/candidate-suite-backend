package com.aspire.kgp.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "socket_notification")
public class WebSocketNotification extends SuperBase {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user", referencedColumnName = "id", insertable = true, nullable = false,
      updatable = true)
  private User user;

  @Column(name = "date", nullable = false)
  private Timestamp date;

  @Column(name = "notificationType", nullable = false, length = 10000)
  private String notificationType;

  @Column(name = "contactId", nullable = false)
  private String contactId;

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  @Column(name = "contactName", nullable = false)
  private String contactName;

  @Column(name = "notificationUserType", nullable = false)
  private String notificationUserType;

  private boolean isReadable;

  public String getNotificationType() {
    return notificationType;
  }

  public void setNotificationType(String notificationType) {
    this.notificationType = notificationType;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Timestamp getDate() {
    return date;
  }

  public void setDate(Timestamp date) {
    this.date = date;
  }

  public boolean isReadable() {
    return isReadable;
  }

  public void setReadable(boolean isReadable) {
    this.isReadable = isReadable;
  }

  public String getContactId() {
    return contactId;
  }

  public void setContactId(String contactId) {
    this.contactId = contactId;
  }

  public String getNotificationUserType() {
    return notificationUserType;
  }

  public void setNotificationUserType(String notificationUserType) {
    this.notificationUserType = notificationUserType;
  }

  @Override
  public String toString() {
    return "WebSocketNotification [user=" + user + ", date=" + date + ", notificationType="
        + notificationType + ", contactId=" + contactId + ", notificationUserType="
        + notificationUserType + ", isReadable=" + isReadable + "]";
  }
}
