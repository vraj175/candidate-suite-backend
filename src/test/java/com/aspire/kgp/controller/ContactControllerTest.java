package com.aspire.kgp.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.ContactDTO;
import com.aspire.kgp.service.ContactService;

class ContactControllerTest {

  @InjectMocks
  ContactController controller;

  @Mock
  ContactService service;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetCandidateDetails() {
    ContactDTO contactDTO = CustomTestData.getContactDTO();
    when(service.getContactDetails(anyString())).thenReturn(contactDTO);

    MappingJacksonValue mapping = controller.getCandidateDetails(Constant.TEST);
    ContactDTO response = (ContactDTO) mapping.getValue();

    assertNotNull(response);
    assertEquals(contactDTO.getEducationDetails().get(0).getId(),
        response.getEducationDetails().get(0).getId());
    assertEquals(contactDTO.getEducationDetails().get(0).getPosition(),
        response.getEducationDetails().get(0).getPosition());
    assertEquals(contactDTO.getEducationDetails().get(0).getSchoolName(),
        response.getEducationDetails().get(0).getSchoolName());
    assertEquals(contactDTO.getEducationDetails().get(0).getDegreeName(),
        response.getEducationDetails().get(0).getDegreeName());
    assertEquals(contactDTO.getEducationDetails().get(0).getMajor(),
        response.getEducationDetails().get(0).getMajor());
    assertEquals(contactDTO.getEducationDetails().get(0).getDegreeYear(),
        response.getEducationDetails().get(0).getDegreeYear());
    assertEquals(contactDTO.getCompany().getWebsite(), response.getCompany().getWebsite());
    assertEquals(contactDTO.getCompany().getDescription(), response.getCompany().getDescription());
    assertEquals(contactDTO.getCompany().getName(), response.getCompany().getName());
    assertEquals(contactDTO.getJobHistory().get(0).getId(),
        response.getJobHistory().get(0).getId());
    assertEquals(contactDTO.getJobHistory().get(0).getStartYear(),
        response.getJobHistory().get(0).getStartYear());
    assertEquals(contactDTO.getJobHistory().get(0).getTitle(),
        response.getJobHistory().get(0).getTitle());
    assertEquals(contactDTO.getJobHistory().get(0).getEndYear(),
        response.getJobHistory().get(0).getEndYear());
    assertEquals(contactDTO.getJobHistory().get(0).getPosition(),
        response.getJobHistory().get(0).getPosition());
    assertEquals(contactDTO.getJobHistory().get(0).getCompany(),
        response.getJobHistory().get(0).getCompany());
    assertEquals(contactDTO.getJobHistory().get(0).getCompany(),
        response.getJobHistory().get(0).getCompany());
  }
  
  @Test
  void testGetContactImage() {
    byte[] bs= new byte[5];
    when(service.getContactImage(anyString())).thenReturn(bs);
   
    byte[] response = controller.getContactImage(Constant.TEST);
    assertNotNull(response);
    assertEquals(bs, response);
  }
  
  @Test
  void testUpdateContactDetails() throws UnsupportedEncodingException {
    when(service.updateContactDetails(anyString(), anyString())).thenReturn(Constant.TEST);
    
    String response = controller.updateContactDetails(Constant.TEST, Constant.TEST);
    assertNotNull(response);
    assertEquals(Constant.TEST, response);
  }

}
