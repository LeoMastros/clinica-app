package com.unisantos.clinica_api.audit;

import com.unisantos.clinica_api.common.factory.EntityFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditFactory implements EntityFactory<AuditLog, AuditDto> {

  @Override
  public AuditLog create(AuditDto dto) {
    return null;
  }
}
