package com.aspire.kgp.dto;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("webSocketNotificationFilter")
public class WebSocketNotificationDTO {

  private String contactId;
  private String notificationType;

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

  @Override
  public String toString() {
    return "WebSocketNotificationDTO [contactId=" + contactId + ", notificationType="
        + notificationType + "]";
  }
}
