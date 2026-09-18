import { render, screen, waitFor } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { useAuth } from '../../core/auth';
import type { User } from '../../types/user';
import { AuthProvider } from './AuthProvider';

// vi.hoisted porque o vi.mock é içado para o topo do arquivo: um const comum
// declarado aqui ainda não existiria quando a fábrica do mock rodasse.
const { getCurrentUser, login, logout } = vi.hoisted(() => ({
  getCurrentUser: vi.fn(),
  login: vi.fn(),
  logout: vi.fn(),
}));

vi.mock('../../core/auth/service', () => ({
  getCurrentUser: () => getCurrentUser(),
  login: (credentials: unknown) => login(credentials),
  logout: () => logout(),
}));

const PERFIL: User = {
  id: '1',
  name: 'Maria de Souza',
  email: 'maria.souza@unisantos.br',
  role: 'psychologist',
  active: true,
};

function Sonda() {
  const { user, isAuthenticated, isInitializing, error, login: entrar } = useAuth();
  return (
    <div>
      <span data-testid="inicializando">{String(isInitializing)}</span>
      <span data-testid="autenticado">{String(isAuthenticated)}</span>
      <span data-testid="nome">{user?.name ?? '—'}</span>
      <span data-testid="erro">{error ?? '—'}</span>
      <button
        onClick={() => {
          void entrar({ email: 'maria.souza@unisantos.br', password: 'senha' });
        }}
      >
        entrar
      </button>
    </div>
  );
}

function renderizar() {
  return render(
    <AuthProvider>
      <Sonda />
    </AuthProvider>
  );
}

describe('AuthProvider', () => {
  beforeEach(() => {
    getCurrentUser.mockReset();
    login.mockReset();
    logout.mockReset();
  });

  it('recupera a sessão guardada ao abrir o app', async () => {
    getCurrentUser.mockResolvedValue(PERFIL);
    renderizar();

    // Enquanto confere com o servidor, ainda não dá para dizer que está fora.
    expect(screen.getByTestId('inicializando').textContent).toBe('true');

    await waitFor(() =>
      expect(screen.getByTestId('inicializando').textContent).toBe('false')
    );
    expect(screen.getByTestId('autenticado').textContent).toBe('true');
    expect(screen.getByTestId('nome').textContent).toBe('Maria de Souza');
  });

  it('termina a inicialização mesmo sem sessão guardada', async () => {
    getCurrentUser.mockResolvedValue(null);
    renderizar();

    await waitFor(() =>
      expect(screen.getByTestId('inicializando').textContent).toBe('false')
    );
    expect(screen.getByTestId('autenticado').textContent).toBe('false');
  });

  it('guarda a mensagem da API quando o login falha', async () => {
    getCurrentUser.mockResolvedValue(null);
    login.mockRejectedValue({
      status: 401,
      message: 'E-mail ou senha invalidos.',
    });
    renderizar();

    await waitFor(() =>
      expect(screen.getByTestId('inicializando').textContent).toBe('false')
    );
    screen.getByRole('button', { name: 'entrar' }).click();

    await waitFor(() =>
      expect(screen.getByTestId('erro').textContent).toBe(
        'E-mail ou senha invalidos.'
      )
    );
    expect(screen.getByTestId('autenticado').textContent).toBe('false');
  });

  it('autentica e expõe o perfil quando o login dá certo', async () => {
    getCurrentUser.mockResolvedValue(null);
    login.mockResolvedValue(PERFIL);
    renderizar();

    await waitFor(() =>
      expect(screen.getByTestId('inicializando').textContent).toBe('false')
    );
    screen.getByRole('button', { name: 'entrar' }).click();

    await waitFor(() =>
      expect(screen.getByTestId('autenticado').textContent).toBe('true')
    );
    expect(screen.getByTestId('erro').textContent).toBe('—');
  });
});
