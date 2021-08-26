package com.aspire.kgp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
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
  void testFindById() {
    Optional<User> user = Optional.ofNullable(CustomTestData.getUser());

    when(repository.findById(anyLong())).thenReturn(user);

    User result = service.findById(Long.MIN_VALUE);

    assertNotNull(result);
    assertEquals(user.get().getId(), result.getId());
    assertEquals(user.get().getCreatedDate(), result.getCreatedDate());
    assertEquals(user.get().getModifyDate(), result.getModifyDate());
    assertEquals(user.get().getGalaxyId(), result.getGalaxyId());
    assertEquals(user.get().isDeleted(), result.isDeleted());
    assertEquals(user.get().getEmail(), result.getEmail());
    assertEquals(user.get().getLastLogin(), result.getLastLogin());
    assertEquals(user.get().getPassword(), result.getPassword());
    assertEquals(user.get().isPasswordReset(), result.isPasswordReset());
    assertEquals(user.get().getLanguage(), result.getLanguage());
    assertEquals(user.get().getRole(), result.getRole());
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
  
  @Test
  void testFindByGalaxyId() {
    User user = CustomTestData.getUser();

    when(repository.findByGalaxyId(anyString())).thenReturn(user);

    User result = service.findByGalaxyId(Constant.TEST);

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
  
  @Test
  void testFindByEmail() {
    User user = CustomTestData.getUser();

    when(repository.findByEmailAndIsDeletedFalse(anyString())).thenReturn(user);

    User result = service.findByEmail(Constant.TEST);

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
