package com.aspire.kgp.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
import com.aspire.kgp.dto.SearchDTO;
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
    String element = new Gson().toJson(
        CustomTestData.getSearchDTOs(),new TypeToken<ArrayList<SearchDTO>>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;}.getType());
    when(restUtil.newGetMethod(anyString())).thenReturn(new JSONArray(element).toString());
    result = util.getSearchList(Constant.TEST, Constant.TEST);
    
    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

}
