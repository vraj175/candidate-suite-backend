package com.aspire.kgp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
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

  @Test
  void testSaveorUpdate() {
    Language language = CustomTestData.getLanguage();

    when(repository.save(any())).thenReturn(language);

    Language result = service.saveorUpdate(language);

    assertNotNull(result);
    assertEquals(language.getId(), result.getId());
    assertEquals(language.getName(), result.getName());
    assertEquals(language.getCreatedDate(), result.getCreatedDate());
    assertEquals(language.getModifyDate(), result.getModifyDate());
  }

  @Test
  void testSaveAll() {
    List<Language> languages = CustomTestData.getLanguages();

    when(repository.saveAll(any())).thenReturn(languages);

    List<Language> result = service.saveAll(languages);

    assertNotNull(result);
    assertEquals(languages.size(), result.size());
  }

  @Test
  void testFindAll() {
    List<Language> languages = CustomTestData.getLanguages();

    when(repository.findAll()).thenReturn(languages);

    List<Language> result = service.findAll();

    assertNotNull(result);
    assertEquals(languages.size(), result.size());
  }

  @Test
  void testFindByName() {
    Language language = CustomTestData.getLanguage();

    when(repository.findByName(anyString())).thenReturn(language);

    Language result = service.findByName(Constant.TEST);

    assertNotNull(result);
    assertEquals(language.getId(), result.getId());
    assertEquals(language.getName(), result.getName());
    assertEquals(language.getCreatedDate(), result.getCreatedDate());
    assertEquals(language.getModifyDate(), result.getModifyDate());
  }

  @Test
  void testInitializeData() {
    List<Language> languages = CustomTestData.getLanguages();

    when(repository.findAll()).thenReturn(languages);

    String result = service.initializeData();

    assertNotNull(result);
    assertEquals(Constant.DATA_ALREADY_INITIALIZED, result);

    when(repository.findAll()).thenReturn(new ArrayList<>());

    result = service.initializeData();

    assertNotNull(result);
    assertEquals(Constant.DATA_SAVED, result);
  }

}
