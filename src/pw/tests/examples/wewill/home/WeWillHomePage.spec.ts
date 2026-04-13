import { test, expect } from '../../../../fixtures/willenium.fixture';
import { weWillHomeLocators } from './WeWillHomePage';

test.describe('Protect Example Home Trust Journey (Playwright)', () => {
  test.beforeEach(async ({ go, finder, testData }) => {
    // This starts each test from the business-defined home entry point for the active profile.
    await go.navigateToJourneyStart(testData);
    await finder.waitForVisible(weWillHomeLocators.heroHeading);
  });

  test('verify hero heading visible', async ({ finder, testData }) => {
    // This confirms first-time visitors still see the expected home promise message.
    const heroHeading = await finder.get(weWillHomeLocators.heroHeading);
    await expect(heroHeading).toContainText(testData.wewillHome.heroHeading);
  });

  test('verify primary call to action visible', async ({ finder, testData }) => {
    // This protects the primary action wording that drives visitors toward the key conversion path.
    const primaryCallToAction = await finder.get(weWillHomeLocators.primaryCallToAction);
    await expect(primaryCallToAction).toContainText(testData.wewillHome.primaryCta);
  });

  test('verify methodology heading visible', async ({ finder, testData }) => {
    // This checks the trust-building methodology section remains visible with the expected label.
    const methodologyHeading = await finder.get(weWillHomeLocators.methodologyHeading);
    await expect(methodologyHeading).toContainText(testData.wewillHome.methodologyHeading);
  });

  test('verify bilingual offering call to action visible', async ({ finder, testData }) => {
    // This keeps language-access navigation discoverable with the expected bilingual CTA text.
    const bilingualOfferingCallToAction = await finder.get(weWillHomeLocators.bilingualOfferingCallToAction);
    await expect(bilingualOfferingCallToAction).toContainText(testData.wewillHome.bilingualOfferingCta);
  });

  test('verify footer slogan visible', async ({ finder, testData }) => {
    // This verifies the closing value statement stays visible as the final trust checkpoint.
    const footerSlogan = await finder.get(weWillHomeLocators.footerSlogan);
    await expect(footerSlogan).toContainText(testData.wewillHome.footerSlogan);
  });
});
