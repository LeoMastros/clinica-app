package com.unisantos.clinica_api.security.jwt;

import com.unisantos.clinica_api.common.util.JwtUtil;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final JwtUtil jwtUtil;

  public JwtTokenProvider(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  public String generateToken(String username) {
    return jwtUtil.generateToken(username);
  }

  public boolean validateToken(String token, String username) {
    return jwtUtil.validateToken(token, username);
  }

  public String extractUsername(String token) {
    return jwtUtil.extractUsername(token);
  }
}
