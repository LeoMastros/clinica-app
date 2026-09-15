import type { Role } from '../../types/permissions';

export const ALL_ROLES: Role[] = ['admin', 'psychologist', 'secretary'];

export function isRole(value: string): value is Role {
  return (ALL_ROLES as string[]).includes(value);
}
