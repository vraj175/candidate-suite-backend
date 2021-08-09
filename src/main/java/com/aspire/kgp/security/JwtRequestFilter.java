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
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.util.CommonUtil;
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
      String password = request.getParameter("password");
      String auth = username + ":" + password;
      try {
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String token = "Basic " + new String(encodedAuth, StandardCharsets.UTF_8);
        AuthenticationResultType authenticationResult =
            restUtil.validateCognitoWithAuthenticationToken(token);

        JsonObject userjson = null;
        String accessToken;
        if (authenticationResult != null) {
          try {
            accessToken = authenticationResult.getAccessToken();

            String authentication = restUtil.getUserDetails(accessToken);
            userjson = new Gson().fromJson(authentication, JsonObject.class);
            log.info(userjson);
          } catch (Exception e) {
            // throw new UnauthorizedAccessException(Constant.INVALID_AUTHENTICATION);
          }
        }
        if (userjson != null && userjson.has("id")) {
          log.info("add or update password");
          service.saveOrUpdatePartner(userjson.get("id").getAsString(), username, password, true);
        }
      } catch (Exception e) {
        log.info("wrong partmer craditionals or it was candidate");
        // exception to save partner
      }
      log.info("end add or update partner");
    } else if (currentUrl.indexOf("/api/") < 0 || currentUrl.indexOf("/initialize") > 0) {
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

      JsonObject userjson = new JsonObject();
      try {
        log.info("AccessToken : " + accessToken);
        request.setAttribute(Constant.ACCESS_TOKEN_X_TOKEN, accessToken);
        String userDetails = restUtil.getUserDetails(accessToken);
        userjson = new Gson().fromJson(userDetails, JsonObject.class);
        log.info(userjson);
      } catch (Exception e) {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorDetails.put(Constant.MESSAGE, Constant.INVALID_AUTHENTICATION);
        objectMapper.writeValue(response.getWriter(), errorDetails);
        return;
      }
      if (userjson.has(Constant.MESSAGE)) {
        log.info("message ::" + userjson.get(Constant.MESSAGE).getAsString());

        // check candidate login
        try {
          OAuth2Authentication authentication = jwtTokenStore.readAuthentication(accessToken);
          log.info(jwtTokenStore.readAccessToken(accessToken).getExpiresIn());
          if (authentication.getUserAuthentication() != null) {
            if (jwtTokenStore.readAccessToken(accessToken).getExpiresIn() <= 0) {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);
              errorDetails.put(Constant.MESSAGE, "Token Expired");
              objectMapper.writeValue(response.getWriter(), errorDetails);
              return;
            }
            String userId = authentication.getUserAuthentication().getName();
            addContactDetails(userId, request, accessToken);
            userjson = null;
          } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            errorDetails.put(Constant.MESSAGE, userjson.get(Constant.MESSAGE).getAsString());
            objectMapper.writeValue(response.getWriter(), errorDetails);
            return;
          }
        } catch (Exception e) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
          errorDetails.put(Constant.MESSAGE, "Malformed Token");
          objectMapper.writeValue(response.getWriter(), errorDetails);
          return;
        }
        // end candidate login
      }
      if (userjson != null) {
        if (userjson.has("id")) {
          User user = service.findByGalaxyId(userjson.get("id").getAsString());
          addPartnerDetails(user, request, userjson, accessToken);
        } else {
          SecurityContextHolder.clearContext();
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
          errorDetails.put(Constant.MESSAGE, Constant.INVALID_AUTHENTICATION);
          objectMapper.writeValue(response.getWriter(), errorDetails);
          return;
        }
      }
    }
    filterChain.doFilter(request, response);
    log.info("filter end");
  }

  private void addPartnerDetails(User user, HttpServletRequest request, JsonObject userjson,
      String accessToken) {
    if (user == null) {
      String email = userjson.get("email").getAsString();
      user = service.saveOrUpdatePartner(userjson.get("id").getAsString(), email,
          CommonUtil.hash(email), false);
    }
    request.setAttribute("user", user);
    UserAuthenticationDTO userAuthenticationDTO = new UserAuthenticationDTO(
        userjson.get("id").getAsString(), userjson.get("name").getAsString(), accessToken, null);

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userAuthenticationDTO, null, null);

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private void addContactDetails(String email, HttpServletRequest request, String accessToken) {
    User user = service.findByEmail(email);
    UserDTO userDTO = service.getContactDetails(user.getGalaxyId());
    if (userDTO == null) {
      throw new APIException("Invalid Contact");
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
