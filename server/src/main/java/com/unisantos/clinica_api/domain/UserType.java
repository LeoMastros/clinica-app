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
@Table(name = "User_Type")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_user_type")
  private Integer id;

  @Column(name = "type_name", unique = true, length = 30)
  private String typeName;
}
