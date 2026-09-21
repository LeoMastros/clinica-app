import { ACCOUNTS, uniqueEmail } from '../../fixtures/accounts';
import { expect, test } from '../../fixtures/test';
import { LoginPage } from '../../pages/auth/LoginPage';
import { UsersPage } from '../../pages/users/UsersPage';

test.describe('users', () => {
  test('coordinator creates a secretary through the 2-step modal', async ({
    coordinatorPage: page,
    api,
  }) => {
    const email = uniqueEmail('sec');
    const usersPage = new UsersPage(page);
    await usersPage.goto();

    const dialog = await usersPage.openCreateDialog();
    await dialog.chooseKind('Secretaria');
    await dialog.fillBaseFields('Maria', 'Souza', email, 'E2eSecret123');
    await dialog.submit();

    const row = usersPage.row(email);
    await expect(row).toBeVisible();
    await expect(row).toContainText('Maria Souza');
    await expect(row.getByRole('switch')).toBeChecked();

    const created = await api.findUserByEmail(email);
    expect(created?.userType).toBe('SECRETARY');
    expect(created?.isActive).toBe(true);
  });

  test('coordinator creates a psychologist — starts inactive', async ({
    coordinatorPage: page,
    api,
  }) => {
    const email = uniqueEmail('pro');
    const usersPage = new UsersPage(page);
    await usersPage.goto();

    const dialog = await usersPage.openCreateDialog();
    await dialog.chooseKind('Psicólogo(a)');
    await dialog.fillBaseFields('Carlos', 'Ramos', email, 'E2ePro12345');
    await dialog.fillProfessionalFields({
      levelName: 'Intern Student',
      specialties: ['CBT', 'Psychoanalysis'],
      crp: '06/99999',
      phone: '11988887777',
      cpf: '11122233344',
      birthDate: '1992-03-15',
    });
    await dialog.submit();

    const row = usersPage.row(email);
    await expect(row).toBeVisible();
    await expect(row.getByRole('switch')).not.toBeChecked();

    const created = await api.findUserByEmail(email);
    expect(created?.userType).toBe('PROFESSIONAL');
    expect(created?.isActive).toBe(false);
  });

  test('deactivating a user blocks login with feedback; re-activating restores it', async ({
    coordinatorPage: page,
    api,
  }) => {
    const email = uniqueEmail('sec-toggle');
    await api.createUser({
      email,
      password: 'E2eSecret123',
      firstName: 'Toggle',
      lastName: 'User',
      role: 'SECRETARY',
    });

    const usersPage = new UsersPage(page);
    const loginPage = new LoginPage(page);
    await usersPage.goto();

    // Deactivate → sign in is rejected with a visible message.
    await usersPage.setActive(email, false);
    expect((await api.findUserByEmail(email))?.isActive).toBe(false);

    await loginPage.logout();
    await loginPage.login(email, 'E2eSecret123');
    await expect(page).toHaveURL(/login/);
    await expect(
      page.getByRole('alert').filter({ hasText: /não|not|inativ/i })
    ).toBeVisible();

    // Re-activate (fresh coordinator session) → sign-in works again.
    await loginPage.login(
      ACCOUNTS.coordinator.email,
      ACCOUNTS.coordinator.password
    );
    await page.waitForURL(url => !url.pathname.endsWith('/login'));
    await usersPage.goto();
    await usersPage.setActive(email, true);
    expect((await api.findUserByEmail(email))?.isActive).toBe(true);

    await loginPage.logout();
    await loginPage.login(email, 'E2eSecret123');
    await expect(page).not.toHaveURL(/login/);
  });

  test('coordinator edits a user and views details', async ({
    coordinatorPage: page,
    api,
  }) => {
    const email = uniqueEmail('sec-edit');
    await api.createUser({
      email,
      password: 'E2eSecret123',
      firstName: 'Before',
      lastName: 'Edit',
      role: 'SECRETARY',
    });

    const usersPage = new UsersPage(page);
    await usersPage.goto();

    const details = await usersPage.openDetails(email);
    await expect(details).toContainText('Before Edit');
    await expect(details).toContainText('Secretaria');
    await page.keyboard.press('Escape');
    await expect(details).toBeHidden();

    const editDialog = await usersPage.openEdit(email);
    await editDialog.getByRole('textbox', { name: 'Sobrenome' }).fill('Edited');
    await page.getByRole('button', { name: 'Salvar' }).click();
    await expect(editDialog).toBeHidden();
    await expect(usersPage.row(email)).toContainText('Before Edited');
  });

  test('filters the table by type and status', async ({
    coordinatorPage: page,
    api,
  }) => {
    const proEmail = uniqueEmail('filter-pro');
    await api.createProfessional({
      email: proEmail,
      password: 'E2ePro12345',
      firstName: 'Filter',
      lastName: 'Pro',
      specialtyIds: [1],
      professionalLevelId: 1,
    });

    const usersPage = new UsersPage(page);
    await usersPage.goto();

    // Filter: PROFESSIONAL + Inativo → the new pro shows, admin does not.
    await usersPage.filterByType('Psicólogo(a)');
    await usersPage.filterByStatus('Inativos');
    await expect(usersPage.row(proEmail)).toBeVisible();
    await expect(usersPage.row(ACCOUNTS.coordinator.email)).toBeHidden();

    // Clear filters → admin is back.
    await usersPage.filterByType('Todos os perfis');
    await usersPage.filterByStatus('Todos os status');
    await expect(usersPage.row(ACCOUNTS.coordinator.email)).toBeVisible();
  });

  test('the coordinator account cannot be deactivated', async ({
    coordinatorPage: page,
  }) => {
    const usersPage = new UsersPage(page);
    await usersPage.goto();
    await expect(
      usersPage.row(ACCOUNTS.coordinator.email).getByRole('switch')
    ).toBeDisabled();
  });
});
