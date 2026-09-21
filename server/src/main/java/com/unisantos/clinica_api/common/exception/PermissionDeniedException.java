package com.unisantos.clinica_api.common.exception;

public class PermissionDeniedException extends RuntimeException {

  public PermissionDeniedException(String message) {
    super(message);
  }

  public PermissionDeniedException(String message, Throwable cause) {
    super(message, cause);
  }
}
