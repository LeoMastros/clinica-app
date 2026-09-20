package com.unisantos.clinica_api.security.permissions;

import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Psychologist permissions per permissions-matrix.md. Clinical access is scoped to OWN
 * patients/appointments — row-level ownership is enforced by the services that call this strategy,
 * not here.
 */
@Component
public class PsychologistPermissionStrategy implements PermissionStrategy {

  private static final Set<String> CREATABLE =
      Set.of("triage", "anamnesis", "session", "report", "virtual_room");
  private static final Set<String> READABLE =
      Set.of("patient", "appointment", "triage", "anamnesis", "session", "report", "virtual_room");
  private static final Set<String> UPDATABLE = Set.of("triage", "anamnesis", "session", "report");

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
    return false;
  }

  @Override
  public boolean isPsychologist(Long userId) {
    return true;
  }
}
