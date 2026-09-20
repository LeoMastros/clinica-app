package com.unisantos.clinica_api.anamnesis;

import com.unisantos.clinica_api.common.factory.EntityFactory;
import org.springframework.stereotype.Component;

@Component
public class AnamnesisFactory implements EntityFactory<Anamnesis, AnamnesisDto> {

  @Override
  public Anamnesis create(AnamnesisDto dto) {
    return null;
  }
}
