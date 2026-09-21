package com.unisantos.clinica_api.common.constants;

public final class SecurityConstants {

  private SecurityConstants() {}

  public static final String JWT_HEADER = "Authorization";
  public static final String JWT_PREFIX = "Bearer ";
  public static final long JWT_EXPIRATION_MS = 86400000;
}
