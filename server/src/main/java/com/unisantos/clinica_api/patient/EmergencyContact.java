package com.unisantos.clinica_api.patient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Emergency_Contact")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContact {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_emergency_contact")
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_patient", nullable = false)
  private Patient patient;

  @Column(name = "name", columnDefinition = "VARBINARY(255)", nullable = false)
  private byte[] name;

  @Column(name = "relationship", columnDefinition = "VARBINARY(60)")
  private byte[] relationship;

  @Column(name = "phone", columnDefinition = "VARBINARY(255)", nullable = false)
  private byte[] phone;

  @Column(name = "is_primary")
  private boolean isPrimary;
}
