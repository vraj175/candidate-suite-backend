package com.aspire.kgp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.dto.PickListDTO;
import com.aspire.kgp.service.PickListService;

class PickListControllerTest {

  @InjectMocks
  PickListController controller;

  @Mock
  PickListService service;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetEducationDegrees() {
    List<String> strings = CustomTestData.getStrings();
    when(service.getEducationDegrees()).thenReturn(strings);

    List<String> result = controller.getEducationDegrees();

    assertNotNull(result);
    assertEquals(strings.size(), result.size());
  }

  @Test
  void testGetReferencesType() {
    List<PickListDTO> pickListDTOs = CustomTestData.getPickListDTOs();
    when(service.getReferencesType()).thenReturn(pickListDTOs);

    List<PickListDTO> result = controller.getReferencesType();

    assertNotNull(result);
    assertEquals(pickListDTOs.size(), result.size());
  }
}
