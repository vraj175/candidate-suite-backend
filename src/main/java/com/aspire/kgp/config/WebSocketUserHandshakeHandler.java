package com.aspire.kgp.config;


import java.security.Principal;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class WebSocketUserHandshakeHandler extends DefaultHandshakeHandler {
  static Log log = LogFactory.getLog(WebSocketUserHandshakeHandler.class.getName());


  @Override
  protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
      Map<String, Object> attributes) {
    log.info("Determine prinicipal");

    return null;

  }

}
