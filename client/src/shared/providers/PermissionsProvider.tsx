import { createContext, useMemo } from 'react';
import type { ReactNode } from 'react';

import { useAuth } from '../../core/auth';
import { can as checkPermission } from '../../core/permissions';
import type { Action, Resource } from '../../types/permissions';

export interface PermissionsContextValue {
  can: (action: Action, resource: Resource) => boolean;
}

export const PermissionsContext = createContext<
  PermissionsContextValue | undefined
>(undefined);

export function PermissionsProvider({ children }: { children: ReactNode }) {
  const { user } = useAuth();

  const value = useMemo<PermissionsContextValue>(
    () => ({
      can: (action, resource) => checkPermission(user?.role, action, resource),
    }),
    [user?.role]
  );

  return (
    <PermissionsContext.Provider value={value}>
      {children}
    </PermissionsContext.Provider>
  );
}
