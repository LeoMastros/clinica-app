import type { Action, Resource, Role } from '../../types/permissions';
import { PERMISSION_MATRIX } from './accessControl';

export function can(
  role: Role | undefined,
  action: Action,
  resource: Resource
): boolean {
  if (!role) return false;
  return PERMISSION_MATRIX[role][resource]?.includes(action) ?? false;
}

export function canAccessResource(
  role: Role | undefined,
  resource: Resource
): boolean {
  return can(role, 'view', resource);
}

export function canAssignPsychologist(role: Role | undefined): boolean {
  return can(role, 'assign_psychologist', 'patient');
}

export function canEditPatientStatus(role: Role | undefined): boolean {
  return can(role, 'edit_status', 'patient');
}

export function canManageUsers(role: Role | undefined): boolean {
  return can(role, 'edit', 'user');
}

export function canGenerateCryptoKeys(role: Role | undefined): boolean {
  return can(role, 'generate', 'cryptoKey');
}
