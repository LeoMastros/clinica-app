import { defineConfig, devices } from '@playwright/test';

import { loadRootEnv } from './e2e/scripts/load-root-env';

// Repo-root .env — single source for MYSQL_*/COORDINATOR_*/E2E_* vars
// shared by dev, compose and tests. Exported env vars win.
loadRootEnv();

/**
 * E2E suite — feature-based specs under e2e/specs/<feature>/.
 * See e2e/README.md for conventions.
 *
 * The suite runs against the real dev stack (vite :5173 + API :8080 + MySQL).
 * Workers are serial: specs share the dev database, so parallel mutation
 * (e.g. creating users) would race. In CI, run against a dedicated test DB
 * and raise workers.
 */
export default defineConfig({
  testDir: './e2e/specs',
  outputDir: './e2e/test-results',
  timeout: 30_000,
  fullyParallel: false,
  forbidOnly: !!process.env.CI,
  workers: 1,
  retries: process.env.CI ? 1 : 0,
  reporter: [
    ['list'],
    ['html', { outputFolder: 'e2e/playwright-report', open: 'never' }],
    // JUnit XML → published as a check on PRs by testing.yml
    ['junit', { outputFile: 'e2e/test-results/junit.xml' }],
    // Organizes recorded videos into demo-video/<feature>/<test>.webm
    ['./e2e/scripts/video-collector.ts'],
  ],
  use: {
    baseURL: process.env.E2E_BASE_URL ?? 'http://localhost:5173',
    // Slow down interactions so recorded videos are watchable.
    // E2E_SLOWMO=600 makes a good PR demo; default 0 keeps CI fast.
    launchOptions: { slowMo: Number(process.env.E2E_SLOWMO ?? 0) },
    video:
      (process.env.E2E_VIDEO as 'on' | 'retain-on-failure' | 'off') ??
      'retain-on-failure',
    screenshot: 'only-on-failure',
    trace: 'retain-on-failure',
  },
  projects: [
    {
      name: 'chromium',
      use: {
        ...devices['Desktop Chrome'],
        // E2E_CHANNEL=chrome reuses a system Chrome instead of the bundled one.
        channel: process.env.E2E_CHANNEL || undefined,
      },
    },
    { name: 'firefox', use: { ...devices['Desktop Firefox'] } },
    { name: 'webkit', use: { ...devices['Desktop Safari'] } },
  ],
});
