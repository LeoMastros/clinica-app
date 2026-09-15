import { useCallback, useMemo, useState } from 'react';
import type { ReactNode } from 'react';

import { AuthContext } from '../../core/auth/context';
import type { Credentials } from '../../core/auth/types';
import type { Role } from '../../types/permissions';
import type { User } from '../../types/user';

/**
 * Mock authentication used until the backend is available: any password is
 * accepted and the role is derived from the email prefix so every role can be
 * exercised (admin@, psicologo@, secretaria@).
 */
const MOCK_USERS: Record<Role, User> = {
  admin: {
    id: 'u-admin',
    name: 'Administrador',
    email: 'admin@clinica.test',
    role: 'admin',
    active: true,
  },
  psychologist: {
    id: 'u-psi',
    name: 'Psicóloga Demo',
    email: 'psicologo@clinica.test',
    role: 'psychologist',
    active: true,
  },
  secretary: {
    id: 'u-sec',
    name: 'Secretaria Demo',
    email: 'secretaria@clinica.test',
    role: 'secretary',
    active: true,
  },
};

function roleFromEmail(email: string): Role {
  const prefix = email.split('@')[0]?.toLowerCase() ?? '';
  if (prefix.startsWith('psic')) return 'psychologist';
  if (prefix.startsWith('secret')) return 'secretary';
  return 'admin';
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const login = useCallback(async ({ email }: Credentials) => {
    setIsLoading(true);
    try {
      const role = roleFromEmail(email);
      setUser({ ...MOCK_USERS[role], email });
    } finally {
      setIsLoading(false);
    }
  }, []);

  const logout = useCallback(() => setUser(null), []);

  const value = useMemo(
    () => ({ user, isAuthenticated: user !== null, isLoading, login, logout }),
    [user, isLoading, login, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
