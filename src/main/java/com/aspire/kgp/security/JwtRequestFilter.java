package com.aspire.kgp.security;

import java.io.IOException;
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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.UserAuthenticationDTO;
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
  UserService service;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("filter start");
    StringBuffer currentUrl = request.getRequestURL();
    if (currentUrl.indexOf("/user/authenticate") > 0 || currentUrl.indexOf("/api/") < 0) {
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
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorDetails.put(Constant.MESSAGE, userjson.get(Constant.MESSAGE).getAsString());
        objectMapper.writeValue(response.getWriter(), errorDetails);
        return;
      }
      if (userjson.has("id")) {
        User user = service.findByGalaxyId(userjson.get("id").getAsString());
        addDetails(user, request, userjson, accessToken);
      } else {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorDetails.put(Constant.MESSAGE, Constant.INVALID_AUTHENTICATION);
        objectMapper.writeValue(response.getWriter(), errorDetails);
        return;
      }
    }
    filterChain.doFilter(request, response);
    log.info("filter end");
  }

  private void addDetails(User user, HttpServletRequest request, JsonObject userjson,
      String accessToken) {
    if (user == null) {
      user = service.savePartner(userjson.get("id").getAsString(),
          userjson.get("email").getAsString());
    }
    request.setAttribute("user", user);
    request.setAttribute("userJson", userjson);
    log.info("session details : " + (User) request.getAttribute("user"));
    UserAuthenticationDTO userAuthenticationDTO = new UserAuthenticationDTO(
        userjson.get("id").getAsString(), userjson.get("name").getAsString(), accessToken, null);

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(userAuthenticationDTO, null, null);

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

}
