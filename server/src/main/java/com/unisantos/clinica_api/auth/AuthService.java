package com.unisantos.clinica_api.auth;

import com.unisantos.clinica_api.auth.dto.LoginRequest;
import com.unisantos.clinica_api.auth.dto.LoginResponse;
import com.unisantos.clinica_api.auth.dto.RefreshTokenResponse;
import com.unisantos.clinica_api.auth.dto.ResetPasswordRequest;
import com.unisantos.clinica_api.auth.entity.PasswordResetToken;
import com.unisantos.clinica_api.auth.entity.PasswordResetTokenRepository;
import com.unisantos.clinica_api.auth.entity.RefreshToken;
import com.unisantos.clinica_api.auth.entity.RefreshTokenRepository;
import com.unisantos.clinica_api.common.exception.ResourceNotFoundException;
import com.unisantos.clinica_api.common.exception.ValidationException;
import com.unisantos.clinica_api.common.util.PasswordUtil;
import com.unisantos.clinica_api.security.jwt.JwtTokenProvider;
import com.unisantos.clinica_api.user.User;
import com.unisantos.clinica_api.user.UserDto;
import com.unisantos.clinica_api.user.UserRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

  private static final int TOKEN_RANDOM_BYTES = 32;
  private static final int REFRESH_TOKEN_TTL_DAYS = 7;
  private static final int RESET_TOKEN_TTL_HOURS = 1;

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordResetTokenRepository passwordResetTokenRepository;
  private final SecureRandom secureRandom = new SecureRandom();

  public AuthService(
      AuthenticationManager authenticationManager,
      JwtTokenProvider jwtTokenProvider,
      UserRepository userRepository,
      RefreshTokenRepository refreshTokenRepository,
      PasswordResetTokenRepository passwordResetTokenRepository) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.userRepository = userRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.passwordResetTokenRepository = passwordResetTokenRepository;
  }

  public LoginResult login(LoginRequest loginRequest) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(), loginRequest.getPassword()));

    User user =
        userRepository
            .findByLoginEmail(loginRequest.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    if (Boolean.FALSE.equals(user.getIsActive())) {
      throw new ValidationException("User account is inactive");
    }

    String accessToken = jwtTokenProvider.generateToken(user.getLoginEmail());
    String rawRefreshToken = generateRandomString(TOKEN_RANDOM_BYTES);
    byte[] tokenHash = sha256(rawRefreshToken);

    RefreshToken refreshToken =
        RefreshToken.builder()
            .user(user)
            .tokenHash(tokenHash)
            .familyId(System.currentTimeMillis())
            .expiresAt(LocalDateTime.now().plusDays(REFRESH_TOKEN_TTL_DAYS))
            .build();
    refreshTokenRepository.save(refreshToken);

    LoginResponse response =
        LoginResponse.builder()
            .accessToken(accessToken)
            .email(user.getLoginEmail())
            .userId(user.getId())
            .userType(user.getUserType() != null ? user.getUserType().getTypeName() : null)
            .build();

    return new LoginResult(response, rawRefreshToken);
  }

  /** Returns the profile of the currently authenticated user (GET /api/v1/auth/me). */
  @Transactional(readOnly = true)
  public UserDto getCurrentUser(String email) {
    User user =
        userRepository
            .findByLoginEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    return UserDto.builder()
        .id(user.getId())
        .firstName(str(user.getFirstName()))
        .lastName(str(user.getLastName()))
        .loginEmail(user.getLoginEmail())
        .userTypeId(user.getUserType() != null ? user.getUserType().getId() : null)
        .userType(user.getUserType() != null ? user.getUserType().getTypeName() : null)
        .isActive(user.getIsActive())
        .build();
  }

  public RefreshResult refreshToken(String rawRefreshToken) {
    if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
      throw new ValidationException("Refresh token is required");
    }

    byte[] tokenHash = sha256(rawRefreshToken);
    RefreshToken existing =
        refreshTokenRepository
            .findByTokenHash(tokenHash)
            .orElseThrow(() -> new ValidationException("Invalid refresh token"));

    if (existing.getRevokedAt() != null || existing.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new ValidationException("Refresh token is expired or revoked");
    }

    User user = existing.getUser();
    existing.setUsedAt(LocalDateTime.now());
    existing.setRevokedAt(LocalDateTime.now());

    String newAccessToken = jwtTokenProvider.generateToken(user.getLoginEmail());
    String newRawRefreshToken = generateRandomString(TOKEN_RANDOM_BYTES);
    byte[] newTokenHash = sha256(newRawRefreshToken);

    RefreshToken newRefreshToken =
        RefreshToken.builder()
            .user(user)
            .tokenHash(newTokenHash)
            .familyId(existing.getFamilyId())
            .expiresAt(LocalDateTime.now().plusDays(REFRESH_TOKEN_TTL_DAYS))
            .build();
    RefreshToken savedNewToken = refreshTokenRepository.save(newRefreshToken);

    existing.setReplacedByToken(savedNewToken);
    refreshTokenRepository.save(existing);

    return new RefreshResult(new RefreshTokenResponse(newAccessToken), newRawRefreshToken);
  }

  public void logout(String rawRefreshToken) {
    if (rawRefreshToken != null && !rawRefreshToken.isBlank()) {
      byte[] tokenHash = sha256(rawRefreshToken);
      Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByTokenHash(tokenHash);
      tokenOpt.ifPresent(
          token -> {
            token.setRevokedAt(LocalDateTime.now());
            refreshTokenRepository.save(token);
          });
    }
  }

  public String forgotPassword(String email) {
    User user =
        userRepository
            .findByLoginEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    String rawResetToken = generateRandomString(TOKEN_RANDOM_BYTES);
    byte[] tokenHash = sha256(rawResetToken);

    PasswordResetToken resetToken =
        PasswordResetToken.builder()
            .user(user)
            .tokenHash(tokenHash)
            .expiresAt(LocalDateTime.now().plusHours(RESET_TOKEN_TTL_HOURS))
            .build();
    passwordResetTokenRepository.save(resetToken);

    return rawResetToken;
  }

  public void resetPassword(ResetPasswordRequest request, String ownerEmail) {
    byte[] tokenHash = sha256(request.getResetToken());
    PasswordResetToken token =
        passwordResetTokenRepository
            .findByTokenHash(tokenHash)
            .orElseThrow(() -> new ValidationException("Invalid reset token"));

    if (token.getUsedAt() != null || token.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new ValidationException("Reset token is expired or already used");
    }

    User user = token.getUser();
    if (!user.getLoginEmail().equals(ownerEmail)) {
      // The token is valid but belongs to another account — refuse.
      throw new ValidationException("Reset token does not belong to this account");
    }
    user.setPasswordHash(PasswordUtil.hashPassword(request.getNewPassword()));
    userRepository.save(user);

    token.setUsedAt(LocalDateTime.now());
    passwordResetTokenRepository.save(token);
  }

  private static String str(byte[] value) {
    return value == null ? null : new String(value, java.nio.charset.StandardCharsets.UTF_8);
  }

  private byte[] sha256(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return digest.digest(input.getBytes());
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("SHA-256 algorithm not available", e);
    }
  }

  private String generateRandomString(int bytesLength) {
    byte[] bytes = new byte[bytesLength];
    secureRandom.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  public record LoginResult(LoginResponse response, String refreshTokenCookieValue) {}

  public record RefreshResult(RefreshTokenResponse response, String refreshTokenCookieValue) {}
}
