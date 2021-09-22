package com.aspire.kgp.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.ContactDTO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

class ContactUtilTest {

  @InjectMocks
  ContactUtil util;

  @Mock
  RestUtil restUtil;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetContactDetails() {
    ContactDTO contactDTO = CustomTestData.getContactDTO();
    String element = new Gson().toJson(contactDTO, new TypeToken<ContactDTO>() {

      /**
       * 
       */
      private static final long serialVersionUID = 1L;
    }.getType());
    when(restUtil.newGetMethod(anyString())).thenReturn(element);

    ContactDTO response = util.getContactDetails(Constant.TEST);

    assertNotNull(response);
    assertEquals(contactDTO.getBaseSalary(), response.getBaseSalary());
    assertEquals(contactDTO.getCompensationExpectation(), response.getCompensationExpectation());
    assertEquals(contactDTO.getCompensationNotes(), response.getCompensationNotes());
    assertEquals(contactDTO.getCurrentJobTitle(), response.getCurrentJobTitle());
    assertEquals(contactDTO.getEquity(), response.getEquity());
    assertEquals(contactDTO.getHomePhone(), response.getHomePhone());
    assertEquals(contactDTO.getPublishedBio(), response.getPublishedBio());
    assertEquals(contactDTO.getTargetBonusValue(), response.getTargetBonusValue());
    assertEquals(contactDTO.getJobHistory().size(), response.getJobHistory().size());
    assertEquals(contactDTO.getBoardDetails().size(), response.getBoardDetails().size());
    assertEquals(contactDTO.getEducationDetails().size(), response.getEducationDetails().size());
    assertEquals(contactDTO.getCompany().getId(), response.getCompany().getId());
  }

}
