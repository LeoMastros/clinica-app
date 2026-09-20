import type { Role } from '../../types/permissions';
import type { User } from '../../types/user';
import apiClient, { clearAccessToken, setAccessToken } from '../api/client';
import { ENDPOINTS } from '../api/endpoints';
import type { Credentials } from './types';

type UserTypeName = 'COORDINATOR' | 'SECRETARY' | 'PROFESSIONAL';

interface LoginPayload {
  accessToken: string;
  email: string;
  userId: number;
  userType: UserTypeName;
}

interface MePayload {
  id: number;
  firstName: string | null;
  lastName: string | null;
  loginEmail: string;
  userType: UserTypeName;
  isActive: boolean;
}

const ROLE_MAP: Record<UserTypeName, Role> = {
  COORDINATOR: 'admin',
  SECRETARY: 'secretary',
  PROFESSIONAL: 'psychologist',
};

export async function login(credentials: Credentials): Promise<User> {
  const { data } = await apiClient.post<{ data: LoginPayload }>(
    ENDPOINTS.auth.login,
    { email: credentials.email, password: credentials.password }
  );
  const payload = data.data;
  setAccessToken(payload.accessToken);
  return {
    id: String(payload.userId),
    name: payload.email.split('@')[0] ?? payload.email,
    email: payload.email,
    role: ROLE_MAP[payload.userType] ?? 'secretary',
    active: true,
  };
}

export async function logout(): Promise<void> {
  try {
    await apiClient.post(ENDPOINTS.auth.logout);
  } finally {
    clearAccessToken();
  }
}

/** Exchanges the httpOnly refresh cookie for a fresh access token. */
export async function refreshSession(): Promise<string> {
  const { data } = await apiClient.post<{ data: { accessToken: string } }>(
    ENDPOINTS.auth.refresh
  );
  setAccessToken(data.data.accessToken);
  return data.data.accessToken;
}

/** GET /auth/me — requires a valid access token (call refreshSession first). */
export async function getCurrentUser(): Promise<User | null> {
  try {
    const { data } = await apiClient.get<{ data: MePayload }>(
      ENDPOINTS.auth.me
    );
    const payload = data.data;
    const name =
      [payload.firstName, payload.lastName].filter(Boolean).join(' ') ||
      payload.loginEmail.split('@')[0] ||
      payload.loginEmail;
    return {
      id: String(payload.id),
      name,
      email: payload.loginEmail,
      role: ROLE_MAP[payload.userType] ?? 'secretary',
      active: payload.isActive,
    };
  } catch {
    return null;
  }
}
