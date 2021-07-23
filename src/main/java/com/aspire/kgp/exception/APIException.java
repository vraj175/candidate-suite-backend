package com.aspire.kgp.exception;

public class APIException extends RuntimeException {

  private static final long serialVersionUID = -8755014638004660118L;

  /***
   * 
   * @param str
   */
  public APIException(String str) {
    super(str);
  }
}
