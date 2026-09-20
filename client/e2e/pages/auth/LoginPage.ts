import type { Page } from '@playwright/test';

export class LoginPage {
  constructor(private readonly page: Page) {}

  async goto() {
    await this.page.goto('/login');
  }

  async login(email: string, password: string) {
    await this.page.getByRole('textbox', { name: /e-mail/i }).fill(email);
    await this.page.getByRole('textbox', { name: 'Senha' }).fill(password);
    await this.page.getByRole('button', { name: 'Entrar' }).click();
  }

  async logout() {
    await this.page.getByRole('button', { name: 'Sair' }).click();
    await this.page.waitForURL(/login/);
  }
}
