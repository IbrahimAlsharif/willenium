import { defineConfig } from '@playwright/test';

const isCi = process.env.CI === 'true';
const defaultHeadless = isCi;

export default defineConfig({
  testDir: './src/pw/tests',
  timeout: 90_000,
  expect: {
    timeout: 15_000,
  },
  fullyParallel: false,
  forbidOnly: isCi,
  retries: isCi ? 1 : 0,
  workers: isCi ? 1 : undefined,
  reporter: [['list'], ['html', { open: 'never' }]],
  use: {
    headless: readBoolean('WILLENIUM_BROWSER_HEADLESS', defaultHeadless),
    viewport: readViewport('WILLENIUM_BROWSER_WINDOW_SIZE', { width: 1366, height: 768 }),
    actionTimeout: 30_000,
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
  },
  projects: [
    {
      name: 'production-english',
      metadata: {
        branch: 'production',
        language: 'english',
      },
      use: {
        locale: 'en-US',
      },
    },
    {
      name: 'production-arabic',
      metadata: {
        branch: 'production',
        language: 'arabic',
      },
      use: {
        locale: 'ar-SA',
      },
    },
    {
      name: 'staging-english',
      metadata: {
        branch: 'staging',
        language: 'english',
      },
      use: {
        locale: 'en-US',
      },
    },
    {
      name: 'staging-arabic',
      metadata: {
        branch: 'staging',
        language: 'arabic',
      },
      use: {
        locale: 'ar-SA',
      },
    },
  ],
});

function readBoolean(key: string, fallback: boolean): boolean {
  const rawValue = process.env[key];
  if (!rawValue) {
    return fallback;
  }

  return ['true', '1', 'yes', 'on'].includes(rawValue.trim().toLowerCase());
}

function readViewport(key: string, fallback: { width: number; height: number }): { width: number; height: number } {
  const rawValue = process.env[key];
  if (!rawValue) {
    return fallback;
  }

  const [widthRaw, heightRaw] = rawValue.toLowerCase().split('x');
  const width = Number.parseInt(widthRaw ?? '', 10);
  const height = Number.parseInt(heightRaw ?? '', 10);

  if (Number.isNaN(width) || Number.isNaN(height)) {
    return fallback;
  }

  return { width, height };
}
