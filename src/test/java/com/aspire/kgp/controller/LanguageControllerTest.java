package com.aspire.kgp.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.Language;
import com.aspire.kgp.service.LanguageService;

class LanguageControllerTest {

  @InjectMocks
  LanguageController controller;

  @Mock
  LanguageService service;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetLanguages() {
    List<Language> roles = CustomTestData.getLanguages();

    when(service.findAll()).thenReturn(roles);
    
    List<Language> result = controller.getLanguages();
    
    assertNotNull(result);
    assertEquals(roles.size(), result.size());
  }

  @Test
  void testInitializeLanguages() {
    when(service.initializeData()).thenReturn(Constant.TEST);
    
    String result = controller.initializeLanguages();
    
    assertNotNull(result);
    assertEquals(Constant.TEST, result);
  }

}
