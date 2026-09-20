package com.unisantos.clinica_api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  @Schema(description = "Institutional e-mail", example = "maria.souza@clinica.com")
  private String email;

  @NotBlank(message = "Initial password is required")
  @Schema(
      description = "Initial password — the user can change it after first login",
      example = "Temp1234!",
      format = "password")
  private String password;

  @NotBlank(message = "Role is required")
  @Pattern(regexp = "SECRETARY|PROFESSIONAL", message = "Role must be SECRETARY or PROFESSIONAL")
  @Schema(
      description = "Account role. COORDINATOR cannot be created (single seeded account).",
      example = "SECRETARY",
      allowableValues = {"SECRETARY", "PROFESSIONAL"})
  private String role;

  @NotBlank(message = "First name is required")
  @Schema(example = "Maria")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Schema(example = "Souza")
  private String lastName;
}
