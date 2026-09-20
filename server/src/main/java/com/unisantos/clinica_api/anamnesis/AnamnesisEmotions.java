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
@Table(name = "Anamnesis_Emotions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnamnesisEmotions {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_emotion")
  private Integer id;

  @Column(name = "emotion_name", unique = true, length = 80)
  private String emotionName;
}
