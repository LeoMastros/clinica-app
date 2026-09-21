import { ACCOUNTS } from '../../fixtures/accounts';
import { expect, test } from '../../fixtures/test';
import { LoginPage } from '../../pages/auth/LoginPage';

test.describe('auth', () => {
  test('coordinator signs in and lands inside the app', async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.goto();
    await loginPage.login(
      ACCOUNTS.coordinator.email,
      ACCOUNTS.coordinator.password
    );

    await expect(page).not.toHaveURL(/login/);
    // AppBar chip shows the role label mapped from the backend userType.
    await expect(page.getByText('Administrador')).toBeVisible();
  });

  test('invalid credentials stay on the login page', async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.goto();
    await loginPage.login(ACCOUNTS.coordinator.email, 'wrong-password');

    await expect(page).toHaveURL(/login/);
  });
});
