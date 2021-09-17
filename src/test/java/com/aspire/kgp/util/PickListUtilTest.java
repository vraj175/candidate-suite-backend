package com.aspire.kgp.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.exception.APIException;

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

}
