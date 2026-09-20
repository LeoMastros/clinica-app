package com.unisantos.clinica_api.auth;

import com.unisantos.clinica_api.auth.dto.LoginRequest;
import com.unisantos.clinica_api.auth.dto.LoginResponse;
import com.unisantos.clinica_api.auth.dto.RefreshTokenResponse;
import com.unisantos.clinica_api.auth.dto.ResetPasswordRequest;
import com.unisantos.clinica_api.common.dto.ApiResponse;
import com.unisantos.clinica_api.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

  private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
  private static final Duration REFRESH_TOKEN_COOKIE_MAX_AGE = Duration.ofDays(7);

  private final AuthService authService;
  private final boolean cookieSecure;

  public AuthController(
      AuthService authService, @Value("${app.auth.cookie-secure}") boolean cookieSecure) {
    this.authService = authService;
    this.cookieSecure = cookieSecure;
  }

  @SecurityRequirements
  @PostMapping("/login")
  @Operation(
      summary = "Authenticate user",
      description = "Authenticate user with email and password")
  public ResponseEntity<ApiResponse<LoginResponse>> login(
      @Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    AuthService.LoginResult result = authService.login(loginRequest);

    ResponseCookie cookie =
        ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, result.refreshTokenCookieValue())
            .httpOnly(true)
            .secure(cookieSecure)
            .path("/api/v1/auth")
            .maxAge(REFRESH_TOKEN_COOKIE_MAX_AGE)
            .sameSite("Lax")
            .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(
        ApiResponse.<LoginResponse>builder()
            .success(true)
            .message("Login successful")
            .data(result.response())
            .build());
  }

  @SecurityRequirements
  @PostMapping("/refresh")
  @Operation(
      summary = "Refresh access token",
      description = "Refresh access token using httpOnly refresh cookie")
  public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
      @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String rawRefreshToken,
      HttpServletResponse response) {
    AuthService.RefreshResult result = authService.refreshToken(rawRefreshToken);

    ResponseCookie cookie =
        ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, result.refreshTokenCookieValue())
            .httpOnly(true)
            .secure(cookieSecure)
            .path("/api/v1/auth")
            .maxAge(REFRESH_TOKEN_COOKIE_MAX_AGE)
            .sameSite("Lax")
            .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(
        ApiResponse.<RefreshTokenResponse>builder()
            .success(true)
            .message("Token refreshed successfully")
            .data(result.response())
            .build());
  }

  @GetMapping("/me")
  @Operation(
      summary = "Current user",
      description =
          "Returns the authenticated user's profile. Requires a valid Bearer access token — "
              + "used by the client to restore the session after a page reload.")
  public ResponseEntity<ApiResponse<UserDto>> me(@AuthenticationPrincipal UserDetails userDetails) {
    UserDto user = authService.getCurrentUser(userDetails.getUsername());
    return ResponseEntity.ok(ApiResponse.<UserDto>builder().success(true).data(user).build());
  }

  @SecurityRequirements
  @PostMapping("/logout")
  @Operation(
      summary = "Logout user",
      description = "Revoke refresh token and clear httpOnly cookie")
  public ResponseEntity<ApiResponse<Void>> logout(
      @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String rawRefreshToken,
      HttpServletResponse response) {
    authService.logout(rawRefreshToken);

    ResponseCookie cookie =
        ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
            .httpOnly(true)
            .secure(cookieSecure)
            .path("/api/v1/auth")
            .maxAge(0)
            .sameSite("Lax")
            .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(
        ApiResponse.<Void>builder().success(true).message("Logged out successfully").build());
  }

  @PostMapping("/forgot-password")
  @Operation(
      summary = "Forgot password",
      description =
          "Generate a password reset token for the authenticated account. "
              + "Requires Bearer token — only the account owner can request a reset.")
  public ResponseEntity<ApiResponse<String>> forgotPassword(
      @AuthenticationPrincipal UserDetails userDetails) {
    String resetToken = authService.forgotPassword(userDetails.getUsername());
    return ResponseEntity.ok(
        ApiResponse.<String>builder()
            .success(true)
            .message("Password reset token generated")
            .data(resetToken)
            .build());
  }

  @PostMapping("/reset-password")
  @Operation(
      summary = "Reset password",
      description =
          "Reset password using a reset token. Requires Bearer token — the token must "
              + "belong to the authenticated account.")
  public ResponseEntity<ApiResponse<Void>> resetPassword(
      @Valid @RequestBody ResetPasswordRequest request,
      @AuthenticationPrincipal UserDetails userDetails) {
    authService.resetPassword(request, userDetails.getUsername());
    return ResponseEntity.ok(
        ApiResponse.<Void>builder().success(true).message("Password reset successfully").build());
  }
}
