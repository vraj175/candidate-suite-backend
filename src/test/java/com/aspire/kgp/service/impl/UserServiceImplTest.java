package com.aspire.kgp.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.aspire.kgp.model.UserSearch;
import com.aspire.kgp.repository.UserRepository;
import com.aspire.kgp.service.LanguageService;
import com.aspire.kgp.service.MailService;
import com.aspire.kgp.service.RoleService;
import com.aspire.kgp.service.UserSearchService;
import com.aspire.kgp.util.RestUtil;

class UserServiceImplTest {

  @InjectMocks
  UserServiceImpl service;

  @Mock
  UserRepository repository;

  @Mock
  RoleService roleService;

  @Mock
  LanguageService languageService;

  @Mock
  UserSearchService searchService;

  @Mock
  MailService mailService;

  @Mock
  RestUtil restUtil;

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

  @Test
  void testSaveOrUpdatePartner() {
    User user = CustomTestData.getUser();

    when(service.saveorUpdate(any())).thenReturn(user);
    when(roleService.findByName(anyString())).thenReturn(CustomTestData.getRole());
    when(languageService.findByName(anyString())).thenReturn(CustomTestData.getLanguage());
    when(service.findByEmail(anyString())).thenReturn(user);

    User result =
        service.saveOrUpdatePartner(Constant.TEST, Constant.TEST, Constant.TEST, Boolean.TRUE);

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

    when(service.findByEmail(anyString())).thenReturn(null);

    result =
        service.saveOrUpdatePartner(Constant.TEST, Constant.TEST, Constant.TEST, Boolean.FALSE);

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
  void testInviteUser() {
    User user = CustomTestData.getUser();
    UserSearch userSearch = CustomTestData.getUserSearch();
    String responseJson = "{" + "    \"candidate\": {" + "        \"contact\": {"
        + "            \"id\": " + Constant.TEST + "," + "            \"first_name\": "
        + Constant.TEST + "," + "            \"last_name\": " + Constant.TEST + "         }, "
        + "        \"search\": {" + "             \"id\": " + Constant.TEST + "         } "
        + "     } " + "}";
    when(restUtil.newGetMethod(anyString())).thenReturn(responseJson);
    when(service.findByEmail(anyString())).thenReturn(null);
    when(service.findByGalaxyId(anyString())).thenReturn(null);
    when(service.saveorUpdate(any())).thenReturn(user);
    when(searchService.saveorUpdate(any())).thenReturn(userSearch);

    boolean result = service.inviteUser(Constant.TEST, Constant.TEST, Constant.TEST,
        new String[] {}, user, Boolean.FALSE, CustomTestData.getRequest());

    assertTrue(result);

    when(service.findByGalaxyId(anyString())).thenReturn(user);
    when(searchService.findByUserAndCandidateId(any(), anyString())).thenReturn(null);

    result = service.inviteUser(Constant.TEST, Constant.TEST, Constant.TEST, new String[] {}, user,
        Boolean.FALSE, CustomTestData.getRequest());

    assertTrue(result);

    user.setPasswordReset(Boolean.TRUE);
    when(searchService.findByUserAndCandidateId(any(), anyString())).thenReturn(userSearch);
    result = service.inviteUser(Constant.TEST, Constant.TEST, Constant.TEST, new String[] {}, user,
        Boolean.TRUE, CustomTestData.getRequest());

    assertTrue(result);

  }
}
