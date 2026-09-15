import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it } from 'vitest';

import { AuthProvider } from '../../../shared/providers/AuthProvider';
import { ThemeProvider } from '../../../shared/providers/ThemeProvider';
import { LoginForm } from './LoginForm';

function renderForm() {
  return render(
    <ThemeProvider>
      <AuthProvider>
        <LoginForm />
      </AuthProvider>
    </ThemeProvider>
  );
}

describe('LoginForm', () => {
  it('shows an inline error for an invalid email and clears it while typing', async () => {
    const user = userEvent.setup();
    renderForm();

    const email = screen.getByLabelText(/E-mail institucional/);
    await user.type(email, 'nome');
    await user.tab();

    expect(await screen.findByText(/Digite um e-mail válido/)).toBeTruthy();

    await user.type(email, '@unisantos.br');

    expect(screen.queryByText(/Digite um e-mail válido/)).toBeNull();
  });

  it('validates the minimum password length', async () => {
    const user = userEvent.setup();
    renderForm();

    await user.type(screen.getByLabelText(/Senha/), '123');
    await user.tab();

    expect(await screen.findByText(/ao menos 8 caracteres/)).toBeTruthy();
  });

  it('toggles password visibility', async () => {
    const user = userEvent.setup();
    renderForm();

    const password = screen.getByLabelText(/Senha/) as HTMLInputElement;
    expect(password.type).toBe('password');

    await user.click(screen.getByRole('button', { name: 'Mostrar senha' }));
    expect(password.type).toBe('text');

    await user.click(screen.getByRole('button', { name: 'Ocultar senha' }));
    expect(password.type).toBe('password');
  });
});
