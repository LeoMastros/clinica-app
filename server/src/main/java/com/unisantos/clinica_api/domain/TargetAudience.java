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
@Table(name = "Target_Audience")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TargetAudience {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_audience")
  private Integer id;

  @Column(name = "audience_name", unique = true, length = 60)
  private String audienceName;
}
