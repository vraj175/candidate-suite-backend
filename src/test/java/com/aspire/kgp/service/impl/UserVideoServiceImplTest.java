package com.aspire.kgp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.model.UserSearch;
import com.aspire.kgp.model.UserVideo;
import com.aspire.kgp.repository.UserVideoRepository;
import com.aspire.kgp.service.UserSearchService;

class UserVideoServiceImplTest {

  @InjectMocks
  UserVideoServiceImpl service;

  @Mock
  UserVideoRepository repository;

  @Mock
  UserSearchService searchService;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSaveorUpdate() {
    UserVideo userVideo = CustomTestData.getUserVideo();

    when(repository.save(any())).thenReturn(userVideo);

    UserVideo result = service.saveorUpdate(userVideo);

    assertNotNull(result);
    assertEquals(userVideo.getId(), result.getId());
    assertEquals(userVideo.getCreatedDate(), result.getCreatedDate());
    assertEquals(userVideo.getModifyDate(), result.getModifyDate());
    // assertEquals(userVideo.getUserSearch(), result.getUserSearch());
    assertEquals(userVideo.isDeleted(), result.isDeleted());
    assertEquals(userVideo.getFileToken(), result.getFileToken());
  }

  @Test
  void testAddCandidateVideo() {
    MockHttpServletRequest request = CustomTestData.getRequest();
    UserVideo userVideo = CustomTestData.getUserVideo();
    UserSearch userSearch = CustomTestData.getUserSearch();

    when(searchService.findByCandidateId(anyString())).thenReturn(userSearch);
    when(service.saveorUpdate(any())).thenReturn(userVideo);

    UserVideo result =
        service.addContactVideo(Constant.TEST, Constant.TEST, Constant.TEST, request);

    assertNotNull(result);
    assertEquals(userVideo.getId(), result.getId());
    assertEquals(userVideo.getCreatedDate(), result.getCreatedDate());
    assertEquals(userVideo.getModifyDate(), result.getModifyDate());
    // assertEquals(userVideo.getUserSearch(), result.getUserSearch());
    assertEquals(userVideo.isDeleted(), result.isDeleted());
    assertEquals(userVideo.getFileToken(), result.getFileToken());
  }

  // @Test
  // void testAddCandidateVideo_NotFoundException() {
  // when(searchService.findByCandidateId(anyString())).thenReturn(null);
  //
  // Exception e = assertThrows(NotFoundException.class,
  // () -> service.addContactVideo(Constant.TEST, Constant.TEST));
  // assertEquals("Candidate is not available", e.getMessage());
  // }

  @Test
  void testFindByCandidateId() {
    List<UserVideo> userVideos = CustomTestData.getUserVideos();
    UserSearch userSearch = CustomTestData.getUserSearch();

    when(searchService.findByCandidateId(anyString())).thenReturn(userSearch);
    when(repository.findByContactIdAndIsDeletedFalseOrderByCreatedDateDesc(any()))
        .thenReturn(userVideos);

    List<UserVideo> result = service.findByContactId(Constant.TEST);

    assertNotNull(result);
    assertEquals(userVideos.size(), result.size());
  }

  // @Test
  // void testFindByCandidateId_NotFoundException() {
  // when(searchService.findByCandidateId(anyString())).thenReturn(null);
  //
  // Exception e =
  // assertThrows(NotFoundException.class, () -> service.findByContactId(Constant.TEST));
  // assertEquals("Candidate is not available", e.getMessage());
  // }
}
