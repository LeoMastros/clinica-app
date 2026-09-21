package com.unisantos.clinica_api.security.permissions;

import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Secretary permissions per permissions-matrix.md: administrative and front-desk operations only.
 * Zero access to clinical modules (triage, anamnesis, sessions, reports, virtual room).
 */
@Component
public class SecretaryPermissionStrategy implements PermissionStrategy {

  private static final Set<String> CREATABLE = Set.of("patient", "appointment", "professional");
  private static final Set<String> READABLE = Set.of("patient", "appointment", "professional");
  private static final Set<String> UPDATABLE = Set.of("patient", "appointment");

  @Override
  public boolean canCreate(String resource, Long userId) {
    return CREATABLE.contains(resource);
  }

  @Override
  public boolean canRead(String resource, Long userId, Long resourceId) {
    return READABLE.contains(resource);
  }

  @Override
  public boolean canUpdate(String resource, Long userId, Long resourceId) {
    return UPDATABLE.contains(resource);
  }

  @Override
  public boolean canDelete(String resource, Long userId, Long resourceId) {
    return false;
  }

  @Override
  public boolean isCoordinator(Long userId) {
    return false;
  }

  @Override
  public boolean isSecretary(Long userId) {
    return true;
  }

  @Override
  public boolean isPsychologist(Long userId) {
    return false;
  }
}
