package com.unisantos.clinica_api.anamnesis;

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
@Table(name = "Anamnesis_Complaints")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnamnesisComplaints {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_complaint")
  private Integer id;

  @Column(name = "complaint_name", unique = true, length = 120)
  private String complaintName;
}
