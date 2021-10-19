package com.aspire.kgp.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.UserSearch;
import com.aspire.kgp.repository.UserSearchRepository;

class UserSearchServiceImplTest {

  @InjectMocks
  UserSearchServiceImpl service;

  @Mock
  UserSearchRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindByUserAndCandidateId() {
    UserSearch userSearch = CustomTestData.getUserSearch();
    when(repository.findByUserAndCandidateIdAndIsDeletedFalse(any(), anyString()))
        .thenReturn(userSearch);

    UserSearch result = service.findByUserAndCandidateId(CustomTestData.getUser(), Constant.TEST);

    assertNotNull(result);
    assertEquals(userSearch.getCompanyId(), result.getCompanyId());
    assertEquals(userSearch.getSearchId(), result.getSearchId());
    assertEquals(userSearch.getCandidateId(), result.getCandidateId());
    assertEquals(userSearch.getUser(), result.getUser());
    assertEquals(userSearch.getInvitedBy(), result.getInvitedBy());
    assertEquals(userSearch.isDeleted(), result.isDeleted());
  }
  
  @Test
  void testFindByUserAndSearchId() {
    UserSearch userSearch = CustomTestData.getUserSearch();
    when(repository.findByUserAndSearchIdAndIsDeletedFalse(any(), anyString()))
        .thenReturn(userSearch);

    UserSearch result = service.findByUserAndSearchId(CustomTestData.getUser(), Constant.TEST);

    assertNotNull(result);
    assertEquals(userSearch.getCompanyId(), result.getCompanyId());
    assertEquals(userSearch.getSearchId(), result.getSearchId());
    assertEquals(userSearch.getCandidateId(), result.getCandidateId());
    assertEquals(userSearch.getUser(), result.getUser());
    assertEquals(userSearch.getInvitedBy(), result.getInvitedBy());
    assertEquals(userSearch.isDeleted(), result.isDeleted());
  }

  @Test
  void testFindByCandidateId() {
    UserSearch userSearch = CustomTestData.getUserSearch();
    when(repository.findByCandidateIdAndIsDeletedFalse(anyString())).thenReturn(userSearch);

    UserSearch result = service.findByCandidateId(Constant.TEST);

    assertNotNull(result);
    assertEquals(userSearch.getSearchId(), result.getSearchId());
    assertEquals(userSearch.getCandidateId(), result.getCandidateId());
    assertEquals(userSearch.getUser(), result.getUser());
    assertEquals(userSearch.getInvitedBy(), result.getInvitedBy());
    assertEquals(userSearch.isDeleted(), result.isDeleted());
  }

  @Test
  void testDeleteUserSearch() {
    UserSearch userSearch = CustomTestData.getUserSearch();
    when(repository.save(any())).thenReturn(userSearch);

    UserSearch result = service.deleteUserSearch(userSearch);

    assertNotNull(result);
    assertEquals(userSearch.getSearchId(), result.getSearchId());
    assertEquals(userSearch.getCandidateId(), result.getCandidateId());
    assertEquals(userSearch.getUser(), result.getUser());
    assertEquals(userSearch.getInvitedBy(), result.getInvitedBy());
    assertEquals(userSearch.isDeleted(), result.isDeleted());
  }
  
  @Test
  void testfindByUser() {
    List<UserSearch> userSearches = CustomTestData.getUserSearches();
    when(repository.findByUserAndIsDeletedFalse(any())).thenReturn(userSearches);

    List<UserSearch> result = service.findByUser(CustomTestData.getUser());

    assertNotNull(result);
    assertEquals(userSearches.size(), result.size());
  }
  
  @Test
  void testfindByIsDeletedFalse() {
    List<UserSearch> userSearches = CustomTestData.getUserSearches();
    when(repository.findByIsDeletedFalse()).thenReturn(userSearches);

    List<UserSearch> result = service.findByIsDeletedFalse();

    assertNotNull(result);
    assertEquals(userSearches.size(), result.size());
  }

}
