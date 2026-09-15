import { useContext } from 'react';

import { PermissionsContext } from '../shared/providers/PermissionsProvider';
import type { PermissionsContextValue } from '../shared/providers/PermissionsProvider';

export function usePermissions(): PermissionsContextValue {
  const context = useContext(PermissionsContext);
  if (!context) {
    throw new Error('usePermissions must be used within a PermissionsProvider');
  }
  return context;
}
