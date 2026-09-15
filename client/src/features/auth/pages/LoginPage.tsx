import { Navigate } from 'react-router-dom';

import { useAuth } from '../../../core/auth';
import { AuthCard } from '../components/AuthCard';
import { LoginForm } from '../components/LoginForm';

export function LoginPage() {
  const { isAuthenticated } = useAuth();
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
