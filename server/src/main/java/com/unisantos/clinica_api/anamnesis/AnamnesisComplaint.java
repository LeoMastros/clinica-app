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
@Table(name = "Anamnesis_Complaint")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnamnesisComplaint {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_anamnesis_complaint")
  private Integer id;
}
