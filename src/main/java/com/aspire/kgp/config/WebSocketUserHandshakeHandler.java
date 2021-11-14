package com.aspire.kgp.config;


import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.aspire.kgp.service.impl.WebSocketNotificationServiceImpl;

@Component
public class WebSocketUserHandshakeHandler extends DefaultHandshakeHandler {
  static Log log = LogFactory.getLog(WebSocketUserHandshakeHandler.class.getName());

  @Autowired
  WebSocketNotificationServiceImpl webSocketNotificationServiceImpl;

  @Override
  protected Principal determineUser(ServerHttpRequest serverHttpRequest, WebSocketHandler wsHandler,
      Map<String, Object> attributes) {
    log.info("New Socket Connection....");
    HttpHeaders header = serverHttpRequest.getHeaders();
    String socketId = header.get("sec-websocket-key").get(0);
    String type = null;
    String id = null;
    final String sessionId;

    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
    HttpSession session = servletRequest.getServletRequest().getSession();
    sessionId = session.getId();
    log.info("Session Id Connected " + sessionId);
    log.info("Socket Id Connected " + socketId);
    webSocketNotificationServiceImpl.setSocketMap(socketId, id + "-" + type);
    log.info("Socket Connected on SocketId: " + socketId + " for " + id + "-" + type);
    return () -> socketId;
  }
}
