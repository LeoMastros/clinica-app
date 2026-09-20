package com.unisantos.clinica_api.patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientGuardianDto {

  private Integer id;
  private byte[] guardianName;
  private byte[] guardianEmail;
  private byte[] guardianMobilePhone;
  private byte[] guardianCpf;
  private byte[] guardianRg;
  private byte[] guardianBirthDate;
  private boolean allowDocumentBilling;
  private boolean allowSessionReminders;
}
