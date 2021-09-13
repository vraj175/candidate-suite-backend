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
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.mock.web.MockHttpServletRequest;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.InviteDTO;
import com.aspire.kgp.dto.UserDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
    ResponseEntity<Object> entity = controller.inviteUser(inviteDTO, request);

    assertNotNull(entity);

    when(service.findByGalaxyId(anyString())).thenReturn(user);
    entity = controller.inviteUser(inviteDTO, request);

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

    Exception e = assertThrows(APIException.class, () -> controller.inviteUser(inviteDTO, request));
    assertEquals("Error in send invite", e.getMessage());
  }

  @Test
  void testGetUserProfile() throws JsonMappingException, JsonProcessingException {
    MockHttpServletRequest request = CustomTestData.getRequest();
    User user = CustomTestData.getUser();
    request.setAttribute("user", user);

    UserDTO userDTO = CustomTestData.getUserDTO();
    when(service.getContactDetails(anyString())).thenReturn(userDTO);
    MappingJacksonValue mapping = controller.getUserProfile(request);
    assertNotNull(mapping);

    user.getRole().setName(Constant.PARTNER);
    when(service.getGalaxyUserDetails(anyString())).thenReturn(userDTO);
    mapping = controller.getUserProfile(request);

    UserDTO response = (UserDTO) mapping.getValue();
    assertNotNull(response);
    assertEquals(userDTO.getName(), response.getName());
    assertEquals(userDTO.getMobilePhone(), response.getMobilePhone());
    assertEquals(userDTO.getWorkPhone(), response.getWorkPhone());
    assertEquals(userDTO.getWorkEmail(), response.getWorkEmail());
    assertEquals(userDTO.getRole(), response.getRole());
    assertEquals(userDTO.getToken(), response.getToken());
    assertEquals(userDTO.getTitle(), response.getTitle());
    assertEquals(userDTO.getCountry(), response.getCountry());
    assertEquals(userDTO.getLinkedinUrl(), response.getLinkedinUrl());
    assertEquals(userDTO.getBio(), response.getBio());
    assertEquals(userDTO.isPasswordReset(), response.isPasswordReset());
  }
  
  @Test
  void testGetUserProfile_APIException() {
    MockHttpServletRequest request = CustomTestData.getRequest();
    User user = CustomTestData.getUser();
    request.setAttribute("user", user);
    
    UserDTO userDTO = new UserDTO();
    when(service.getContactDetails(anyString())).thenReturn(userDTO);
    
    Exception e = assertThrows(APIException.class, () -> controller.getUserProfile(request));
    assertEquals("Something went wrong to fetch the user data", e.getMessage());
  }  
}
