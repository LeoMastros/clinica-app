package com.unisantos.clinica_api.professional;

import com.unisantos.clinica_api.domain.TargetAudience;
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
    name = "Professional_Target_Audience",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uq_professional_audience",
            columnNames = {"id_professional", "id_audience"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalTargetAudience {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_professional_audience")
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_professional")
  private Professional professional;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_audience")
  private TargetAudience targetAudience;
}
