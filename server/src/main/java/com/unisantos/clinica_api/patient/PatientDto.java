package com.unisantos.clinica_api.patient;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {

  private Long id;
  private Long professionalId;
  private Integer patientStatusId;
  private Integer genderId;
  private Integer educationLevelId;
  private Integer ethnicityId;
  private byte[] firstName;
  private byte[] lastName;
  private byte[] socialName;
  private byte[] contactEmail;
  private byte[] mobilePhone;
  private byte[] landlinePhone;
  private byte[] cpf;
  private byte[] rg;
  private byte[] birthDate;
  private byte[] placeOfBirth;
  private byte[] occupation;
  private byte[] howTheyFoundUs;
  private byte[] referredBy;
  private String identificationColor;
  private byte[] notes;
  private PatientAddressDto address;
  private PatientGuardianDto guardian;
  private List<EmergencyContactDto> emergencyContacts;
  private List<Integer> tagIds;
}
