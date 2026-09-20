// Playwright reporter: lifts each test's recorded video out of the hashed
// test-results dir into demo-video/<feature>/<test-slug>.webm, then removes
// the raw copy — videos live in ONE place (demo-video/, gitignored).
//
// A reporter (not an afterEach hook) is used because the video attachment
// only exists after the test fully ends, and hooks declared in a fixture
// file don't apply to specs in other files.
import type { Reporter, TestCase, TestResult } from '@playwright/test/reporter';
import { copyFileSync, existsSync, mkdirSync, unlinkSync } from 'node:fs';
import { basename, dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const REPO_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../../..');

function slugify(title: string): string {
  return title
    .toLowerCase()
    .normalize('NFD')
    .replace(/[̀-ͯ]/g, '') // strip accents
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-+|-+$/g, '');
}

export default class VideoCollector implements Reporter {
  onTestEnd(test: TestCase, result: TestResult) {
    const video = result.attachments.find(
      a => a.name === 'video' && a.path && existsSync(a.path)
    );
    if (!video?.path) return;
    try {
      const feature = basename(dirname(test.location.file));
      const dir = resolve(REPO_ROOT, 'demo-video', feature);
      const target = resolve(dir, `${slugify(test.title)}.webm`);
      mkdirSync(dir, { recursive: true });
      copyFileSync(video.path, target);
      unlinkSync(video.path);
    } catch (error) {
      console.warn(
        `[e2e] could not collect video for "${test.title}":`,
        error instanceof Error ? error.message : error
      );
    }
  }
}
