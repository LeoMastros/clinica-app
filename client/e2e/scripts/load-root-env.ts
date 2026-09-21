// Loads the repo-root .env — the single source for MYSQL_*/COORDINATOR_*/
// E2E_* vars shared by dev, compose and tests. Explicitly exported env
// vars always win (they are never overwritten).
import { readFileSync } from 'node:fs';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const envPath = resolve(
  dirname(fileURLToPath(import.meta.url)),
  '../../../.env'
);

export function loadRootEnv(): void {
  try {
    for (const line of readFileSync(envPath, 'utf8').split('\n')) {
      const m = /^([A-Z0-9_]+)=(.*)$/.exec(line.trim());
      if (m && process.env[m[1]] === undefined) {
        // Strip surrounding quotes, then trailing inline comments.
        process.env[m[1]] = m[2]
          .replace(/^["']|["']$/g, '')
          .replace(/\s+#.*$/, '')
          .trim();
      }
    }
  } catch {
    /* .env is optional — real env vars still work */
  }
}
