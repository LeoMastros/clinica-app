#!/usr/bin/env bun
// Ensures the local dev stack is running before e2e tests:
//   MySQL  — server/compose.yaml dev container on :3307
//   API    — ./gradlew bootRun on :8080 (needs COORDINATOR_*/JWT_SECRET from .env)
//   Vite   — bun run dev on :5173
//
// Anything already healthy is reused untouched (e.g. the docker compose
// stack on the same ports). Spawned servers are left running afterwards —
// that is the point of a dev stack.
import { spawn } from 'node:child_process';
import { openSync, writeFileSync } from 'node:fs';
import net from 'node:net';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

import { loadRootEnv } from './load-root-env';

loadRootEnv();

const ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../../..');
const SERVER = resolve(ROOT, 'server');
const CLIENT = resolve(ROOT, 'client');
const API_URL = (process.env.E2E_API_URL ?? 'http://localhost:8080').replace(
  /\/$/,
  ''
);
const BASE_URL = (process.env.E2E_BASE_URL ?? 'http://localhost:5173').replace(
  /\/$/,
  ''
);

function waitForHttp(url: string, timeoutMs: number): Promise<boolean> {
  const deadline = Date.now() + timeoutMs;
  return new Promise(resolvePromise => {
    const tick = async () => {
      try {
        const res = await fetch(url);
        if (res.status < 500) return resolvePromise(true);
      } catch {
        /* not up yet */
      }
      if (Date.now() > deadline) return resolvePromise(false);
      setTimeout(tick, 1500);
    };
    void tick();
  });
}

function waitForPort(port: number, timeoutMs: number): Promise<boolean> {
  const deadline = Date.now() + timeoutMs;
  return new Promise(resolvePromise => {
    const tick = () => {
      const socket = net.connect(port, '127.0.0.1');
      socket.once('connect', () => {
        socket.destroy();
        resolvePromise(true);
      });
      socket.once('error', () => {
        socket.destroy();
        if (Date.now() > deadline) resolvePromise(false);
        else setTimeout(tick, 1500);
      });
    };
    tick();
  });
}

function spawnDetached(
  command: string,
  args: string[],
  cwd: string,
  logName: string,
  trackPid = true
): void {
  // Bun's spawn doesn't resolve bare names through PATH reliably —
  // resolve to an absolute path first.
  const bin = command.startsWith('/')
    ? command
    : (Bun.which(command) ?? command);
  const log = openSync(`/tmp/e2e-${logName}.log`, 'a');
  const child = spawn(bin, args, {
    cwd,
    detached: true,
    stdio: ['ignore', log, log],
    env: process.env,
  });
  child.on('error', error => {
    console.error(`✗ failed to spawn "${command}": ${error.message}`);
  });
  child.unref();
  // Track long-running servers (not `docker compose up -d`, which exits
  // immediately) so `e2e:stack:down` can stop them later.
  if (trackPid && child.pid)
    writeFileSync(`/tmp/e2e-${logName}.pid`, String(child.pid));
}

async function ensureMysql(): Promise<void> {
  if (await waitForPort(3307, 1000)) {
    console.log('✓ MySQL dev container already up (:3307)');
    return;
  }
  console.log('• Starting MySQL dev container (server/compose.yaml)...');
  spawnDetached(
    'docker',
    ['compose', '-f', 'compose.yaml', 'up', '-d'],
    SERVER,
    'mysql',
    false
  );
  if (!(await waitForPort(3307, 60_000))) {
    throw new Error('MySQL did not come up on :3307 within 60s');
  }
  // Marker so stack:down knows we started the container (vs. reusing one).
  writeFileSync('/tmp/e2e-mysql.started', '1');
  console.log('✓ MySQL dev container ready (:3307)');
}

async function ensureApi(): Promise<void> {
  if (await waitForHttp(`${API_URL}/actuator/health`, 1000)) {
    // API up implies its DB is up — compose stack or dev stack alike.
    console.log(`✓ API already up (${API_URL})`);
    return;
  }
  await ensureMysql();
  console.log(`• Starting API: ./gradlew bootRun (log: /tmp/e2e-api.log)...`);
  spawnDetached(resolve(SERVER, 'gradlew'), ['bootRun'], SERVER, 'api');
  if (!(await waitForHttp(`${API_URL}/actuator/health`, 180_000))) {
    throw new Error(
      `API did not come up at ${API_URL} within 180s — check /tmp/e2e-api.log`
    );
  }
  console.log(`✓ API ready (${API_URL})`);
}

async function ensureWeb(): Promise<void> {
  if (await waitForHttp(BASE_URL, 1000)) {
    console.log(`✓ Web already up (${BASE_URL})`);
    return;
  }
  console.log(`• Starting web: bun run dev (log: /tmp/e2e-web.log)...`);
  spawnDetached('bun', ['run', 'dev'], CLIENT, 'web');
  if (!(await waitForHttp(BASE_URL, 60_000))) {
    throw new Error(
      `Web did not come up at ${BASE_URL} within 60s — check /tmp/e2e-web.log`
    );
  }
  console.log(`✓ Web ready (${BASE_URL})`);
}

try {
  await ensureApi();
  await ensureWeb();
  console.log('Stack is up — running Playwright...\n');
} catch (error) {
  console.error(`\n✗ ${error instanceof Error ? error.message : error}\n`);
  process.exit(1);
}
