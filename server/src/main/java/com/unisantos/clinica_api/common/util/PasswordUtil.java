package com.unisantos.clinica_api.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

  private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

  public static String hashPassword(String rawPassword) {
    return ENCODER.encode(rawPassword);
  }

  public String encodePassword(String rawPassword) {
    return ENCODER.encode(rawPassword);
  }

  public boolean matches(String rawPassword, String encodedPassword) {
    return ENCODER.matches(rawPassword, encodedPassword);
  }
}
