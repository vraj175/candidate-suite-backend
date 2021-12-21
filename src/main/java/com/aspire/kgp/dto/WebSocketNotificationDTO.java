package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("webSocketNotificationFilter")
public class WebSocketNotificationDTO {
  private String id;
  private String contactId;
  private String notificationType;
  private String notificationUserType;

  public String getContactId() {
    return contactId;
  }

  public void setContactId(String contactId) {
    this.contactId = contactId;
  }

  public String getNotificationType() {
    return notificationType;
  }

  public void setNotificationType(String notificationType) {
    this.notificationType = notificationType;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNotificationUserType() {
    return notificationUserType;
  }

  public void setNotificationUserType(String notificationUserType) {
    this.notificationUserType = notificationUserType;
  }

  @Override
  public String toString() {
    return "WebSocketNotificationDTO [id=" + id + ", contactId=" + contactId + ", notificationType="
        + notificationType + ", notificationUserType=" + notificationUserType + "]";
  }
}
