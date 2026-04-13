import { expect, type Locator, type Page } from '@playwright/test';

import type { RuntimeProfile } from '../runtime/RuntimeProfile';

export interface FinderLocator {
  id?: string;
  name?: string;
  css?: string;
  xpath?: string;
}

export class Finder {
  constructor(
    private readonly page: Page,
    private readonly runtimeProfile: RuntimeProfile,
  ) {}

  async get(target: FinderLocator): Promise<Locator> {
    const locator = this.resolve(target);
    await expect(locator).toBeVisible({ timeout: this.runtimeProfile.waitTimeoutMs });
    return locator;
  }

  async getClickable(target: FinderLocator): Promise<Locator> {
    const locator = this.resolve(target);
    await expect(locator).toBeVisible({ timeout: this.runtimeProfile.waitTimeoutMs });
    await expect(locator).toBeEnabled({ timeout: this.runtimeProfile.waitTimeoutMs });
    return locator;
  }

  async waitForVisible(target: FinderLocator): Promise<void> {
    const locator = this.resolve(target);
    await expect(locator).toBeVisible({ timeout: this.runtimeProfile.waitTimeoutMs });
  }

  async waitForHidden(target: FinderLocator): Promise<void> {
    const locator = this.resolve(target);
    await expect(locator).toBeHidden({ timeout: this.runtimeProfile.waitTimeoutMs });
  }

  async elementIsVisible(target: FinderLocator): Promise<boolean> {
    const locator = this.resolve(target);
    return locator.isVisible();
  }

  resolve(target: FinderLocator): Locator {
    if (target.id) {
      return this.page.locator(`[id='${escapeAttribute(target.id)}']`);
    }

    if (target.name) {
      return this.page.locator(`[name='${escapeAttribute(target.name)}']`);
    }

    if (target.css) {
      return this.page.locator(target.css);
    }

    if (target.xpath) {
      return this.page.locator(`xpath=${target.xpath}`);
    }

    throw new Error('Finder locator must include id, name, css, or xpath.');
  }
}

function escapeAttribute(rawValue: string): string {
  return rawValue.replace(/'/g, "\\'");
}
