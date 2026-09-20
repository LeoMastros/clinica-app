import { Navigate } from 'react-router-dom';

import { useAuth } from '../../../core/auth';
import { AuthCard } from '../components/AuthCard';
import { LoginForm } from '../components/LoginForm';

export function LoginPage() {
  const { isAuthenticated, isRestoring } = useAuth();
  // While the session is being restored from the refresh cookie, render
  // nothing — an authenticated user is bounced to '/' and must never see
  // the login form flash.
  if (isRestoring) return null;
  if (isAuthenticated) return <Navigate to="/" replace />;

  return (
    <AuthCard
      title="Entrar no PsiUnisantos"
      subtitle="Acesse a gestão da clínica escola de psicologia"
    >
      <LoginForm />
    </AuthCard>
  );
}
