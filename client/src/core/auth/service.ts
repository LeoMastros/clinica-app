import type { User } from '../../types/user';
import type { Credentials } from './types';

export async function login(_credentials: Credentials): Promise<User> {
  // TODO: POST ENDPOINTS.auth.login through apiClient with the credentials
  // TODO: Store the returned token under AUTH_TOKEN_STORAGE_KEY
  // TODO: Return the authenticated user profile
  // NO FUNCTIONAL CODE - Implementation guide only
  throw new Error('not implemented');
}

export async function logout(): Promise<void> {
  // TODO: Clear AUTH_TOKEN_STORAGE_KEY and any cached crypto keys
  // NO FUNCTIONAL CODE - Implementation guide only
}

export async function getCurrentUser(): Promise<User | null> {
  // TODO: GET ENDPOINTS.auth.me and map the payload to User
  // NO FUNCTIONAL CODE - Implementation guide only
  return null;
}
