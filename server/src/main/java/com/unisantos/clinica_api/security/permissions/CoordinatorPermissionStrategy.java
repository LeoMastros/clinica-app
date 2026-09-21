package com.unisantos.clinica_api.security.permissions;

import org.springframework.stereotype.Component;

/**
 * Coordinator (Admin) permissions per permissions-matrix.md: full access to everything except
 * deletion, which is forbidden system-wide by the CFP/LGPD no-deletion data retention policy.
 */
@Component
public class CoordinatorPermissionStrategy implements PermissionStrategy {

  @Override
  public boolean canCreate(String resource, Long userId) {
    return true;
  }

  @Override
  public boolean canRead(String resource, Long userId, Long resourceId) {
    return true;
  }

  @Override
  public boolean canUpdate(String resource, Long userId, Long resourceId) {
    return true;
  }

  @Override
  public boolean canDelete(String resource, Long userId, Long resourceId) {
    return false;
  }

  @Override
  public boolean isCoordinator(Long userId) {
    return true;
  }

  @Override
  public boolean isSecretary(Long userId) {
    return false;
  }

  @Override
  public boolean isPsychologist(Long userId) {
    return false;
  }
}
