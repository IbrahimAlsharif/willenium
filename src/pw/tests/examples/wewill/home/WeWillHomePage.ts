import type { FinderLocator } from '../../../../core/helpers/Finder';

export const weWillHomeLocators: Record<string, FinderLocator> = {
  heroHeading: { css: "[data-i18n='hero.title']" },
  primaryCallToAction: { css: "[data-i18n='hero.ctaPrimary']" },
  methodologyHeading: { css: "[data-i18n='genai.card.title']" },
  bilingualOfferingCallToAction: { css: "[data-i18n='genai.cta']" },
  footerSlogan: { css: "[data-site-setting='footer_tagline']" },
};
