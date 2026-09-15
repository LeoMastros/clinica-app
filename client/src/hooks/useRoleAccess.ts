import { useAuth } from '../core/auth';
import type { Resource } from '../types/permissions';
import { usePermissions } from './usePermissions';

export function useRoleAccess() {
  const { user } = useAuth();
  const { can } = usePermissions();
  return {
    role: user?.role,
    canView: (resource: Resource) => can('view', resource),
  };
}
