package com.aspire.kgp.exception;

public class UnauthorizedAccessException extends RuntimeException {
  private static final long serialVersionUID = -8755014638004660118L;

  /***
   * 
   * @param str
   */
  public UnauthorizedAccessException(String str) {
    super(str);
  }

  /***
   * 
   */
  public UnauthorizedAccessException() {
    super("");
  }
}
