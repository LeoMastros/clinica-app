import axios from 'axios';
import type { AxiosError } from 'axios';
import { beforeEach, describe, expect, it } from 'vitest';

import { AUTH_TOKEN_STORAGE_KEY } from '../../constants/api';
import type { ApiError } from '../../types/api';
import { clearToken, readToken, setupInterceptors, storeToken } from './interceptors';

function novoCliente() {
  const client = axios.create({ baseURL: 'http://servidor.teste/api/v1' });
  setupInterceptors(client);
  return client;
}

/** Executa só a parte de requisição, sem rede. */
async function montarRequisicao(client: ReturnType<typeof novoCliente>, url: string) {
  const handlers = client.interceptors.request as unknown as {
    handlers: { fulfilled: (c: unknown) => Promise<unknown> | unknown }[];
  };
  const config = { url, headers: {} as Record<string, string> };
  return (await handlers.handlers[0].fulfilled(config)) as typeof config;
}

/** Executa só a parte de resposta com erro, sem rede. */
function dispararErro(client: ReturnType<typeof novoCliente>, error: Partial<AxiosError>) {
  const handlers = client.interceptors.response as unknown as {
    handlers: { rejected: (e: unknown) => Promise<never> }[];
  };
  return handlers.handlers[0].rejected(error);
}

describe('token guardado', () => {
  beforeEach(() => localStorage.clear());

  it('grava, lê e apaga', () => {
    expect(readToken()).toBeNull();

    storeToken('abc123');
    expect(readToken()).toBe('abc123');
    expect(localStorage.getItem(AUTH_TOKEN_STORAGE_KEY)).toBe('abc123');

    clearToken();
    expect(readToken()).toBeNull();
  });
});

describe('interceptador de requisição', () => {
  beforeEach(() => localStorage.clear());

  it('anexa o token no cabeçalho quando existe sessão', async () => {
    storeToken('token-de-teste');
    const config = await montarRequisicao(novoCliente(), '/pacientes');

    expect(config.headers.Authorization).toBe('Bearer token-de-teste');
  });

  it('não anexa cabeçalho nenhum quando não há sessão', async () => {
    const config = await montarRequisicao(novoCliente(), '/pacientes');

    expect(config.headers.Authorization).toBeUndefined();
  });
});

describe('interceptador de resposta', () => {
  beforeEach(() => localStorage.clear());

  it('usa a mensagem que o servidor mandou no ProblemDetail', async () => {
    const erro = await dispararErro(novoCliente(), {
      config: { url: '/auth/login' } as unknown as AxiosError['config'],
      response: {
        status: 401,
        data: { title: 'Falha na autenticacao', detail: 'E-mail ou senha invalidos.' },
      } as unknown as AxiosError['response'],
    }).catch((e: ApiError) => e);

    expect(erro.status).toBe(401);
    expect(erro.message).toBe('E-mail ou senha invalidos.');
  });

  it('mantém a sessão quando o 401 vem do próprio login', async () => {
    storeToken('token-de-teste');

    await dispararErro(novoCliente(), {
      config: { url: '/auth/login' } as unknown as AxiosError['config'],
      response: { status: 401, data: {} } as unknown as AxiosError['response'],
    }).catch(() => undefined);

    // Credencial errada não é sessão expirada: quem já estava logado continua.
    expect(readToken()).toBe('token-de-teste');
  });

  it('descarta a sessão quando o 401 vem de qualquer outra rota', async () => {
    storeToken('token-de-teste');

    await dispararErro(novoCliente(), {
      config: { url: '/pacientes' } as unknown as AxiosError['config'],
      response: { status: 401, data: {} } as unknown as AxiosError['response'],
    }).catch(() => undefined);

    expect(readToken()).toBeNull();
  });

  it('explica quando o servidor não respondeu', async () => {
    const erro = await dispararErro(novoCliente(), {
      config: { url: '/pacientes' } as unknown as AxiosError['config'],
    }).catch((e: ApiError) => e);

    expect(erro.status).toBe(0);
    expect(erro.message).toBe('Não foi possível falar com o servidor.');
  });
});
