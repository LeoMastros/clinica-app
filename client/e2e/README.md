# E2E tests (Playwright)

Feature-based end-to-end suite mirroring `src/features/`. Specs run against
the **real dev stack** — no mocks:

| Dependency                   | How it starts         |
| ---------------------------- | --------------------- |
| MySQL (dev container, :3307) | `server/compose.yaml` |
| Spring Boot API (:8080)      | `./gradlew bootRun`   |
| Vite dev server (:5173)      | `bun run dev`         |

**You don't have to start these manually** — every `test:e2e*` script first
runs `bun run e2e:stack`, which brings up whatever is missing and reuses
whatever is already healthy — **spawned servers stay running** afterwards
(tracked via `/tmp/e2e-*.pid`); `bun run e2e:stack:down` stops them —
whatever is already healthy (including the docker compose stack if it's the
one occupying the ports). Env vars come from the repo-root `.env`
automatically — no exports needed.

## Run

```bash
bunx playwright install              # first time only (bundled browsers)
                                     # — skip if E2E_CHANNEL=chrome is set

bun run test:e2e                     # stack + headless, all specs (chromium)
bunx playwright test                 # stack manual + ALL browsers (slow)
bun run test:e2e:ui                  # stack + interactive UI mode
bun run test:e2e:video               # stack + record video of every test
bun run e2e:stack                    # just ensure the stack is up
bun run e2e:stack:down               # stop what e2e:stack spawned (frees :8080)
bunx playwright test users           # one feature folder
```

Videos are **local-only**: nothing records them in CI or on deploy — only
`test:e2e:video` (`E2E_VIDEO=on`) or a failure (`retain-on-failure` default).

Recorded videos are auto-organized by a reporter into
**`demo-video/<feature>/<test-slug>.webm`** at the repo root (gitignored —
attach the interesting ones to your PR instead of committing them):

```
demo-video/
├── auth/
│   ├── an-authenticated-user-is-bounced-away-from-the-login-page.webm
│   └── ...
└── users/
    └── ...
```

`e2e/test-results/` keeps only failure artifacts (traces, screenshots) and is
wiped at the start of each run; `e2e/playwright-report/` is the browsable
HTML report (`bunx playwright show-report e2e/playwright-report`).

Env overrides (root `.env` or exported): `E2E_BASE_URL` (client — use
`http://localhost:3000` to test the compose stack), `E2E_API_URL` (backend),
`E2E_ADMIN_EMAIL`/`E2E_ADMIN_PASSWORD` (must match the seeded coordinator),
`E2E_CHANNEL` (e.g. `chrome` to use system Chrome instead of bundled
Chromium, chromium project only), `E2E_SLOWMO=<ms>` (slows every action —
use ~600 when recording PR demo videos), `E2E_VIDEO` (`on`/`off`/
`retain-on-failure`).

## CI

`.github/workflows/testing.yml` runs this suite on PRs and pushes to `main`,
separate from `deploy.yml`: it builds the compose stack with throwaway
credentials, waits for the API, installs Chromium and runs the specs headless
(`E2E_VIDEO=off`, `E2E_SLOWMO` unset). The Playwright HTML report is uploaded
as an artifact on failure.

## Structure

```
e2e/
├── fixtures/
│   ├── test.ts          # shared fixtures — ALWAYS import test/expect from here
│   └── accounts.ts      # seeded credentials + uniqueEmail() helper
├── helpers/
│   └── api.ts           # backend client for arrange/assert (not the flow under test)
├── pages/               # Page Objects, one folder per feature
│   ├── auth/LoginPage.ts
│   └── users/{UsersPage, CreateUserDialog}.ts
├── scripts/
│   ├── load-root-env.ts    # loads repo-root .env (shared by config + stack script)
│   ├── start-stack.ts      # brings up mysql + API + vite before the suite
│   └── video-collector.ts  # reporter → demo-video/<feature>/<test>.webm
└── specs/               # one folder per feature, mirroring src/features/
    ├── auth/login.spec.ts
    ├── auth/session.spec.ts
    └── users/manage-users.spec.ts
```

## Conventions

- **Import `test`/`expect` from `e2e/fixtures/test.ts`**, never from
  `@playwright/test` — that's how every spec gets the shared fixtures
  (`api`, `coordinatorPage`, add your own there).
- **Page Objects** for anything the user sees/clicks. Specs describe
  behavior, pages describe the DOM. New feature → `pages/<feature>/` +
  `specs/<feature>/`.
- **Arrange via the API, act via the UI.** Use the `api` fixture to set up
  state (e.g. create an inactive professional) and to assert side effects;
  the thing under test must happen through the UI.
- **`uniqueEmail()` for created users** — the system has a no-delete policy,
  so test data persists; unique emails keep specs independent.
- **Auth state is per-test** — log in through the UI (`coordinatorPage`
  fixture or `LoginPage`). The session survives a reload via the httpOnly
  refresh cookie (boot-time `/auth/refresh` + `/auth/me`), so specs can
  `page.reload()`/`page.goto()` without losing auth.
- **Serial execution** is intentional (`workers: 1`): specs share the dev
  database. For CI, point at a dedicated test DB and raise workers.
- **Locators**: prefer `getByRole`/`getByLabel` with accessible names —
  they double as a11y checks. Avoid CSS selectors.
