package com.unisantos.clinica_api.common.factory;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class EntityFactoryProvider {

  private final Map<Class<?>, EntityFactory<?, ?>> factories = new HashMap<>();

  public <T, D> void registerFactory(Class<T> entityClass, EntityFactory<T, D> factory) {
    factories.put(entityClass, factory);
  }

  @SuppressWarnings("unchecked")
  public <T, D> EntityFactory<T, D> getFactory(Class<T> entityClass) {
    EntityFactory<?, ?> factory = factories.get(entityClass);
    if (factory == null) {
      throw new IllegalArgumentException("No factory registered for " + entityClass.getName());
    }
    return (EntityFactory<T, D>) factory;
  }
}
