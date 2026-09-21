package com.unisantos.clinica_api.patient;

import com.unisantos.clinica_api.domain.EducationLevel;
import com.unisantos.clinica_api.domain.Ethnicity;
import com.unisantos.clinica_api.domain.Gender;
import com.unisantos.clinica_api.domain.PatientStatus;
import com.unisantos.clinica_api.professional.Professional;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "Patient")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Patient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JdbcTypeCode(SqlTypes.INTEGER)
  @Column(name = "id_patient")
  private Long id;

  @JdbcTypeCode(SqlTypes.INTEGER)
  @Column(name = "id_professional")
  private Long professionalId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_professional", insertable = false, updatable = false)
  private Professional professional;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_patient_status")
  private PatientStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_gender")
  private Gender gender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_education_level")
  private EducationLevel educationLevel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_ethnicity")
  private Ethnicity ethnicity;

  @Column(name = "first_name", columnDefinition = "VARBINARY(255)")
  private byte[] firstName;

  @Column(name = "last_name", columnDefinition = "VARBINARY(255)")
  private byte[] lastName;

  @Column(name = "social_name", columnDefinition = "VARBINARY(255)")
  private byte[] socialName;

  @Column(name = "contact_email", columnDefinition = "VARBINARY(255)")
  private byte[] contactEmail;

  @Column(name = "mobile_phone", columnDefinition = "VARBINARY(255)")
  private byte[] mobilePhone;

  @Column(name = "landline_phone", columnDefinition = "VARBINARY(255)")
  private byte[] landlinePhone;

  @Column(name = "cpf", columnDefinition = "VARBINARY(512)")
  private byte[] cpf;

  @Column(name = "rg", columnDefinition = "VARBINARY(512)")
  private byte[] rg;

  @Column(name = "birth_date", columnDefinition = "VARBINARY(64)")
  private byte[] birthDate;

  @Column(name = "place_of_birth", columnDefinition = "VARBINARY(120)")
  private byte[] placeOfBirth;

  @Column(name = "occupation", columnDefinition = "VARBINARY(120)")
  private byte[] occupation;

  @Column(name = "how_they_found_us", columnDefinition = "VARBINARY(120)")
  private byte[] howTheyFoundUs;

  @Column(name = "referred_by", columnDefinition = "VARBINARY(120)")
  private byte[] referredBy;

  @Column(name = "identification_color", length = 7)
  private String identificationColor;

  @Column(name = "notes", columnDefinition = "BLOB")
  private byte[] notes;

  @CreatedDate
  @Column(name = "registration_date", updatable = false)
  private LocalDateTime registeredAt;

  @JdbcTypeCode(SqlTypes.INTEGER)
  @Column(name = "registered_by_user")
  private Long registeredByUser;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private PatientAddress address;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private PatientGuardian guardian;

  @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<EmergencyContact> emergencyContacts;

  @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<PatientTag> patientTags;
}
