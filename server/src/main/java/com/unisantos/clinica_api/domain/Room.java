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
@Table(name = "Room")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_room")
  private Integer id;

  @Column(name = "room_name", unique = true, length = 60)
  private String roomName;

  @Column(name = "is_active")
  private boolean isActive;
}
