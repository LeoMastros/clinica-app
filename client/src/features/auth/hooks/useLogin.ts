import { useAuth } from '../../../core/auth';

export function useLogin() {
  // TODO: Replace with a React Query mutation calling authService.login once the API exists
  const { login, isLoading } = useAuth();
  return { login, isLoading };
}
