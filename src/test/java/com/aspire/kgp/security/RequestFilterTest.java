package com.aspire.kgp.security;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.test.util.ReflectionTestUtils;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.UserDTO;
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
  private JwtTokenStore jwtTokenStore;

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

    request.removeHeader(Constant.API_KEY);
    request.addHeader(Constant.API_KEY,
        "$2a$10$f3GPt8/.hwBGRNOUzxuHLe5/MfSIrK5L/tubjn2tSq33gxtPklZLu");
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);

    request.addHeader(Constant.AUTHORIZATION, Constant.TEST);
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);

    UserDTO userDTO = CustomTestData.getUserDTO();
    when(service.getContactDetails(anyString())).thenReturn(userDTO);
    when(service.findByEmail(anyString())).thenReturn(user);
    request.removeHeader(Constant.AUTHORIZATION);
    request.addHeader(Constant.AUTHORIZATION, "Bearer " + Constant.TEST);
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);
    
    OAuth2Request storedRequest = new OAuth2Request(new HashMap<>(), Constant.TEST,
        CustomTestData.getUserEntity().getGrantedAuthoritiesList(), Boolean.TRUE, new HashSet<>(),
        new HashSet<>(), Constant.TEST, new HashSet<>(), new HashMap<>());
    OAuth2Authentication authentication =
        new OAuth2Authentication(storedRequest, null);
    when(jwtTokenStore.readAuthentication(anyString())).thenReturn(authentication);
    filter.doFilterInternal(request, response, filterChain);
    verify(service, times(1)).saveOrUpdatePartner(Constant.TEST, Constant.TEST);

    Authentication userAuthentication = new AbstractAuthenticationToken(
        CustomTestData.getUserEntity().getGrantedAuthoritiesList()) {

      @Override
      public Object getPrincipal() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Object getCredentials() {
        // TODO Auto-generated method stub
        return null;
      }
    };
  }

}
