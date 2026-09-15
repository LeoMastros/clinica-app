import type { PermissionMatrix } from '../../types/permissions';

/**
 * Single source of truth for role-based access, mirroring the Role-Based
 * Permission Matrix of CLINIC_APP_STRUCTURE_PLAN.md. No data can ever be
 * deleted, so no role holds a `delete` action.
 */
export const PERMISSION_MATRIX: PermissionMatrix = {
  admin: {
    appointment: ['view', 'create', 'edit', 'cancel'],
    triage: ['view', 'create', 'edit'],
    anamnesis: ['view', 'create', 'edit'],
    session: ['view', 'create', 'edit', 'cancel'],
    report: ['view', 'create', 'edit'],
    patient: ['view', 'create', 'edit', 'assign_psychologist', 'edit_status'],
    user: ['view', 'create', 'edit', 'reset_password'],
    cryptoKey: ['generate'],
  },
  psychologist: {
    appointment: ['view'],
    triage: ['view', 'create', 'edit'],
    anamnesis: ['view', 'create', 'edit'],
    session: ['view', 'create', 'edit', 'cancel'],
    report: ['view', 'create', 'edit'],
    patient: ['view'],
  },
  secretary: {
    appointment: ['view', 'create', 'edit', 'cancel'],
    session: ['view'],
    report: ['view'],
    patient: ['view', 'create', 'edit', 'edit_status'],
  },
};
