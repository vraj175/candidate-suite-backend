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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {
  static Log log = LogFactory.getLog(ApiKeyAuthFilter.class.getName());

  @Value("${spring.api.secret.key}")
  private String apiSecretKey;

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    Map<String, Object> errorDetails = new HashMap<>();
    ObjectMapper objectMapper = new ObjectMapper();
    // StringBuffer currentUrl = request.getRequestURL(); || currentUrl.indexOf("/oauth/token") > -1
    if (request.getRequestURI().indexOf("/api/") > -1) {
      log.info("filter called");
      String apiKey = request.getHeader(Constant.API_KEY);
      if (apiKey == null) {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorDetails.put(Constant.MESSAGE, Constant.BAD_REQUEST);
        objectMapper.writeValue(response.getWriter(), errorDetails);
        return;
      }
      log.info("api key is :: " + CommonUtil.verifyHash(apiSecretKey, apiKey));
      if (!CommonUtil.verifyHash(apiSecretKey, apiKey)) {
        log.info("message :: " + Constant.INVALID_API_KEY);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        errorDetails.put(Constant.MESSAGE, Constant.INVALID_API_KEY);
        objectMapper.writeValue(response.getWriter(), errorDetails);
        return;
      }

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(null, null, null);

      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      log.info("filter end");
    }
    filterChain.doFilter(request, response);
  }
}
