import { AuthCard } from '../components/AuthCard';
import { SignupForm } from '../components/SignupForm';

export function SignupPage() {
  // TODO: Wire to authService.signup once the backend contract exists
  return (
    <AuthCard title="Criar conta">
      <SignupForm />
    </AuthCard>
  );
}
