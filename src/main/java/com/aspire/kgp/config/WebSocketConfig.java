package com.aspire.kgp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.MissingAuthTokenException;
import com.aspire.kgp.exception.UnauthorizedAccessException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.service.WebSocketNotificationService;
import com.aspire.kgp.util.CommonUtil;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Value("${spring.api.secret.key}")
  private String apiSecretKey;

  @Autowired
  UserService service;

  @Autowired
  private JwtTokenStore jwtTokenStore;

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
    registration.interceptors(new ChannelInterceptor() {

      @Override
      public Message<?> preSend(Message<?> message, MessageChannel channel) {

        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        StompHeaderAccessor accessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand()) && accessor != null) {
          String apiKey = accessor.getFirstNativeHeader(Constant.API_KEY);

          apiKeyCheck(apiKey);
          String accessToken = accessor.getFirstNativeHeader(Constant.AUTHORIZATION);
          jwtTokenCheck(accessToken);
          String galaxyId = accessor.getFirstNativeHeader("galaxyId");
          String notificationUserType = accessor.getFirstNativeHeader("notificationUserType");
          String sessionId = headerAccessor.getSessionId();
          accessor.setUser(() -> sessionId);
          System.out.println("Add in to map id " + galaxyId + " user type " + notificationUserType);
          webSocketNotificationService.setSocketMap(headerAccessor.getSessionId(),
              galaxyId + "-" + notificationUserType);
        }
        return message;
      }


      private void apiKeyCheck(String apiKey)
          throws UnauthorizedAccessException, MissingAuthTokenException {
        if (apiKey == null) {
          throw new MissingAuthTokenException(Constant.MISSING_API_KEY);
        }
        if (!CommonUtil.verifyHash(apiSecretKey, apiKey)) {
          throw new UnauthorizedAccessException(Constant.INVALID_API_KEY);
        }
      }

      private void jwtTokenCheck(String accessToken)
          throws UnauthorizedAccessException, MissingAuthTokenException {
        if (accessToken == null) {
          throw new MissingAuthTokenException(Constant.MISSING_REQUEST_HEADER);
        }
        if (!accessToken.startsWith("Bearer ")) {
          throw new UnauthorizedAccessException(Constant.INVALID_AUTHENTICATION);
        }
        accessToken = accessToken.split(" ")[1].trim();

        try {
          OAuth2Authentication authentication = jwtTokenStore.readAuthentication(accessToken);
          if (authentication.getUserAuthentication() == null) {
            throw new UnauthorizedAccessException("Malformed Token");
          } else {
            if (jwtTokenStore.readAccessToken(accessToken).getExpiresIn() <= 0) {
              throw new UnauthorizedAccessException("Token Expired");
            }
          }
          String userId = authentication.getUserAuthentication().getName();
          User user = service.findByEmail(userId);
          UserDTO userDTO = null;
          if (user.getRole().getName().equalsIgnoreCase(Constant.PARTNER)) {
            userDTO = service.getGalaxyUserDetails(user.getGalaxyId());
          } else {
            userDTO = service.getContactDetails(user.getGalaxyId());
          }
          if (userDTO.getFirstName() == null) {
            throw new UnauthorizedAccessException("Invalid User or User Delete from Galaxy");
          }
        } catch (UnauthorizedAccessException e) {
          throw new UnauthorizedAccessException(e.getMessage());
        } catch (Exception e) {
          throw new UnauthorizedAccessException("Malformed Token");
        }
      }

    });

  }
}
