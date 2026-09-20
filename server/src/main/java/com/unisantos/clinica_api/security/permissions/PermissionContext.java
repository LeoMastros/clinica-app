package com.unisantos.clinica_api.security.permissions;

import org.springframework.stereotype.Component;

@Component
public class PermissionContext {

  private final PermissionService permissionService;

  public PermissionContext(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  public boolean checkPermission(String resource, String action, Long userId, Long resourceId) {
    return switch (action.toLowerCase()) {
      case "create" -> permissionService.canCreate(resource, userId);
      case "read" -> permissionService.canRead(resource, userId, resourceId);
      case "update" -> permissionService.canUpdate(resource, userId, resourceId);
      case "delete" -> permissionService.canDelete(resource, userId, resourceId);
      default -> false;
    };
  }
}
