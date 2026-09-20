package com.unisantos.clinica_api.triage;

import com.unisantos.clinica_api.common.factory.EntityFactory;
import org.springframework.stereotype.Component;

@Component
public class TriageFactory implements EntityFactory<Triage, TriageDto> {

  @Override
  public Triage create(TriageDto dto) {
    return null;
  }
}
