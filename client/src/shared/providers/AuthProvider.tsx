import { useCallback, useEffect, useMemo, useState } from 'react';
import type { ReactNode } from 'react';

import { AuthContext } from '../../core/auth/context';
import {
  login as apiLogin,
  logout as apiLogout,
  getCurrentUser,
  refreshSession,
} from '../../core/auth/service';
import type { Credentials } from '../../core/auth/types';
import type { User } from '../../types/user';

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  // Session restore: the access token lives in memory and is lost on reload,
  // but the httpOnly refresh cookie survives — exchange it, then load /me.
  // Guards wait on this before deciding anything.
  const [isRestoring, setIsRestoring] = useState(true);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        await refreshSession();
        const restored = await getCurrentUser();
        if (!cancelled) setUser(restored);
      } catch {
        if (!cancelled) setUser(null);
      } finally {
        if (!cancelled) setIsRestoring(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  const login = useCallback(async (credentials: Credentials) => {
    setIsLoading(true);
    try {
      setUser(await apiLogin(credentials));
    } finally {
      setIsLoading(false);
    }
  }, []);

  const logout = useCallback(() => {
    void apiLogout();
    setUser(null);
  }, []);

  const value = useMemo(
    () => ({
      user,
      isAuthenticated: user !== null,
      isLoading,
      isRestoring,
      login,
      logout,
    }),
    [user, isLoading, isRestoring, login, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
