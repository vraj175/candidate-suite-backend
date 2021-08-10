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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.UserAuthenticationDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.MissingAuthTokenException;
import com.aspire.kgp.exception.UnauthorizedAccessException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;
import com.aspire.kgp.util.CommonUtil;
import com.aspire.kgp.util.RestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
  static Log log = LogFactory.getLog(JwtRequestFilter.class.getName());

  @Autowired
  RestUtil restUtil;

  @Autowired
  private JwtTokenStore jwtTokenStore;

  @Autowired
  UserService service;

  @Value("${spring.api.secret.key}")
  private String apiSecretKey;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("filter start");
    StringBuffer currentUrl = request.getRequestURL();
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> errorDetails = new HashMap<>();
    String apiKey = request.getHeader(Constant.API_KEY);
    if (currentUrl.indexOf("/oauth/token") > 0) {
      log.info("start add or update partner");
      try {
        apiKeyCheck(apiKey);
      } catch (MissingAuthTokenException e) {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorDetails.put(Constant.MESSAGE, Constant.BAD_REQUEST);
        objectMapper.writeValue(response.getWriter(), errorDetails);
        return;
      } catch (UnauthorizedAccessException e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorDetails.put(Constant.MESSAGE, Constant.INVALID_API_KEY);
        objectMapper.writeValue(response.getWriter(), errorDetails);
        return;
      }

      String username = request.getParameter("username");
      String password = request.getParameter("password");
      service.saveOrUpdatePartner(username, password);
      log.info("end add or update partner");
    } else if (currentUrl.indexOf("/api/") < 0) {
      log.info("not need to authorize ");
    } else {

      try {
        apiKeyCheck(apiKey);
      } catch (MissingAuthTokenException e) {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorDetails.put(Constant.MESSAGE, Constant.BAD_REQUEST);
        objectMapper.writeValue(response.getWriter(), errorDetails);
        return;
      } catch (UnauthorizedAccessException e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorDetails.put(Constant.MESSAGE, Constant.INVALID_API_KEY);
        objectMapper.writeValue(response.getWriter(), errorDetails);
        return;
      }

      if (currentUrl.indexOf("/initialize") > 0 || currentUrl.indexOf("/user/invite") > 0) {
        log.info("no need to verify JWT token ");
      } else {
        log.info("authorize token " + currentUrl);
        String accessToken = request.getHeader(Constant.AUTHORIZATION);
        log.info("accessToken:" + accessToken);

        try {
          jwtTokenCheck(accessToken, request);
        } catch (MissingAuthTokenException e) {
          SecurityContextHolder.clearContext();
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
          errorDetails.put(Constant.MESSAGE, Constant.BAD_REQUEST);
          objectMapper.writeValue(response.getWriter(), errorDetails);
          return;
        } catch (UnauthorizedAccessException e) {
          SecurityContextHolder.clearContext();
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
          errorDetails.put(Constant.MESSAGE, e.getMessage());
          objectMapper.writeValue(response.getWriter(), errorDetails);
          return;
        }
      }

    }
    filterChain.doFilter(request, response);
    log.info("filter end");
  }

  private void apiKeyCheck(String apiKey)
      throws UnauthorizedAccessException, MissingAuthTokenException {
    if (apiKey == null) {
      throw new MissingAuthTokenException(Constant.BAD_REQUEST);
    }
    if (!CommonUtil.verifyHash(apiSecretKey, apiKey)) {
      throw new UnauthorizedAccessException(Constant.INVALID_API_KEY);
    }
  }

  private void jwtTokenCheck(String accessToken, HttpServletRequest request)
      throws UnauthorizedAccessException, MissingAuthTokenException {
    if (accessToken == null) {
      throw new MissingAuthTokenException(Constant.BAD_REQUEST);
    }
    if (!accessToken.startsWith("Bearer ")) {
      throw new UnauthorizedAccessException(Constant.INVALID_AUTHENTICATION);
    }
    accessToken = accessToken.split(" ")[1].trim();
    log.info("accessToken:" + accessToken);
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
      addContactDetails(userId, request, accessToken);
    } catch (UnauthorizedAccessException e) {
      throw new UnauthorizedAccessException(e.getMessage());
    } catch (Exception e) {
      throw new UnauthorizedAccessException("Malformed Token");
    }
  }

  private void addContactDetails(String email, HttpServletRequest request, String accessToken) {
    User user = service.findByEmail(email);
    UserDTO userDTO = null;
    if (user.getRole().getName().equalsIgnoreCase(Constant.PARTNER)) {
      userDTO = service.getGalaxyUserDetails(user.getGalaxyId());
    } else {
      userDTO = service.getContactDetails(user.getGalaxyId());
    }
    if (userDTO.getFirstName() == null) {
      throw new UnauthorizedAccessException("Invalid User or User Delete from Galaxy");
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
