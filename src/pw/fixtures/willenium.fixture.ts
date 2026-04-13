import { test as base, expect } from '@playwright/test';

import { TestDataFactory, type TestDataModel } from '../core/data/TestDataFactory';
import { Finder } from '../core/helpers/Finder';
import { Go } from '../core/helpers/Go';
import { resolveRuntimeProfile, type RuntimeProfile } from '../core/runtime/RuntimeProfile';

interface WilleniumFixtures {
  runtimeProfile: RuntimeProfile;
  testData: TestDataModel;
  finder: Finder;
  go: Go;
}

export const test = base.extend<WilleniumFixtures>({
  runtimeProfile: async ({}, use, testInfo) => {
    const metadata = testInfo.project.metadata as { branch?: string; language?: string };
    const runtimeProfile = resolveRuntimeProfile({
      branch: metadata.branch,
      language: metadata.language,
    });

    await use(runtimeProfile);
  },

  testData: async ({ runtimeProfile }, use) => {
    const testData = TestDataFactory.load(runtimeProfile);
    await use(testData);
  },

  finder: async ({ page, runtimeProfile }, use) => {
    const finder = new Finder(page, runtimeProfile);
    await use(finder);
  },

  go: async ({ page, finder, runtimeProfile }, use) => {
    const go = new Go(page, finder, runtimeProfile);
    await use(go);
  },
});

export { expect };
