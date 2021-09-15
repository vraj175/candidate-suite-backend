package com.aspire.kgp.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.exception.MissingAuthTokenException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;

class RequestFilterTest {

  @InjectMocks
  RequestFilter filter;

  @Mock
  FilterChain filterChain;

  @Mock
  UserService service;

  @Mock
  MockHttpServletRequest request;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testDoFilterInternal() throws ServletException, IOException {
    MockHttpServletResponse response = new MockHttpServletResponse();
    when(request.getRequestURL()).thenReturn(new StringBuffer("/api/v1.0/oauth/token"));
    User user = CustomTestData.getUser();
    when(service.saveOrUpdatePartner(anyString(), anyString())).thenReturn(user);
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(null, null);

    when(request.getRequestURL()).thenReturn(new StringBuffer("/bpi/"));
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(null, null);
    
    when(request.getRequestURL()).thenReturn(new StringBuffer("/api/initialize"));
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(null, null);
    
    when(request.getRequestURL()).thenReturn(new StringBuffer("/api/user/invite"));
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(null, null);
    
    when(request.getRequestURL()).thenReturn(new StringBuffer("/api/user/forgotPassword"));
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(null, null);
    
    when(request.getRequestURL()).thenReturn(new StringBuffer("/api/user/resetPassword"));
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(null, null);
    
    when(request.getRequestURL()).thenReturn(new StringBuffer("/api/user/verify/recaptcha"));
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(null, null);
  }

}
