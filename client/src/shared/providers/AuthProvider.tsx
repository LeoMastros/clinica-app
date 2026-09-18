import { useCallback, useEffect, useMemo, useState } from 'react';
import type { ReactNode } from 'react';

import { AuthContext } from '../../core/auth/context';
import { getCurrentUser, login as autenticar, logout as encerrar } from '../../core/auth/service';
import type { Credentials } from '../../core/auth/types';
import type { ApiError } from '../../types/api';
import type { User } from '../../types/user';

function mensagemDaFalha(erro: unknown): string {
  const apiError = erro as Partial<ApiError>;
  if (typeof apiError?.message === 'string' && apiError.message.length > 0) {
    return apiError.message;
  }
  return 'Não foi possível entrar. Tente novamente.';
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isInitializing, setIsInitializing] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Ao abrir o app, confere com o servidor se o token guardado ainda vale.
  // Sem isto, recarregar a página derrubaria a sessão.
  useEffect(() => {
    let cancelado = false;

    getCurrentUser()
      .then(perfil => {
        if (!cancelado) setUser(perfil);
      })
      .finally(() => {
        if (!cancelado) setIsInitializing(false);
      });

    return () => {
      cancelado = true;
    };
  }, []);

  const login = useCallback(async (credentials: Credentials) => {
    setIsLoading(true);
    setError(null);
    try {
      setUser(await autenticar(credentials));
    } catch (erro) {
      // A falha vira estado para o formulário exibir. Propagar aqui deixaria
      // uma promise rejeitada sem dono, já que o submit não a aguarda.
      setUser(null);
      setError(mensagemDaFalha(erro));
    } finally {
      setIsLoading(false);
    }
  }, []);

  const logout = useCallback(() => {
    void encerrar();
    setUser(null);
    setError(null);
  }, []);

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: user !== null,
      isLoading,
      isInitializing,
      error,
      login,
      logout,
    }),
    [user, isLoading, isInitializing, error, login, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
