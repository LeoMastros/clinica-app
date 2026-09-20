package com.unisantos.clinica_api.common.factory;

public interface EntityFactory<T, D> {

  T create(D dto);
}
