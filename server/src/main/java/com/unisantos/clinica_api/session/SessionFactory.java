package com.unisantos.clinica_api.session;

import com.unisantos.clinica_api.common.factory.EntityFactory;
import org.springframework.stereotype.Component;

@Component
public class SessionFactory implements EntityFactory<TherapySession, SessionDto> {

  @Override
  public TherapySession create(SessionDto dto) {
    return null;
  }
}
