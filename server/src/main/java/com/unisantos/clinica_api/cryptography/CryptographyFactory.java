package com.unisantos.clinica_api.cryptography;

import com.unisantos.clinica_api.common.factory.EntityFactory;
import org.springframework.stereotype.Component;

@Component
public class CryptographyFactory implements EntityFactory<KeyReference, CryptographyDto> {

  @Override
  public KeyReference create(CryptographyDto dto) {
    return null;
  }
}
