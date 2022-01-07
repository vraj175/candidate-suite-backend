package com.aspire.kgp.service.impl;

import java.lang.reflect.Type;

import com.aspire.kgp.model.WebSocketNotification;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class WebSocketAdpter implements JsonSerializer<WebSocketNotification> {

  @Override
  public JsonElement serialize(WebSocketNotification webSocketNotification, Type typeOfSrc,
      JsonSerializationContext context) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", webSocketNotification.getId());
    jsonObject.addProperty("contactId", webSocketNotification.getContactId());
    jsonObject.addProperty("notificationType", webSocketNotification.getNotificationType());
    jsonObject.addProperty("notificationUserType", webSocketNotification.getNotificationUserType());
    jsonObject.addProperty("isReadable", webSocketNotification.isReadable());
    jsonObject.addProperty("date", webSocketNotification.getDate().toString());
    jsonObject.addProperty("user", webSocketNotification.getUser().toString());
    return jsonObject;
  }
}
