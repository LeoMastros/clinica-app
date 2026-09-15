import type { Role } from '../types/permissions';

export const ROLES: Record<Uppercase<Role>, Role> = {
  ADMIN: 'admin',
  PSYCHOLOGIST: 'psychologist',
  SECRETARY: 'secretary',
};

export const ROLE_LABELS: Record<Role, string> = {
  admin: 'Administrador',
  psychologist: 'Psicólogo(a)',
  secretary: 'Secretaria',
};
