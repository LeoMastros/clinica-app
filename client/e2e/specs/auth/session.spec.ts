import { expect, test } from '../../fixtures/test';
import { LoginPage } from '../../pages/auth/LoginPage';

test.describe('auth session', () => {
  test('session survives a page reload via the refresh cookie', async ({
    coordinatorPage: page,
  }) => {
    // Full reload clears the in-memory access token — the httpOnly refresh
    // cookie restores the session silently via /auth/refresh + /auth/me.
    await page.reload();
    await expect(page).not.toHaveURL(/login/);
    await expect(page.getByRole('button', { name: 'Sair' })).toBeVisible();
  });

  test('an authenticated user is bounced away from the login page', async ({
    coordinatorPage: page,
  }) => {
    // Deep-linking /login while authenticated redirects to the index —
    // the login page is only for unauthenticated users.
    await page.goto('/login');
    await expect(page).not.toHaveURL(/login/);
    await expect(page.getByRole('button', { name: 'Sair' })).toBeVisible();
  });

  test('after logout the login page is reachable again', async ({
    coordinatorPage: page,
  }) => {
    const loginPage = new LoginPage(page);
    await loginPage.logout();
    await expect(page).toHaveURL(/login/);
    await expect(page.getByRole('textbox', { name: /e-mail/i })).toBeVisible();
  });
});
