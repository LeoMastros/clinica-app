package com.unisantos.clinica_api.professional;

import com.unisantos.clinica_api.domain.ProfessionalLevel;
import com.unisantos.clinica_api.domain.Specialty;
import com.unisantos.clinica_api.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "Professional")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Professional {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JdbcTypeCode(SqlTypes.INTEGER)
  @Column(name = "id_professional")
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_user", unique = true)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_professional_level")
  private ProfessionalLevel professionalLevel;

  // Many-to-many via Professional_Specialty (V3) — a psychologist can have
  // multiple specialties.
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "Professional_Specialty",
      joinColumns = @JoinColumn(name = "id_professional"),
      inverseJoinColumns = @JoinColumn(name = "id_specialty"))
  private Set<Specialty> specialties;

  @Column(name = "cpf", columnDefinition = "VARBINARY(512)")
  private byte[] cpf;

  @Column(name = "phone", columnDefinition = "VARBINARY(512)")
  private byte[] phone;

  @Column(name = "birth_date", columnDefinition = "VARBINARY(64)")
  private byte[] birthDate;

  @Column(name = "crp_registration", columnDefinition = "VARBINARY(512)")
  private byte[] crpRegistration;

  @Column(name = "identification_color", length = 7)
  private String identificationColor;
}
