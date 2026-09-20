#!/usr/bin/env bun
// Stops whatever `e2e:stack` spawned: the detached API/web processes (tracked
// via PID files) and the MySQL dev container. Anything that was already
// running before e2e:stack (e.g. the compose stack) is left alone — the PID
// files only exist for processes this tooling spawned.
import { spawnSync } from 'node:child_process';
import { existsSync, readFileSync, rmSync } from 'node:fs';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../../..');
const SERVER = resolve(ROOT, 'server');

let stopped = 0;

// API + web: detached process groups — kill the group leader's negative PID
// so the java/node children die too. Skip gracefully if already dead.
for (const name of ['api', 'web']) {
  const pidFile = `/tmp/e2e-${name}.pid`;
  if (!existsSync(pidFile)) {
    console.log(`– ${name}: not spawned by e2e:stack (or already stopped)`);
    continue;
  }
  const pid = Number(readFileSync(pidFile, 'utf8').trim());
  try {
    process.kill(-pid);
    console.log(`✓ ${name}: stopped process group ${pid}`);
    stopped++;
  } catch {
    console.log(`– ${name}: process ${pid} already gone`);
  }
  rmSync(pidFile, { force: true });
}

// MySQL dev container — only stop it if e2e:stack was the one that started
// it (a pre-existing container belongs to the dev's own workflow).
const mysqlMarker = '/tmp/e2e-mysql.started';
if (existsSync(mysqlMarker)) {
  const docker = Bun.which('docker') ?? 'docker';
  const down = spawnSync(docker, ['compose', '-f', 'compose.yaml', 'down'], {
    cwd: SERVER,
    stdio: 'inherit',
  });
  if (down.status === 0) {
    console.log('✓ MySQL dev container stopped (:3307)');
    stopped++;
  } else {
    console.log('– MySQL dev container: nothing to stop');
  }
  rmSync(mysqlMarker, { force: true });
} else {
  console.log('– MySQL: not started by e2e:stack — leaving it running');
}

console.log(
  stopped > 0
    ? 'Stack stopped — ports freed.'
    : 'Nothing was running from e2e:stack.'
);
