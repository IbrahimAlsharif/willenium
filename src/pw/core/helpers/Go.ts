import { expect, type Locator, type Page } from '@playwright/test';

import type { TestDataModel } from '../data/TestDataFactory';
import type { RuntimeProfile } from '../runtime/RuntimeProfile';
import type { Finder, FinderLocator } from './Finder';

export class Go {
  constructor(
    private readonly page: Page,
    private readonly finder: Finder,
    private readonly runtimeProfile: RuntimeProfile,
  ) {}

  async navigate(url: string): Promise<void> {
    await this.page.goto(url, { waitUntil: 'domcontentloaded' });
  }

  async navigateToJourneyStart(testData: TestDataModel): Promise<void> {
    const baseUrl = testData['base-url'].env[this.runtimeProfile.language];
    await this.navigate(baseUrl);
    await this.applyLanguagePreference();
  }

  async click(target: FinderLocator | Locator, name = 'target element'): Promise<void> {
    const locator = await this.resolveLocator(target, true);

    if (this.runtimeProfile.highlightInteractions) {
      await this.highlight(locator);
    }

    await locator.click({ timeout: this.runtimeProfile.waitTimeoutMs });
  }

  async type(target: FinderLocator | Locator, value: string, name = 'target field'): Promise<void> {
    const locator = await this.resolveLocator(target, true);

    if (this.runtimeProfile.highlightInteractions) {
      await this.highlight(locator);
    }

    await locator.fill(value, { timeout: this.runtimeProfile.waitTimeoutMs });

    if (this.runtimeProfile.verifyTypedText) {
      await expect(locator, `${name} should contain the typed value`).toHaveValue(value, {
        timeout: this.runtimeProfile.waitTimeoutMs,
      });
    }
  }

  async clickAndWaitForUrlContains(target: FinderLocator | Locator, expectedUrlPart: string): Promise<void> {
    await Promise.all([
      this.page.waitForURL(`**${expectedUrlPart}**`, { timeout: this.runtimeProfile.waitTimeoutMs }),
      this.click(target, expectedUrlPart),
    ]);
  }

  private async resolveLocator(target: FinderLocator | Locator, clickable: boolean): Promise<Locator> {
    if (isLocator(target)) {
      await expect(target).toBeVisible({ timeout: this.runtimeProfile.waitTimeoutMs });
      if (clickable) {
        await expect(target).toBeEnabled({ timeout: this.runtimeProfile.waitTimeoutMs });
      }
      return target;
    }

    return clickable ? this.finder.getClickable(target) : this.finder.get(target);
  }

  private async highlight(locator: Locator): Promise<void> {
    await locator.evaluate((element) => {
      const previousOutline = (element as HTMLElement).style.outline;
      (element as HTMLElement).style.outline = '2px solid #1d4ed8';
      setTimeout(() => {
        (element as HTMLElement).style.outline = previousOutline;
      }, 250);
    });
  }

  private async applyLanguagePreference(): Promise<void> {
    const expectedLanguageCode = this.runtimeProfile.language === 'arabic' ? 'ar' : 'en';
    const currentLanguageCode = (await this.page.locator('html').getAttribute('lang'))?.toLowerCase() ?? '';

    if (currentLanguageCode.startsWith(expectedLanguageCode)) {
      return;
    }

    const languageToggle = this.page.locator(`.lang-btn[data-lang='${expectedLanguageCode}']`).first();
    const hasToggle = (await languageToggle.count()) > 0;

    if (!hasToggle || !(await languageToggle.isVisible())) {
      throw new Error(
        `The runtime requested '${this.runtimeProfile.language}' but the page did not expose a visible language toggle for ${expectedLanguageCode}.`,
      );
    }

    await languageToggle.click({ timeout: this.runtimeProfile.waitTimeoutMs });
    await expect(this.page.locator('html')).toHaveAttribute('lang', new RegExp(`^${expectedLanguageCode}`), {
      timeout: this.runtimeProfile.waitTimeoutMs,
    });
  }
}

function isLocator(target: FinderLocator | Locator): target is Locator {
  return typeof (target as Locator).click === 'function';
}
