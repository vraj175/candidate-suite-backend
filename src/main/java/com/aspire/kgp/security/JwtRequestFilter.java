package com.aspire.kgp.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.UserAuthenticationDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.UnauthorizedAccessException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.util.RestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
  static Log log = LogFactory.getLog(JwtRequestFilter.class.getName());

  @Autowired
  RestUtil restUtil;

  @Autowired
  private JwtTokenStore jwtTokenStore;

  @Autowired
  UserService service;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("filter start");
    StringBuffer currentUrl = request.getRequestURL();
    if (currentUrl.indexOf("/oauth/token") > 0) {
      log.info("start add or update partner");
      String username = request.getParameter("username");
      User user = service.findByEmail(username);
      if (user == null || user.getRole().getName().equalsIgnoreCase(Constant.PARTNER)) {
        String password = request.getParameter("password");
        String auth = username + ":" + password;
        try {
          byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
          String token = "Basic " + new String(encodedAuth, StandardCharsets.UTF_8);
          AuthenticationResultType authenticationResult =
              restUtil.validateCognitoWithAuthenticationToken(token);

          String accessToken = authenticationResult.getAccessToken();
          String authentication = restUtil.getUserDetails(accessToken);
          JsonObject userjson = new Gson().fromJson(authentication, JsonObject.class);
          log.info("add or update password");
          service.saveOrUpdatePartner(userjson.get("id").getAsString(), username, password, true);
        } catch (Exception e) {
          log.info("wrong partner craditionals or it was candidate");
        }
      }
      log.info("end add or update partner");
    } else if (currentUrl.indexOf("/api/") < 0 || currentUrl.indexOf("/initialize") > 0
        || currentUrl.indexOf("/user/invite") > 0) {
      log.info("not need to authorize ");
    } else {
      log.info("authorize token " + currentUrl);
      String accessToken = request.getHeader(Constant.AUTHORIZATION);
      ObjectMapper objectMapper = new ObjectMapper();
      log.info("accessToken:" + accessToken);
      Map<String, Object> errorDetails = new HashMap<>();
      if (accessToken == null) {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorDetails.put(Constant.MESSAGE, Constant.BAD_REQUEST);
        objectMapper.writeValue(response.getWriter(), errorDetails);
        return;
      }
      if (!accessToken.startsWith("Bearer ")) {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorDetails.put(Constant.MESSAGE, Constant.INVALID_AUTHENTICATION);
        objectMapper.writeValue(response.getWriter(), errorDetails);
        return;
      }

      accessToken = accessToken.split(" ")[1].trim();
      // check candidate login
      try {
        request.setAttribute(Constant.ACCESS_TOKEN_X_TOKEN, accessToken);
        OAuth2Authentication authentication = jwtTokenStore.readAuthentication(accessToken);
        log.info(jwtTokenStore.readAccessToken(accessToken).getExpiresIn());
        if (authentication.getUserAuthentication() == null) {
          throw new UnauthorizedAccessException("Malformed Token");
        }
        if (jwtTokenStore.readAccessToken(accessToken).getExpiresIn() <= 0) {
          throw new UnauthorizedAccessException("Token Expired");
        }
        String userId = authentication.getUserAuthentication().getName();
        addContactDetails(userId, request, accessToken);
      } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorDetails.put(Constant.MESSAGE, e.getMessage());
        objectMapper.writeValue(response.getWriter(), errorDetails);
        return;
      }

    }
    filterChain.doFilter(request, response);
    log.info("filter end");
  }

  private void addContactDetails(String email, HttpServletRequest request, String accessToken) {
    User user = service.findByEmail(email);
    UserDTO userDTO = null;
    if(user.getRole().getName().equalsIgnoreCase(Constant.PARTNER)) {
      userDTO = service.getGalaxyUserDetails(user.getGalaxyId());
    }else {
      userDTO = service.getContactDetails(user.getGalaxyId());
    }
    if (userDTO.getFirstName() == null) {
      throw new UnauthorizedAccessException("Invalid User or Contact Delete from Galaxy");
    }
    request.setAttribute("user", user);
    UserAuthenticationDTO userAuthenticationDTO =
        new UserAuthenticationDTO(String.valueOf(user.getId()),
            userDTO.getFirstName() + " " + userDTO.getLastName(), accessToken, null);

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userAuthenticationDTO, null, null);

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

}
