package com.aspire.kgp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.Role;
import com.aspire.kgp.repository.RoleRepository;

class RoleServiceImplTest {

  @InjectMocks
  RoleServiceImpl service;

  @Mock
  RoleRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSaveorUpdate() {
    Role role = CustomTestData.getRole();

    when(repository.save(any())).thenReturn(role);

    Role result = service.saveorUpdate(role);

    assertNotNull(result);
    assertEquals(role.getId(), result.getId());
    assertEquals(role.getName(), result.getName());
    assertEquals(role.getCreatedDate(), result.getCreatedDate());
    assertEquals(role.getModifyDate(), result.getModifyDate());
  }

  @Test
  void testSaveAll() {
    List<Role> roles = CustomTestData.getRoles();

    when(repository.saveAll(any())).thenReturn(roles);

    List<Role> result = service.saveAll(roles);

    assertNotNull(result);
    assertEquals(roles.size(), result.size());
  }

  @Test
  void testFindAll() {
    List<Role> roles = CustomTestData.getRoles();

    when(repository.findAll()).thenReturn(roles);

    List<Role> result = service.findAll();

    assertNotNull(result);
    assertEquals(roles.size(), result.size());
  }

  @Test
  void testFindByName() {
    Role role = CustomTestData.getRole();

    when(repository.findByName(anyString())).thenReturn(role);

    Role result = service.findByName(Constant.TEST);

    assertNotNull(result);
    assertEquals(role.getId(), result.getId());
    assertEquals(role.getName(), result.getName());
    assertEquals(role.getCreatedDate(), result.getCreatedDate());
    assertEquals(role.getModifyDate(), result.getModifyDate());
  }

  @Test
  void testInitializeData() {
    List<Role> roles = CustomTestData.getRoles();

    when(repository.findAll()).thenReturn(roles);

    String result = service.initializeData();

    assertNotNull(result);
    assertEquals(Constant.DATA_ALREADY_INITIALIZED, result);

    when(repository.findAll()).thenReturn(new ArrayList<>());

    result = service.initializeData();

    assertNotNull(result);
    assertEquals(Constant.DATA_SAVED, result);
  }
}
