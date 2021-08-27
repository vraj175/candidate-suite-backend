package com.aspire.kgp.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.Role;
import com.aspire.kgp.service.RoleService;

class RoleControllerTest {

  @InjectMocks
  RoleController controller;

  @Mock
  RoleService service;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetRoles() {
    List<Role> roles = CustomTestData.getRoles();

    when(service.findAll()).thenReturn(roles);
    
    List<Role> result = controller.getRoles();
    
    assertNotNull(result);
    assertEquals(roles.size(), result.size());
  }

  @Test
  void testInitializeRoles() {
    when(service.initializeData()).thenReturn(Constant.TEST);
    
    String result = controller.initializeRoles();
    
    assertNotNull(result);
    assertEquals(Constant.TEST, result);
  }
}
