package com.unisantos.clinica_api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Patient_Status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientStatus {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_patient_status")
  private Integer id;

  @Column(name = "status_name", unique = true, length = 30)
  private String statusName;
}
