import { expect } from '@playwright/test';
import type { Locator, Page } from '@playwright/test';

import { CreateUserDialog } from './CreateUserDialog';

export class UsersPage {
  constructor(private readonly page: Page) {}

  /**
   * Navigates via the sidebar NavLink — client-side routing. A full page
   * reload (page.goto) would reset the app's in-memory auth state and land
   * back on /login.
   */
  async goto() {
    await this.page.getByRole('link', { name: 'Gestão de Usuários' }).click();
    await expect(this.page).toHaveURL(/usuarios/);
  }

  /** Table row containing the given e-mail. */
  row(email: string): Locator {
    return this.page.getByRole('row', {
      name: new RegExp(email.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')),
    });
  }

  async openCreateDialog(): Promise<CreateUserDialog> {
    await this.page.getByRole('button', { name: 'Novo usuário' }).click();
    const dialog = new CreateUserDialog(this.page);
    await dialog.waitForOpen();
    return dialog;
  }

  /** Clicks the row's switch and waits until it reflects `active`. */
  async setActive(email: string, active: boolean) {
    const toggle = this.row(email).getByRole('switch');
    await toggle.click();
    await expect(toggle).toBeChecked({ checked: active });
  }

  async openDetails(email: string) {
    await this.row(email)
      .getByRole('button', { name: new RegExp(`Ver detalhes`) })
      .click();
    return this.page.getByRole('dialog', { name: 'Detalhes do usuário' });
  }

  async openEdit(email: string) {
    await this.row(email)
      .getByRole('button', { name: new RegExp(`Editar`) })
      .click();
    return this.page.getByRole('dialog', { name: 'Editar usuário' });
  }

  async filterByType(typeLabel: string) {
    await this.page.getByRole('combobox', { name: 'Perfil' }).click();
    await this.page.getByRole('option', { name: typeLabel }).click();
  }

  async filterByStatus(statusLabel: 'Ativos' | 'Inativos' | 'Todos os status') {
    await this.page.getByRole('combobox', { name: 'Status' }).click();
    await this.page.getByRole('option', { name: statusLabel }).click();
  }
}
