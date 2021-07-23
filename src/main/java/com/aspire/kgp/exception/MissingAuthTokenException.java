package com.aspire.kgp.exception;

public class MissingAuthTokenException extends RuntimeException {
  private static final long serialVersionUID = -8755014638004660118L;

  /***
   * 
   * @param str
   */
  public MissingAuthTokenException(String str) {
    super(str);
  }
}
