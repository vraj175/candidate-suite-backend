package com.aspire.kgp.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.dto.InviteDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;

class UserControllerTest {

  @InjectMocks
  UserController controller;

  @Mock
  UserService service;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

   @Test
   void testInviteUser() {
     MockHttpServletRequest request = CustomTestData.getRequest();

     when(service.findByGalaxyId(anyString())).thenReturn(null);
     UserDTO userDTO = CustomTestData.getUserDTO();
     when(service.getGalaxyUserDetails(anyString())).thenReturn(userDTO);

     User user = CustomTestData.getUser();
     when(service.saveOrUpdatePartner(anyString(), anyString(), anyString(), anyBoolean()))
         .thenReturn(user);

     when(service.inviteUser(anyString(), anyString(), anyString(), any(), any(), anyBoolean(),
         any())).thenReturn(Boolean.TRUE);

     InviteDTO inviteDTO = CustomTestData.getInviteDTO();
     ResponseEntity<Object> entity= controller.inviteUser(inviteDTO, request);
     
     assertNotNull(entity);
     
     when(service.findByGalaxyId(anyString())).thenReturn(user);
     entity= controller.inviteUser(inviteDTO, request);
     
     assertNotNull(entity);
   }

  @Test
  void testInviteUser_NotFoundException() {
    MockHttpServletRequest request = CustomTestData.getRequest();

    when(service.findByGalaxyId(anyString())).thenReturn(null);
    UserDTO userDTO = new UserDTO();
    when(service.getGalaxyUserDetails(anyString())).thenReturn(userDTO);

    InviteDTO inviteDTO = CustomTestData.getInviteDTO();

    Exception e =
        assertThrows(NotFoundException.class, () -> controller.inviteUser(inviteDTO, request));
    assertEquals("Invalid Partner Id", e.getMessage());
  }

  @Test
  void testInviteUser_APIException() {
    MockHttpServletRequest request = CustomTestData.getRequest();

    when(service.findByGalaxyId(anyString())).thenReturn(null);
    UserDTO userDTO = CustomTestData.getUserDTO();
    when(service.getGalaxyUserDetails(anyString())).thenReturn(userDTO);

    User user = CustomTestData.getUser();
    when(service.saveOrUpdatePartner(anyString(), anyString(), anyString(), anyBoolean()))
        .thenReturn(user);

    when(service.inviteUser(anyString(), anyString(), anyString(), any(), any(), anyBoolean(),
        any())).thenReturn(Boolean.FALSE);

    InviteDTO inviteDTO = CustomTestData.getInviteDTO();

    Exception e =
        assertThrows(APIException.class, () -> controller.inviteUser(inviteDTO, request));
    assertEquals("Error in send invite", e.getMessage());
  }

}