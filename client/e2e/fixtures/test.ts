import { test as base } from '@playwright/test';
import type { Page } from '@playwright/test';
import { existsSync, mkdirSync, unlinkSync } from 'node:fs';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

import { ApiHelper } from '../helpers/api';
import { LoginPage } from '../pages/auth/LoginPage';
import { ACCOUNTS } from './accounts';

interface E2EFixtures {
  /** Direct backend client for setup and backend-state assertions. */
  api: ApiHelper;
  /** Page already signed in as the coordinator. */
  coordinatorPage: Page;
}

const REPO_ROOT = resolve(
  dirname(fileURLToPath(import.meta.url)),
  '../../../..'
);

function slugify(title: string): string {
  return title
    .toLowerCase()
    .normalize('NFD')
    .replace(/[̀-ͯ]/g, '') // strip accents
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-+|-+$/g, '');
}

/**
 * Shared fixtures. Specs import `test`/`expect` from here — never from
 * '@playwright/test' directly — so fixtures stay consistent.
 *
 * Note: the app's auth state lives in React memory, so storageState cannot
 * restore a session — every test signs in through the UI (which is also the
 * flow under test).
 */
export const test = base.extend<E2EFixtures>({
  api: async ({}, use) => {
    await use(new ApiHelper());
  },
  coordinatorPage: async ({ page }, use) => {
    const loginPage = new LoginPage(page);
    await loginPage.goto();
    await loginPage.login(
      ACCOUNTS.coordinator.email,
      ACCOUNTS.coordinator.password
    );
    await page.waitForURL(url => !url.pathname.endsWith('/login'));
    await use(page);
  },
});

// Organize recorded videos: lift each test's video out of the hashed
// test-results dir into demo-video/<feature>/<test-slug>.webm, then remove
// the raw copy so videos live in ONE place (demo-video/, gitignored).
test.afterEach(async ({ page }, testInfo) => {
  const video = page.video();
  if (!video) return;
  try {
    // The video file only exists once the browser context is closed —
    // afterEach runs before fixture teardown, so close it ourselves.
    await page.context().close();
    const feature = dirname(testInfo.file).split('/').pop() ?? 'misc';
    const dir = resolve(REPO_ROOT, 'demo-video', feature);
    const target = resolve(dir, `${slugify(testInfo.title)}.webm`);
    mkdirSync(dir, { recursive: true });
    await video.saveAs(target);
    const raw = resolve(testInfo.outputDir, 'video.webm');
    if (existsSync(raw)) unlinkSync(raw);
  } catch (error) {
    console.warn(
      `[e2e] could not collect video for "${testInfo.title}":`,
      error instanceof Error ? error.message : error
    );
  }
});

export { expect } from '@playwright/test';
