package com.aspire.kgp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.model.User;
import com.aspire.kgp.repository.UserRepository;

class UserServiceImplTest {
  
  @InjectMocks
  UserServiceImpl service;
  
  @Mock
  UserRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSaveorUpdate() {
    User user = CustomTestData.getUser();

    when(repository.save(any())).thenReturn(user);

    User result = service.saveorUpdate(user);

    assertNotNull(result);
    assertEquals(user.getId(), result.getId());
    assertEquals(user.getCreatedDate(), result.getCreatedDate());
    assertEquals(user.getModifyDate(), result.getModifyDate());
    assertEquals(user.getGalaxyId(), result.getGalaxyId());
    assertEquals(user.isDeleted(), result.isDeleted());
    assertEquals(user.getEmail(), result.getEmail());
    assertEquals(user.getLastLogin(), result.getLastLogin());
    assertEquals(user.getPassword(), result.getPassword());
    assertEquals(user.isPasswordReset(), result.isPasswordReset());
    assertEquals(user.getLanguage(), result.getLanguage());
    assertEquals(user.getRole(), result.getRole());
  }

}
