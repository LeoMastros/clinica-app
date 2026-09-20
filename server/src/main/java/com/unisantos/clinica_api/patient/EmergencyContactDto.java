package com.unisantos.clinica_api.patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactDto {

  private Integer id;
  private byte[] name;
  private byte[] relationship;
  private byte[] phone;
  private boolean isPrimary;
}
