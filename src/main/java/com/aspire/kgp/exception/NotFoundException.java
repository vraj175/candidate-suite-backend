package com.aspire.kgp.exception;

public class NotFoundException extends RuntimeException {

  private static final long serialVersionUID = -8755014638004660118L;

  /***
   * 
   * @param str
   */
  public NotFoundException(String str) {
    super(str);
  }
}
