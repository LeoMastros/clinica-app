package com.unisantos.clinica_api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  @Schema(description = "Institutional e-mail", example = "coordinator@clinica.com")
  private String email;

  @NotBlank(message = "Password is required")
  @Schema(description = "Account password", example = "S3cure!Pass", format = "password")
  private String password;
}
