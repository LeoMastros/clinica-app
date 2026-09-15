import type { ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';

import { ROUTES } from '../constants/routes';
import { useAuth } from '../core/auth';
import { usePermissions } from '../hooks/usePermissions';
import type { Resource } from '../types/permissions';

export function RequireAuth({ children }: { children: ReactNode }) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    return (
      <Navigate to={ROUTES.login} state={{ from: location.pathname }} replace />
    );
  }
  return <>{children}</>;
}

export function RequirePermission({
  resource,
  children,
}: {
  resource: Resource;
  children: ReactNode;
}) {
  const { can } = usePermissions();

  if (!can('view', resource)) {
    return <Navigate to={ROUTES.forbidden} replace />;
  }
  return <>{children}</>;
}
