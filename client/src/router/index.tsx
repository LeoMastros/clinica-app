import { RouterProvider } from 'react-router-dom';

import { router } from './routes';

export function AppRouter() {
  return <RouterProvider router={router} />;
}

export { router } from './routes';
export { RequireAuth, RequirePermission } from './guards';
export { firstAllowedRoute } from './navigation';
