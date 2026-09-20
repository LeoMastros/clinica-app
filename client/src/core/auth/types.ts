import type { User } from '../../types/user';

export interface Credentials {
  email: string;
  password: string;
}

export interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  /** A request is in flight (login, or the initial session restore). */
  isLoading: boolean;
  /** True only while the boot-time session restore is unresolved. */
  isRestoring: boolean;
}

export interface AuthContextValue extends AuthState {
  login: (credentials: Credentials) => Promise<void>;
  logout: () => void;
}
