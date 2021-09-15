package com.aspire.kgp.security;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;

class RequestFilterTest {

  @InjectMocks
  RequestFilter filter;

  @Mock
  FilterChain filterChain;

  @Mock
  UserService service;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testDoFilterInternal() throws ServletException, IOException {
    MockHttpServletRequest request = CustomTestData.getRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.setParameter("username", Constant.TEST);
    request.setParameter("password", Constant.TEST);
    User user = CustomTestData.getUser();
    when(service.saveOrUpdatePartner(anyString(), anyString())).thenReturn(user);
    request.setRequestURI("/api/v1.0/oauth/token");
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);

    request.setRequestURI("/bpi/");
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);

    request.setRequestURI("/api/initialize");
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);

    request.setRequestURI("/api/user/invite");
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);

    request.setRequestURI("/api/user/forgotPassword");
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);

    request.setRequestURI("/api/user/resetPassword");
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);

    request.setRequestURI("/api/user/verify/recaptcha");
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);

    ReflectionTestUtils.setField(filter, "apiSecretKey", "kgpc@nd!d@t3su!t3u@t");
    request.addHeader(Constant.API_KEY,
        "$2a$10$f3GPt8/.hwBGRNOUzxuHLe5/MfSIrK5L/tubjn2tSq33gxtPklZw");
    request.setRequestURI("/api/user/profile");
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);

    request.addHeader(Constant.API_KEY,
        "$2a$10$f3GPt8/.hwBGRNOUzxuHLe5/MfSIrK5L/tubjn2tSq33gxtPklZu");
    request.addHeader(Constant.AUTHORIZATION, "Bearer " + Constant.TEST);
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);
  }

}
