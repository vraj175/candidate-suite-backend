package com.aspire.kgp.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.aspire.kgp.constant.Constant;
import com.aspire.kgp.exception.APIException;
import com.aspire.kgp.exception.AppExceptionHandler;
import com.aspire.kgp.exception.MissingAuthTokenException;
import com.aspire.kgp.exception.NotFoundException;
import com.aspire.kgp.exception.ValidateException;

class AppExceptionHandlerTest {

  @InjectMocks
  AppExceptionHandler appExceptionHandler;

  @Mock
  WebRequest webRequest;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testHandleAPIException() {
    ResponseEntity<Object> responseEntity = appExceptionHandler
        .handleAPIException(new APIException("An exception was thrown!"), webRequest);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
  }

  @Test
  void testHandleMissingAuthToken() {
    ResponseEntity<Object> responseEntity = appExceptionHandler.handleMissingAuthToken(
        new MissingAuthTokenException(Constant.MISSING_REQUEST_HEADER), webRequest);
    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
  }

  @Test
  void testHandleValidateException() {
    ResponseEntity<Object> responseEntity = appExceptionHandler
        .handleValidateException(new ValidateException("Candidate already invited"), webRequest);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @Test
  void testHandleNotFoundException() {
    ResponseEntity<Object> responseEntity =
        appExceptionHandler.handleNotFoundException(new NotFoundException("Not Found"), webRequest);
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }

}
