package com.unisantos.clinica_api.patient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Patient_Guardian")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientGuardian {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_guardian")
  private Integer id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_patient", unique = true)
  private Patient patient;

  @Column(name = "guardian_name", columnDefinition = "VARBINARY(255)", nullable = false)
  private byte[] guardianName;

  @Column(name = "guardian_email", columnDefinition = "VARBINARY(255)")
  private byte[] guardianEmail;

  @Column(name = "guardian_mobile_phone", columnDefinition = "VARBINARY(255)")
  private byte[] guardianMobilePhone;

  @Column(name = "guardian_cpf", columnDefinition = "VARBINARY(512)")
  private byte[] guardianCpf;

  @Column(name = "guardian_rg", columnDefinition = "VARBINARY(512)")
  private byte[] guardianRg;

  @Column(name = "guardian_birth_date", columnDefinition = "VARBINARY(64)")
  private byte[] guardianBirthDate;

  @Column(name = "allow_document_billing")
  private boolean allowDocumentBilling;

  @Column(name = "allow_session_reminders")
  private boolean allowSessionReminders;
}
