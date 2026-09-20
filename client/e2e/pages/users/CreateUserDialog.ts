import { expect } from '@playwright/test';
import type { Locator, Page } from '@playwright/test';

export type RoleOption = 'Secretaria' | 'Psicólogo(a)';

export interface ProfessionalFields {
  levelName: string; // e.g. 'Intern Student'
  specialties: string[]; // e.g. ['CBT']
  crp: string;
  phone: string;
  cpf: string;
  birthDate: string; // ISO yyyy-mm-dd
}

/** The 2-step create-user dialog: kind selection, then fields. */
export class CreateUserDialog {
  private readonly dialog: Locator;

  constructor(private readonly page: Page) {
    this.dialog = page.getByRole('dialog', { name: 'Novo usuário' });
  }

  async waitForOpen() {
    await expect(this.dialog).toBeVisible();
  }

  /** Step 1: pick the account kind and advance. */
  async chooseKind(role: RoleOption) {
    const escaped = role.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    await this.dialog.getByRole('radio', { name: new RegExp(escaped) }).check();
    await this.dialog.getByRole('button', { name: 'Avançar' }).click();
  }

  async fillBaseFields(
    firstName: string,
    lastName: string,
    email: string,
    password: string
  ) {
    await this.dialog
      .getByRole('textbox', { name: 'Nome', exact: true })
      .fill(firstName);
    await this.dialog
      .getByRole('textbox', { name: 'Sobrenome' })
      .fill(lastName);
    await this.dialog.getByRole('textbox', { name: /e-mail/i }).fill(email);
    await this.dialog
      .getByRole('textbox', { name: 'Senha inicial' })
      .fill(password);
  }

  /** Professional-only fields. */
  async fillProfessionalFields(fields: ProfessionalFields) {
    await this.dialog
      .getByRole('combobox', { name: /nível profissional/i })
      .click();
    await this.page.getByRole('option', { name: fields.levelName }).click();
    for (const specialty of fields.specialties) {
      await this.dialog.getByRole('checkbox', { name: specialty }).check();
    }
    await this.dialog.getByRole('textbox', { name: 'CRP' }).fill(fields.crp);
    await this.dialog
      .getByRole('textbox', { name: 'Telefone' })
      .fill(fields.phone);
    await this.dialog.getByRole('textbox', { name: 'CPF' }).fill(fields.cpf);
    await this.dialog
      .getByRole('textbox', { name: 'Data de nascimento' })
      .fill(fields.birthDate);
  }

  async submit() {
    await this.page.getByRole('button', { name: 'Criar usuário' }).click();
    await expect(this.dialog).toBeHidden();
  }
}
