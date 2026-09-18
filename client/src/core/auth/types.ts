import type { User } from '../../types/user';

export interface Credentials {
  email: string;
  password: string;
}

/** Corpo devolvido por `POST /auth/login`. */
export interface AuthResponse {
  token: string;
  user: User;
}

export interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  /** Uma tentativa de login está em andamento. */
  isLoading: boolean;
  /**
   * A sessão guardada ainda está sendo conferida com o servidor. Enquanto for
   * verdadeiro, `isAuthenticated` ser falso não significa que o usuário está
   * de fora — as rotas protegidas precisam esperar em vez de redirecionar.
   */
  isInitializing: boolean;
  /** Mensagem da última falha de login, pronta para exibir. */
  error: string | null;
}

export interface AuthContextValue extends AuthState {
  login: (credentials: Credentials) => Promise<void>;
  logout: () => void;
}
