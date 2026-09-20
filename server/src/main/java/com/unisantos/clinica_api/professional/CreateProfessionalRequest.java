package com.unisantos.clinica_api.professional;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProfessionalRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  @Schema(description = "Institutional e-mail", example = "carlos.ramos@clinica.com")
  private String email;

  @NotBlank(message = "Initial password is required")
  @Schema(description = "Initial password", example = "Temp1234!", format = "password")
  private String password;

  @NotBlank(message = "First name is required")
  @Schema(example = "Carlos")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Schema(example = "Ramos")
  private String lastName;

  @Schema(
      description = "Professional level id — see GET /api/v1/lookups/professional-levels",
      example = "1")
  private Integer professionalLevelId;

  @NotEmpty(message = "At least one specialty is required")
  @Schema(description = "Specialty ids — see GET /api/v1/lookups/specialties", example = "[1, 2]")
  private List<Integer> specialtyIds;

  // Zero-Trust: personal data fields arrive as strings; until the client-side
  // crypto module lands they are stored as UTF-8 bytes in the VARBINARY
  // columns. Later these same fields carry base64 ciphertext.
  @Schema(example = "123.456.789-00")
  private String cpf;

  @Schema(example = "(11) 98888-7777")
  private String phone;

  @Schema(description = "ISO date", example = "1992-03-15")
  private String birthDate;

  @Schema(description = "Professional registration number", example = "06/12345")
  private String crpRegistration;

  @Schema(description = "UI color metadata, e.g. #RRGGBB — not personal data", example = "#3F51B5")
  private String identificationColor;
}
