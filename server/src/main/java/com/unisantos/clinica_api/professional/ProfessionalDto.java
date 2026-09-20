package com.unisantos.clinica_api.professional;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalDto {

  private Long id;

  @NotNull private Long userId;

  private String firstName;

  private String lastName;

  private String loginEmail;

  private Boolean isActive;

  private Integer professionalLevelId;

  private String professionalLevel;

  private List<Integer> specialtyIds;

  private List<String> specialties;

  // Zero-Trust: ciphertext as base64. Not decrypted server-side.
  private byte[] cpf;

  private byte[] phone;

  private byte[] birthDate;

  private byte[] crpRegistration;

  private String identificationColor;
}
