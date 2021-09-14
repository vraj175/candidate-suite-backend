package com.aspire.kgp.security;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;

class OAuthDaoTest {

  @InjectMocks
  OAuthDao oAuthDao;

  @Mock
  UserService service;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetUserDetails() {
    when(service.findByEmail(anyString())).thenReturn(null);

    UserEntity result = oAuthDao.getUserDetails(Constant.TEST);
    assertNull(result);

    User user = CustomTestData.getUser();
    when(service.findByEmail(anyString())).thenReturn(user);

    result = oAuthDao.getUserDetails(Constant.TEST);
    assertNotNull(result);
    assertEquals(user.getEmail(), result.getUsername());
    assertEquals(user.getPassword(), result.getPassword());
    assertEquals(1, result.getGrantedAuthoritiesList().size());
  }

}
