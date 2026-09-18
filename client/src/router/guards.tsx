import type { ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';

import Box from '@mui/material/Box';
import CircularProgress from '@mui/material/CircularProgress';

import { ROUTES } from '../constants/routes';
import { useAuth } from '../core/auth';
import { usePermissions } from '../hooks/usePermissions';
import type { Resource } from '../types/permissions';

export function RequireAuth({ children }: { children: ReactNode }) {
  const { isAuthenticated, isInitializing } = useAuth();
  const location = useLocation();

  // Enquanto a sessão guardada está sendo conferida com o servidor, ainda não
  // dá para saber se a pessoa está logada. Redirecionar aqui expulsaria quem
  // recarregou a página com um token perfeitamente válido.
  if (isInitializing) {
    return (
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          minHeight: '100vh',
        }}
        role="status"
        aria-live="polite"
        aria-label="Verificando sessão"
      >
        <CircularProgress />
      </Box>
    );
  }

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
