package com.aspire.kgp.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.DocumentDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.model.UserVideo;
import com.aspire.kgp.service.UserVideoService;

class VideoControllerTest {

  @InjectMocks
  VideoController controller;

  @Mock
  UserVideoService service;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddVideo() {
    UserVideo userVideo = CustomTestData.getUserVideo();

    when(service.addContactVideo(anyString(), anyString())).thenReturn(userVideo);

    ResponseEntity<Object> result = controller.addVideo(Constant.TEST, Constant.TEST);

    assertNotNull(result);
  }

  @Test
  void testAddVideo_APIException() {
    when(service.addContactVideo(anyString(), anyString())).thenReturn(null);

    Exception e =
        assertThrows(APIException.class, () -> controller.addVideo(Constant.TEST, Constant.TEST));
    assertEquals("Error in add video", e.getMessage());
  }

  @Test
  void testGetCandidateVideo() {
    when(service.findByContactId(anyString())).thenReturn(new ArrayList<>());

    DocumentDTO result = controller.getCandidateVideo(Constant.TEST);

    assertNull(result);

    List<UserVideo> userVideos = CustomTestData.getUserVideos();
    when(service.findByContactId(anyString())).thenReturn(userVideos);
    result = controller.getCandidateVideo(Constant.TEST);

    UserVideo userVideo = userVideos.get(0);
    assertNotNull(result);
    assertEquals(String.valueOf(userVideo.getId()), result.getId());
    assertEquals(String.valueOf(userVideo.getCreatedDate()), result.getCreatedAt());
    assertEquals(userVideo.getFileToken(), result.getFileName());
  }
}
