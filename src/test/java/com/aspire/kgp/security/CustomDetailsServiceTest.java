package com.aspire.kgp.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.exception.UnauthorizedAccessException;
import com.aspire.kgp.exception.ValidateException;

class CustomDetailsServiceTest {

  @InjectMocks
  CustomDetailsService service;

  @Mock
  OAuthDao oauthDao;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testLoadUserByUsername() {
    UserEntity userEntity = CustomTestData.getUserEntity();
    when(oauthDao.getUserDetails(anyString())).thenReturn(userEntity);

    CustomUser result = service.loadUserByUsername(Constant.TEST);

    assertNotNull(result);
    assertEquals(new CustomUser(userEntity), result);
  }

  @Test
  void testLoadUserByUsername_UnauthorizedAccessException() {
    when(oauthDao.getUserDetails(anyString())).thenReturn(null);

    Exception e =
        assertThrows(UnauthorizedAccessException.class, () -> service.loadUserByUsername(Constant.TEST));
    assertEquals(Constant.INVALID_AUTHENTICATION, e.getMessage());
  }

}
