package com.unisantos.clinica_api.security.permissions;

public interface PermissionStrategy {

  boolean canCreate(String resource, Long userId);

  boolean canRead(String resource, Long userId, Long resourceId);

  boolean canUpdate(String resource, Long userId, Long resourceId);

  boolean canDelete(String resource, Long userId, Long resourceId);

  boolean isCoordinator(Long userId);

  boolean isSecretary(Long userId);

  boolean isPsychologist(Long userId);
}
