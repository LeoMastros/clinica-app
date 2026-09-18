import { beforeEach, describe, expect, it, vi } from 'vitest';

import type { User } from '../../types/user';
import { readToken, storeToken } from '../api/interceptors';
import { getCurrentUser, login, logout } from './service';

// vi.hoisted porque o vi.mock é içado para o topo do arquivo: um const comum
// declarado aqui ainda não existiria quando a fábrica do mock rodasse.
const { get, post } = vi.hoisted(() => ({ get: vi.fn(), post: vi.fn() }));

vi.mock('../api/client', () => ({
  apiClient: {
    get: (...args: unknown[]) => get(...args),
    post: (...args: unknown[]) => post(...args),
  },
}));

const PERFIL: User = {
  id: '1',
  name: 'Maria de Souza',
  email: 'maria.souza@unisantos.br',
  role: 'psychologist',
  active: true,
};

describe('login', () => {
  beforeEach(() => {
    localStorage.clear();
    get.mockReset();
    post.mockReset();
  });

  it('guarda o token e devolve o perfil', async () => {
    post.mockResolvedValue({ data: { token: 'jwt-de-teste', user: PERFIL } });

    const usuario = await login({
      email: 'maria.souza@unisantos.br',
      password: 'senha-de-teste',
    });

    expect(post).toHaveBeenCalledWith('/auth/login', {
      email: 'maria.souza@unisantos.br',
      password: 'senha-de-teste',
    });
    expect(usuario).toEqual(PERFIL);
    expect(readToken()).toBe('jwt-de-teste');
  });

  it('não guarda token nenhum quando a API recusa', async () => {
    post.mockRejectedValue({ status: 401, message: 'E-mail ou senha invalidos.' });

    await expect(
      login({ email: 'maria.souza@unisantos.br', password: 'errada' })
    ).rejects.toMatchObject({ status: 401 });

    expect(readToken()).toBeNull();
  });
});

describe('getCurrentUser', () => {
  beforeEach(() => {
    localStorage.clear();
    get.mockReset();
  });

  it('não chama a API quando não há sessão guardada', async () => {
    expect(await getCurrentUser()).toBeNull();
    expect(get).not.toHaveBeenCalled();
  });

  it('devolve o perfil quando o token ainda vale', async () => {
    storeToken('jwt-de-teste');
    get.mockResolvedValue({ data: PERFIL });

    expect(await getCurrentUser()).toEqual(PERFIL);
    expect(get).toHaveBeenCalledWith('/auth/me');
  });

  it('descarta o token recusado para não insistir a cada recarga', async () => {
    storeToken('jwt-expirado');
    get.mockRejectedValue({ status: 401, message: 'Token invalido.' });

    expect(await getCurrentUser()).toBeNull();
    expect(readToken()).toBeNull();
  });
});

describe('logout', () => {
  it('apaga a sessão guardada', async () => {
    storeToken('jwt-de-teste');

    await logout();

    expect(readToken()).toBeNull();
  });
});
