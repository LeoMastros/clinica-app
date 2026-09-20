package com.unisantos.clinica_api.patient;

import com.unisantos.clinica_api.domain.Tag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "Patient_Tag",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uq_patient_tag",
          columnNames = {"id_patient", "id_tag"})
    })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientTag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_patient_tag")
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_patient", nullable = false)
  private Patient patient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_tag", nullable = false)
  private Tag tag;
}
