import type { AxiosError, AxiosInstance } from 'axios';

import { AUTH_TOKEN_STORAGE_KEY } from '../../constants/api';
import { ROUTES } from '../../constants/routes';
import type { ApiError } from '../../types/api';

/**
 * O acesso ao localStorage é envolvido em try/catch porque ele lança em aba
 * anônima e com dados de site bloqueados. Sem sessão guardada o app ainda
 * funciona: o usuário só precisa entrar de novo.
 */
export function readToken(): string | null {
  try {
    return localStorage.getItem(AUTH_TOKEN_STORAGE_KEY);
  } catch {
    return null;
  }
}

export function storeToken(token: string): void {
  try {
    localStorage.setItem(AUTH_TOKEN_STORAGE_KEY, token);
  } catch {
    // Sessão vale só para esta aba; não é motivo para interromper o login.
  }
}

export function clearToken(): void {
  try {
    localStorage.removeItem(AUTH_TOKEN_STORAGE_KEY);
  } catch {
    // Nada a limpar se o storage não está disponível.
  }
}

/** Corpo de erro do servidor: o Spring devolve `ProblemDetail` (RFC 7807). */
interface ProblemDetail {
  title?: string;
  detail?: string;
  fields?: Record<string, string>;
}

function mensagemDoErro(error: AxiosError<ProblemDetail>): string {
  const corpo = error.response?.data;
  if (corpo?.detail) return corpo.detail;
  if (corpo?.title) return corpo.title;
  if (error.code === 'ECONNABORTED') return 'O servidor demorou para responder.';
  if (!error.response) return 'Não foi possível falar com o servidor.';
  return 'Não foi possível concluir a operação.';
}

/**
 * Instala os interceptadores na instância compartilhada do axios.
 *
 * <p>A descriptografia de payload mencionada no plano original não entra aqui:
 * o módulo de criptografia ficou com outra pessoa, e o formato do envelope
 * ainda não está definido.
 */
export function setupInterceptors(client: AxiosInstance): void {
  client.interceptors.request.use(config => {
    const token = readToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });

  client.interceptors.response.use(
    response => response,
    (error: AxiosError<ProblemDetail>) => {
      const status = error.response?.status ?? 0;
      const url = error.config?.url ?? '';

      // 401 no próprio login é credencial errada, e quem trata é o formulário.
      // Em qualquer outra rota significa sessão expirada ou token inválido.
      const sessaoExpirou = status === 401 && !url.includes('/auth/login');

      if (sessaoExpirou) {
        clearToken();
        if (window.location.pathname !== ROUTES.login) {
          window.location.assign(ROUTES.login);
        }
      }

      const apiError: ApiError = {
        status,
        message: mensagemDoErro(error),
        details: error.response?.data,
      };
      return Promise.reject(apiError);
    }
  );
}
