import { Navigate } from 'react-router-dom';

import { useAuth } from '../core/auth';
import { firstAllowedRoute } from './navigation';

export function HomeRedirect() {
  const { user } = useAuth();
  return <Navigate to={firstAllowedRoute(user?.role)} replace />;
}
