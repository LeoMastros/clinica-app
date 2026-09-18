import type { User } from '../../types/user';
import { apiClient } from '../api/client';
import { ENDPOINTS } from '../api/endpoints';
import { clearToken, readToken, storeToken } from '../api/interceptors';
import type { AuthResponse, Credentials } from './types';

/**
 * Autentica e guarda o token para as próximas requisições.
 *
 * <p>Erros sobem como `ApiError`, já traduzidos pelo interceptador — o
 * formulário mostra a mensagem como veio do servidor.
 */
export async function login(credentials: Credentials): Promise<User> {
  const { data } = await apiClient.post<AuthResponse>(
    ENDPOINTS.auth.login,
    credentials
  );
  storeToken(data.token);
  return data.user;
}

export async function logout(): Promise<void> {
  clearToken();
}

/**
 * Recupera o perfil de quem está com a sessão aberta, usado para reidratar o
 * estado quando a página é recarregada.
 *
 * <p>Devolve `null` em vez de lançar quando não há sessão válida: para quem
 * chama, "não está logado" é uma resposta esperada, não um erro. Um token
 * recusado é descartado aqui mesmo, para não ficar tentando a cada recarga.
 */
export async function getCurrentUser(): Promise<User | null> {
  if (!readToken()) return null;

  try {
    const { data } = await apiClient.get<User>(ENDPOINTS.auth.me);
    return data;
  } catch {
    clearToken();
    return null;
  }
}
