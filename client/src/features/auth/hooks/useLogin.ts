import { useAuth } from '../../../core/auth';

export function useLogin() {
  const { login, isLoading, error } = useAuth();
  return { login, isLoading, error };
}
