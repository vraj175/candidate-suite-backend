package com.aspire.kgp.util;

import static org.junit.jupiter.api.Assertions.*;
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
import com.aspire.kgp.dto.PickListDTO;
import com.aspire.kgp.exception.APIException;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

class PickListUtilTest {
  
  @InjectMocks
  PickListUtil util;
  
  @Mock
  RestUtil restUtil;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetEducationDegrees() {
    List<String> strings = CustomTestData.getStrings();
    String response = strings.toString();
    when(restUtil.newGetMethod(anyString())).thenReturn(response);
    
    List<String> result = util.getEducationDegrees();
    
    assertNotNull(result);
    assertEquals(strings.size(), result.size());
  }
  
  @Test
  void testGetEducationDegrees_APIException() {
    String response = "{";
    when(restUtil.newGetMethod(anyString())).thenReturn(response);
    
    Exception e = assertThrows(APIException.class, () -> util.getEducationDegrees());
    assertNotNull(e);
    
  }
  
  @Test
  void testGetReferencesType() throws JSONException {
    List<PickListDTO> pickListDTOs = CustomTestData.getPickListDTOs();
    String element =
        new Gson().toJson(pickListDTOs, new TypeToken<ArrayList<PickListDTO>>() {

          /**
           * 
           */
          private static final long serialVersionUID = 1L;
        }.getType());
    String response = new JSONArray(element).toString();
    when(restUtil.newGetMethod(anyString())).thenReturn(response);
    
    List<PickListDTO> result = util.getReferencesType();
    
    assertNotNull(result);
    assertEquals(pickListDTOs.size(), result.size());
    assertEquals(pickListDTOs.get(0).getName(), result.get(0).getName());
  }
  
  @Test
  void testGetReferencesType_APIException() {
    String response = "{";
    when(restUtil.newGetMethod(anyString())).thenReturn(response);
    
    Exception e = assertThrows(APIException.class, () -> util.getReferencesType());
    assertNotNull(e);
  }

}
