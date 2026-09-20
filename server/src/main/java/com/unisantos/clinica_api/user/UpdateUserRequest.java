package com.unisantos.clinica_api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Coordinator-editable profile fields. Role and password are not editable here. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

  @NotBlank(message = "First name is required")
  @Schema(example = "Maria")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Schema(example = "Souza")
  private String lastName;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  @Schema(example = "maria.souza@clinica.com")
  private String email;
}
