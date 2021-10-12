package com.cgi.op.pyr.exception;

/** Generic application exception */
public class AppException extends Exception {

  public static final String STATUS_MUST_BE_NEW_TO_ADD_TRANSACTION =
      "Rule 1 - Status must be 'NEW' to add Transaction : ";
  public static final String STATUS_CAPTURED_IS_AVAILABLE_ONLY_FROM_AUTHORIZED_FOR_TRANSACTION =
      "Rule 2 - Status 'CAPTURED' is available only from 'AUTHORIZED' for Transaction : ";
  public static final String STATUS_CAPTURED_CAN_NOT_BE_UPDATED_FOR_TRANSACTION =
      "Rule 3 - Status 'CAPTURED' can not be updated for Transaction :  ";

  /**
   * Constructor
   *
   * @param msg
   */
  public AppException(String msg) {
    super(msg);
  }
}
