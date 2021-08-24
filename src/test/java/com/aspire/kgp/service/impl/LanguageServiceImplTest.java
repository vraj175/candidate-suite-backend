package com.aspire.kgp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.model.Language;
import com.aspire.kgp.repository.LanguageRepository;

class LanguageServiceImplTest {

  @InjectMocks
  LanguageServiceImpl service;

  @Mock
  LanguageRepository repository;

  /**
   * @throws java.lang.Exception
   */
  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }
  
  private Language getLanguage() {
    Timestamp t1 = new Timestamp(System.currentTimeMillis());

    Language language = new Language();
    language.setId(Long.MIN_VALUE);
    language.setName(Constant.TEST);
    language.setCreatedDate(t1);
    language.setModifyDate(t1);
    
    return language;
  }

  @Test
  void testSaveorUpdate() {
    Language language = getLanguage();

    when(repository.save(any())).thenReturn(language);

    Language result = service.saveorUpdate(language);

    assertNotNull(result);
    assertEquals(result.getId(), language.getId());
    assertEquals(result.getName(), language.getName());
    assertEquals(result.getCreatedDate(), language.getCreatedDate());
    assertEquals(result.getModifyDate(), language.getModifyDate());
  }
  
  @Test
  void testFindByName() {
    Language language = getLanguage();

    when(repository.findByName(anyString())).thenReturn(language);

    Language result = service.findByName(Constant.TEST);

    assertNotNull(result);
    assertEquals(result.getId(), language.getId());
    assertEquals(result.getName(), language.getName());
    assertEquals(result.getCreatedDate(), language.getCreatedDate());
    assertEquals(result.getModifyDate(), language.getModifyDate());
  }

}
