package com.aspire.kgp.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;

import com.aspire.kgp.CustomTestData;
import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.dto.CandidateDTO;
import com.aspire.kgp.dto.UserDTO;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

class MailServiceImplTest {

  @InjectMocks
  MailServiceImpl service;

  @Mock
  Configuration configuration;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetEmailContent() throws IOException, TemplateException {
    MockHttpServletRequest request = CustomTestData.getRequest();
    UserDTO user = CustomTestData.getUserDTO();
    CandidateDTO candidateDTO = CustomTestData.getCandidateDTO();
    Template template = new Template(Constant.TEST, Constant.TEST, null);
    when(configuration.getTemplate(any())).thenReturn(template);

    String result =
        service.getEmailContent(request, user, new HashMap<>(), Constant.TEST, candidateDTO);

    assertNotNull(result);
  }

}
