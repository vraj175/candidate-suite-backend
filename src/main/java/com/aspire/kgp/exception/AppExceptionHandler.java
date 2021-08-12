package com.aspire.kgp.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.aspire.kgp.constant.Constant;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
  static Log log = LogFactory.getLog(AppExceptionHandler.class.getName());

  /***
   * 
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(APIException.class)
  public ResponseEntity<Object> handleAPIException(APIException ex, WebRequest request) {
    String errorMessgeDescription = ex.getMessage();
    Map<String, Object> body = new LinkedHashMap<>();
    body.put(Constant.TIMESTAMP, LocalDateTime.now());
    body.put(Constant.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
    body.put(Constant.ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    body.put(Constant.MESSAGE, errorMessgeDescription);
    body.put(Constant.PATH, request.getDescription(false));
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /***
   * 
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(MissingAuthTokenException.class)
  public ResponseEntity<Object> handleMissingAuthToken(MissingAuthTokenException ex,
      WebRequest request) {
    String errorMessgeDescription = ex.getMessage();
    Map<String, Object> body = new LinkedHashMap<>();
    body.put(Constant.TIMESTAMP, LocalDateTime.now());
    body.put(Constant.STATUS, HttpStatus.BAD_REQUEST.value());
    body.put(Constant.ERROR, HttpStatus.BAD_REQUEST);
    body.put(Constant.MESSAGE, errorMessgeDescription);
    body.put(Constant.PATH, request.getDescription(false));
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  /***
   * 
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(UnauthorizedAccessException.class)
  public ResponseEntity<Object> handleUnauthorizedAccessException(UnauthorizedAccessException ex,
      WebRequest request) {
    String errorMessgeDescription = ex.getMessage();
    Map<String, Object> body = new LinkedHashMap<>();
    body.put(Constant.TIMESTAMP, LocalDateTime.now());
    body.put(Constant.STATUS, HttpStatus.UNAUTHORIZED.value());
    body.put(Constant.ERROR, HttpStatus.UNAUTHORIZED);
    body.put(Constant.MESSAGE, errorMessgeDescription);
    body.put(Constant.PATH, request.getDescription(false));
    return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
  }

  /***
   * 
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(ValidateException.class)
  public ResponseEntity<Object> handleValidateException(ValidateException ex, WebRequest request) {
    String errorMessgeDescription = ex.getMessage();
    Map<String, Object> body = new LinkedHashMap<>();
    body.put(Constant.TIMESTAMP, LocalDateTime.now());
    body.put(Constant.STATUS, HttpStatus.OK.value());
    body.put(Constant.ERROR, HttpStatus.OK);
    body.put(Constant.MESSAGE, errorMessgeDescription);
    body.put(Constant.PATH, request.getDescription(false));
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  /***
   * 
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
    String errorMessgeDescription = ex.getMessage();
    Map<String, Object> body = new LinkedHashMap<>();
    body.put(Constant.TIMESTAMP, LocalDateTime.now());
    body.put(Constant.STATUS, HttpStatus.NOT_FOUND.value());
    body.put(Constant.ERROR, HttpStatus.NOT_FOUND);
    body.put(Constant.MESSAGE, errorMessgeDescription);
    body.put(Constant.PATH, request.getDescription(false));
    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }
}
