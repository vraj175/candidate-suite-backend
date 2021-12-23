package com.aspire.kgp.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.service.WebSocketNotificationService;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  static Log log = LogFactory.getLog(WebSocketConfig.class.getName());


  @Autowired
  WebSocketNotificationService webSocketNotificationService;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/response", "/queue");
    config.setApplicationDestinationPrefixes("/api/v1.0");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/webSocket-notification").withSockJS();
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    log.debug("New Socket Connection....");
    registration.interceptors(new ChannelInterceptor() {

      @Override
      public Message<?> preSend(Message<?> message, MessageChannel channel) {

        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        StompHeaderAccessor accessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand()) && accessor != null) {
          String apiKey = accessor.getFirstNativeHeader(Constant.API_KEY);
          webSocketNotificationService.apiKeyCheck(apiKey);
          String accessToken = accessor.getFirstNativeHeader(Constant.AUTHORIZATION);
          webSocketNotificationService.jwtTokenCheck(accessToken);
          String galaxyId = accessor.getFirstNativeHeader("galaxyId");
          String notificationUserType = accessor.getFirstNativeHeader("notificationUserType");
          String sessionId = headerAccessor.getSessionId();
          accessor.setUser(() -> sessionId);
          log.info("Socket is connected with galaxy Id " + galaxyId + " User Type "
              + notificationUserType);
          webSocketNotificationService.setSocketMap(headerAccessor.getSessionId(),
              galaxyId + "-" + notificationUserType);
        }
        return message;
      }
    });

  }
}
