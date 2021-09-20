package com.aspire.kgp.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.SearchDTO;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.model.User;
import com.aspire.kgp.model.UserSearch;
import com.aspire.kgp.service.UserSearchService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

class SearchUtilTest {

  @InjectMocks
  SearchUtil util;

  @Mock
  UserSearchService searchService;

  @Mock
  RestUtil restUtil;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetSearchList() throws JSONException {
    when(searchService.findByIsDeletedFalse()).thenReturn(new ArrayList<>());

    List<SearchDTO> result = util.getSearchList(Constant.TEST, Constant.TEST);
    assertNotNull(result);
    assertTrue(result.isEmpty());

    List<UserSearch> searches = CustomTestData.getUserSearches();
    when(searchService.findByIsDeletedFalse()).thenReturn(searches);
    String element =
        new Gson().toJson(CustomTestData.getSearchDTOs(), new TypeToken<ArrayList<SearchDTO>>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;
        }.getType());
    when(restUtil.newGetMethod(anyString())).thenReturn(new JSONArray(element).toString());
    result = util.getSearchList(Constant.TEST, Constant.TEST);

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  void testgetSearchList_APIException() {
    List<UserSearch> searches = CustomTestData.getUserSearches();
    when(searchService.findByIsDeletedFalse()).thenReturn(searches);
    when(restUtil.newGetMethod(anyString())).thenReturn("{");
    Exception e =
        assertThrows(APIException.class, () -> util.getSearchList(Constant.TEST, Constant.TEST));
    assertEquals("Error in coverting json to object", e.getMessage());
  }

  @Test
  void testgetSearchListForUser() throws JSONException {
    User user = CustomTestData.getUser();
    when(searchService.findByUser(any())).thenReturn(new ArrayList<>());
    List<CandidateDTO> result = util.getSearchListForUser(user, Constant.TEST);
    assertNotNull(result);
    assertTrue(result.isEmpty());

    List<UserSearch> userSearchs = CustomTestData.getUserSearches();
    when(searchService.findByUser(any())).thenReturn(userSearchs);

    String element =
        new Gson().toJson(CustomTestData.getSearchDTOs(), new TypeToken<ArrayList<SearchDTO>>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;
        }.getType());
    when(restUtil.postMethod(anyString(), anyString(), any()))
        .thenReturn(new JSONArray(element).toString());
    result = util.getSearchListForUser(user, Constant.TEST);

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

}
