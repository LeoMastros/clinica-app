/**
 * Accounts used by e2e specs. The coordinator credentials come from
 * E2E_ADMIN_EMAIL / E2E_ADMIN_PASSWORD — never hardcode them here; they must
 * match the COORDINATOR_EMAIL / COORDINATOR_PASSWORD the server was seeded
 * with (see root README).
 */
const requireEnv = (name: string): string => {
  const value = process.env[name];
  if (!value) {
    throw new Error(
      `${name} is not set — export it before running e2e specs (see e2e/README.md)`
    );
  }
  return value;
};

export const ACCOUNTS = {
  /** Resolved lazily — specs that only need `uniqueEmail` don't require the env vars. */
  get coordinator() {
    return {
      email: requireEnv('E2E_ADMIN_EMAIL'),
      password: requireEnv('E2E_ADMIN_PASSWORD'),
    };
  },
} as const;

/**
 * The system has a no-delete policy — test accounts persist. Always generate
 * a unique email so specs stay independent and re-runnable.
 */
export function uniqueEmail(prefix: string): string {
  return `e2e-${prefix}-${Date.now()}-${Math.floor(Math.random() * 1000)}@clinica.test`;
}
