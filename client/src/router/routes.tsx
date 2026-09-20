import type { ReactElement } from 'react';
import { Navigate, createBrowserRouter } from 'react-router-dom';

import { ROUTES } from '../constants/routes';
import { AnamnesisListPage } from '../features/anamnesis';
import { SchedulerPage } from '../features/appointments';
import { LoginPage } from '../features/auth';
import { PatientListPage } from '../features/patients';
import { ReportsDashboardPage } from '../features/reports';
import { SessionListPage } from '../features/sessions';
import { TriageListPage } from '../features/triage';
import { UserListPage } from '../features/users';
import { AuthLayout } from '../layout/AuthLayout';
import { MainLayout } from '../layout/MainLayout';
import { ForbiddenPage } from '../shared/templates/ErrorPage';
import type { Resource } from '../types/permissions';
import { HomeRedirect } from './HomeRedirect';
import { RequireAuth, RequirePermission } from './guards';

function protectedRoute(resource: Resource, element: ReactElement) {
  return <RequirePermission resource={resource}>{element}</RequirePermission>;
}

export const router = createBrowserRouter([
  {
    element: <AuthLayout />,
    children: [{ path: ROUTES.login, element: <LoginPage /> }],
  },
  {
    element: (
      <RequireAuth>
        <MainLayout />
      </RequireAuth>
    ),
    children: [
      { path: '/', element: <HomeRedirect /> },
      {
        path: ROUTES.appointments,
        element: protectedRoute('appointment', <SchedulerPage />),
      },
      {
        path: ROUTES.triage,
        element: protectedRoute('triage', <TriageListPage />),
      },
      {
        path: ROUTES.anamnesis,
        element: protectedRoute('anamnesis', <AnamnesisListPage />),
      },
      {
        path: ROUTES.sessions,
        element: protectedRoute('session', <SessionListPage />),
      },
      {
        path: ROUTES.reports,
        element: protectedRoute('report', <ReportsDashboardPage />),
      },
      {
        path: ROUTES.patients,
        element: protectedRoute('patient', <PatientListPage />),
      },
      { path: ROUTES.users, element: protectedRoute('user', <UserListPage />) },
      { path: ROUTES.forbidden, element: <ForbiddenPage /> },
    ],
  },
  { path: '*', element: <Navigate to="/" replace /> },
]);
